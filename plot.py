import csv
import matplotlib.pyplot as plt
import pandas as pd

data = pd.read_csv('dataAnglesParametricGriewankFunction.txt', 
    names=['EPISODE', 'EPOCH', 'GAP', 'FVALUE', 'X', 'Y', 'REWARD'], sep=";")

fig, (ax1, ax2) = plt.subplots(2)

episodes = []

for e in range(0, max(data['EPISODE']), 30):
    episode = data.loc[data['EPISODE'] == e]
    ax1.step(episode['EPOCH'], episode['GAP'])
    ax2.step(episode['EPOCH'], episode['FVALUE'])
    #ax3.step(episode['EPOCH'], episode['REWARD'])
    episodes.append("Episode " + str(e))

greedyEpisode = data.loc[data['EPISODE'] == max(data['EPISODE'])]
ax1.step(greedyEpisode['EPOCH'], greedyEpisode['GAP'], linewidth = 3, color='r')
ax2.step(greedyEpisode['EPOCH'], greedyEpisode['FVALUE'], linewidth = 3, color='r')
#ax3.step(greedyEpisode['EPOCH'], greedyEpisode['REWARD'], linewidth = 3, color='r')

episodes.append('Greedy')

ax1.set(xlabel='Epochs', ylabel='Gap value')
ax1.legend(episodes)

ax2.set(xlabel='Epochs', ylabel='Function value')
ax2.legend(episodes)

plt.show()