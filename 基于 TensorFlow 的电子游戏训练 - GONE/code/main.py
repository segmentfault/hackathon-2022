import argparse
import os
import random
import numpy as np
import gym
import tensorflow as tf
import tensorlayer as tl


parser = argparse.ArgumentParser()
parser.add_argument('--train', dest='train', default=True)
parser.add_argument('--test', dest='test', default=True)

args = parser.parse_args()

TRAIN_EPISODE = 400
TEST_EPISODE = 40

class ReplayBuffer:
    def __init__(self, capacity=300000):
        self.capacity = capacity
        self.batch_size = 512
        self.buffer = []
        self.position = 0

    def store(self, state, action, reward, next_state, done):
        if len(self.buffer) < self.capacity:
            self.buffer.append(None)
        self.buffer[self.position] = (state, action, reward, next_state, done)
        self.position = int((self.position + 1) % self.capacity)

    def sample(self, batch_size):
        batch = random.sample(self.buffer, batch_size)
        state, action, reward, next_state, done = map(np.stack, zip(*batch))
        return state, action, reward, next_state, done

class Net:
    def __init__(self, state_dim, action_dim):
        self.input_layer = tl.layers.Input([None, state_dim])
        self.layer_1 = tl.layers.Dense(n_units=128, act=tf.nn.relu)(self.input_layer)
        self.layer_2 = tl.layers.Dense(n_units=256, act=tf.nn.relu)(self.layer_1)
        self.output_layer = tl.layers.Dense(n_units=action_dim)(self.layer_2)

        self.model = tl.models.Model(inputs=self.input_layer, outputs=self.output_layer)
        self.target_model = tl.models.Model(inputs=self.input_layer, outputs=self.output_layer)
        self.model.train()
        self.target_model.eval()
        self.optimizer = tf.optimizers.Adam(lr=0.0001)

class Agent:
    def __init__(self, env):
        self.gamma = 0.99
        self.eps_min = 0.01
        self.eps_max = 1.0

        self.epsilon = self.eps_max
        self.adjuster = 0.9995

        self.env = env
        self.state_dim = self.env.observation_space.shape[0]
        self.action_dim = self.env.action_space.n
        self.net = Net(self.state_dim, self.action_dim)
        self.replay_buffer = ReplayBuffer()

    def target_update(self):
        for weights, target_weights in zip(
                self.net.model.trainable_weights, self.net.target_model.trainable_weights):
            target_weights.assign(weights)

    def choose_action(self, state):
        if np.random.uniform() < self.epsilon:
            return self.env.action_space.sample()
        else:
            q_value = self.net.model(state[np.newaxis, :])[0]
            return np.argmax(q_value)

    def update_epsilon(self):
        self.epsilon = max(self.eps_min, self.epsilon * self.adjuster)

    def replay(self):
        for _ in range(10):
            states, actions, rewards, next_states, done = self.replay_buffer.sample(self.replay_buffer.batch_size)
            target = self.net.target_model(states).numpy()
            next_target = self.net.target_model(next_states)
            next_q_value = tf.reduce_max(next_target, axis=1)
            target[range(self.replay_buffer.batch_size), actions] = rewards + (1 - done) * self.gamma * next_q_value

            with tf.GradientTape() as tape:
                q_pred = self.net.model(states)
                loss = tf.losses.mean_squared_error(target, q_pred)
            grads = tape.gradient(loss, self.net.model.trainable_weights)
            self.net.optimizer.apply_gradients(zip(grads, self.net.model.trainable_weights))


    def test_episode(self, test_episodes):
        for episode in range(test_episodes):
            state = self.env.reset()
            total_reward, done = 0, False
            while not done:
                action = self.net.model(np.array([state], dtype=np.float32))[0]
                action = np.argmax(action)
                next_state, reward, done, _ = self.env.step(action)
                next_state = next_state.astype(np.float32)

                total_reward += reward
                state = next_state
                self.env.render()
            print("Test {} | episode rewards is {}".format(episode, total_reward))

    def train(self, train_episodes=200):
        if args.train:
            for episode in range(train_episodes):
                episode_reward_sum, done = 0, False
                state = self.env.reset()
                while not done:
                    self.env.render()
                    action = self.choose_action(state)
                    next_state, reward, done, _ = self.env.step(action)
                    next_state = next_state.astype(np.float32)
                    self.replay_buffer.store(state, action, reward, next_state, done)
                    episode_reward_sum += reward
                    state = next_state
                if len(self.replay_buffer.buffer) > self.replay_buffer.batch_size:
                    self.replay()
                    self.target_update()
                    self.update_epsilon()
                print('EP{} EpisodeReward={}'.format(episode, episode_reward_sum))
            self.saveModel()
        if args.test:
            self.loadModel()
            self.test_episode(TEST_EPISODE)

    def saveModel(self):
        path = os.path.join('model', '_'.join(['DQN', 'LunarLander-v2']))
        if not os.path.exists(path):
            os.makedirs(path)
        tl.files.save_weights_to_hdf5(os.path.join(path, 'model.hdf5'), self.net.model)
        tl.files.save_weights_to_hdf5(os.path.join(path, 'target_model.hdf5'), self.net.target_model)
        print('Saved weights.')

    def loadModel(self):
        path = os.path.join('model', '_'.join(['DQN', 'LunarLander-v2']))
        if os.path.exists(path):
            print('Load DQN Network parametets ...')
            tl.files.load_hdf5_to_weights_in_order(os.path.join(path, 'model.hdf5'), self.net.model)
            tl.files.load_hdf5_to_weights_in_order(os.path.join(path, 'target_model.hdf5'), self.net.target_model)
            print('Load weights!')
        else: print("No model file find, please train model first...")


if __name__ == '__main__':
    env = gym.make('LunarLander-v2')
    agent = Agent(env)
    agent.train(TRAIN_EPISODE)
    env.close()