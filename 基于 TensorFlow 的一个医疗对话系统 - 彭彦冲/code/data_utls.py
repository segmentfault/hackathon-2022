import os
import jieba
from tkinter import _flatten

'''
语料库预处理
'''

file_path = './dialog'  # 语料库存储路径
file_names = os.listdir(file_path)  # 列举指定路径的所有文件
save_path = './ids'  # 保存处理后的文档路径

# 1 读取指定文件夹内的所有语料文件
corpus = []
i = 0
for file_name in file_names:
    with open(os.path.join(file_path, file_name), 'r', encoding='utf-8') as f:
        corpus.extend(f.readlines())
corpus = corpus[0:24000:]
print(corpus)

# 2 中文分词
corpus_cut = [jieba.lcut(i.replace('\n', '')) for i in corpus]
print(corpus_cut)
# 3 构建词典
f = _flatten(corpus_cut)  # 展平

all_dict = list(set(f))  # 去重，得到词典

# 4 存储词典和分词后的语料文件到指定路径
if not os.path.exists(save_path):
    os.mkdir(save_path)
# 分离问题和答案
source = corpus_cut[::2]  # from: to: step
target = corpus_cut[1::2]

with open(os.path.join(save_path, 'all_dict.txt'), 'w', encoding='utf-8') as f:
    f.writelines('\n'.join(all_dict))
with open(os.path.join(save_path, 'source.txt'), 'w', encoding='utf-8') as f:
    f.writelines([' '.join(i) + '\n' for i in source])
with open(os.path.join(save_path, 'target.txt'), 'w', encoding='utf-8') as f:
    f.writelines([' '.join(i) + '\n' for i in target])
