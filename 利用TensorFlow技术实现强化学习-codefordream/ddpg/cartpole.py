import gym
from DDPGAgent import DDPG
import numpy as np

env = gym.make('CartPole-v0')
env.seed(1)
env = env.unwrapped

state_dim = env.observation_space.shape[0]
# action_dim = env.action_space.n
max_action = 1
action_dim=1

max_episode = 3000

RL = DDPG(state_dim,400,action_dim,max_action)

for i_episode in range(max_episode):
    observation = env.reset()
    while True:
        env.render()
        action = RL.choose_action(observation)
        observation_, reward, done, _ ,_ = env.step(round(np.float(action)))
        RL.replay_buffer.push((observation, observation_, action, reward, np.float(done)))
        RL.update()
        observation = observation_
        if done:
            break
