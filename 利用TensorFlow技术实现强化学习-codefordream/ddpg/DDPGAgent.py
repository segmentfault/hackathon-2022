import argparse
from itertools import count

import os, sys, random
import numpy as np

import gym
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from torch.distributions import Normal
from tensorboardX import SummaryWriter


memory_size = 1000
batch_size = 100

# update_iteration = 100
gamma = 0.99
tau = 0.005


class Replay_buffer():
    def __init__(self, max_size=memory_size):
        self.storage = []
        self.max_size = max_size
        self.ptr = 0

    def push(self, data):
        if len(self.storage) == self.max_size:
            self.storage[int(self.ptr)] = data
            self.ptr = (self.ptr + 1) % self.max_size
        else:
            self.storage.append(data)

    def sample(self, batch_size):
        ind = np.random.randint(0, len(self.storage), size=batch_size)
        x, y, u, r, d = [], [], [], [], []

        for i in ind:
            X, Y, U, R, D = self.storage[i]
            x.append(np.array(X, copy=False))
            y.append(np.array(Y, copy=False))
            u.append(np.array(U, copy=False))
            r.append(np.array(R, copy=False))
            d.append(np.array(D, copy=False))

        return np.array(x), np.array(y), np.array(u), np.array(r).reshape(-1, 1), np.array(d).reshape(-1, 1)


class Actor(nn.Module):
    def __init__(self, state_dim, hidden_size, action_dim, max_action):
        super(Actor, self).__init__()

        self.l1 = nn.Linear(state_dim, hidden_size)
        self.l2 = nn.Linear(hidden_size, hidden_size)
        self.l3 = nn.Linear(hidden_size, action_dim)
        self.sg = nn.Sigmoid()

        self.max_action = max_action

    def forward(self, x):
        x = F.relu(self.l1(x))
        x = F.relu(self.l2(x))
        x = self.l3(x)
        x = self.sg(x) * self.max_action
        # x = self.max_action * torch.tanh(self.l3(x))
        # x = self.l3(x)
        # x = torch.argmax(x).detach().numpy()
        return x


class Critic(nn.Module):
    def __init__(self, state_dim,hidden_size, action_dim):
        super(Critic, self).__init__()

        self.l1 = nn.Linear(state_dim + action_dim, hidden_size)
        self.l2 = nn.Linear(hidden_size , hidden_size)
        self.l3 = nn.Linear(hidden_size, 1)

    def forward(self, x, u):
        u = u.view(-1,1)
        x = F.relu(self.l1(torch.cat([x, u], 1)))
        x = F.relu(self.l2(x))
        x = self.l3(x)
        return x



class DDPG(object):
    def __init__(self, state_dim, hidden_size, action_dim, max_action):
        self.actor = Actor(state_dim, hidden_size, action_dim, max_action)
        self.actor_target = Actor(state_dim, hidden_size, action_dim, max_action)
        self.actor_target.load_state_dict(self.actor.state_dict())
        self.actor_optimizer = optim.Adam(self.actor.parameters(), lr=1e-4)

        self.critic = Critic(state_dim, hidden_size, action_dim)
        self.critic_target = Critic(state_dim, hidden_size, action_dim)
        self.critic_target.load_state_dict(self.critic.state_dict())
        self.critic_optimizer = optim.Adam(self.critic.parameters(), lr=1e-3)

        self.replay_buffer = Replay_buffer()

    # def choose_action(self, state):
    #     state = torch.FloatTensor(state.reshape(1, -1))
    #     # return int("".join(map(str, torch.argmax(self.actor(state)).detach().numpy().flatten())))
    #     return self.actor(state).detach().numpy().flatten()

    def choose_action(self, state: np.ndarray):
        state_torch = torch.from_numpy(state).float()
        action = self.actor(state_torch).squeeze()
        return action.detach().numpy()

    def update(self):
        x, y, u, r, d = self.replay_buffer.sample(batch_size)
        state = torch.FloatTensor(x)
        action = torch.FloatTensor(u)
        next_state = torch.FloatTensor(y)
        done = torch.FloatTensor(1 - d)
        reward = torch.FloatTensor(r)

        target_Q = self.critic_target(next_state, self.actor_target(next_state))
        target_Q = reward + (done * gamma * target_Q)  # .detach()
        current_Q = self.critic(state, action)
        critic_loss = F.mse_loss(current_Q, target_Q)
        self.critic_optimizer.zero_grad()
        critic_loss.backward()
        self.critic_optimizer.step()

        actor_loss = -self.critic(state, self.actor(state)).mean()
        self.actor_optimizer.zero_grad()
        actor_loss.backward()
        self.actor_optimizer.step()

        for param, target_param in zip(self.critic.parameters(), self.critic_target.parameters()):
            target_param.data.copy_(tau * param.data + (1 - tau) * target_param.data)

        for param, target_param in zip(self.actor.parameters(), self.actor_target.parameters()):
            target_param.data.copy_(tau * param.data + (1 - tau) * target_param.data)

