__author__ = 'Nacturne'

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

from Parsing_Tree import Node, PTree

def show_greater(dataframe, value):
    temp = pd.DataFrame()
    for i in dataframe.columns.values:
         if dataframe[i].max() >= value:
            temp[i] = dataframe[i]

    return temp

def df_col_bar(df, str, percentage = True, show = True):
    if percentage:
        df = df / df.sum()
    length = len(df[str])
    plt.bar(range(length), df[str], align = 'center')
    plt.xticks(range(length), df.index)
    if percentage:
        plt.ylabel('percentage  of  ('+ str + ')')
        plt.ylim(0.0,1.0)
    else:
        plt.ylabel('counters  for  (' + str + ')')
    plt.xlabel(df.index.name)
    if show:
        plt.show()

def df_row_bar(df, row, col): # always draw in percentage way
    for i in range(1,row*col+1):
         plt.subplot(row,col,i)
         df_col_bar(df, df.columns.values[i-1], show=False)
    plt.show()

def Parse(x): # Take a sentence, convert it to a tree
    tree = PTree(int(x[1]))
    current = tree.root
    counter = 0
    for i in range(3,len(x)):
        if x[i] == "(":
            if current.lchild == None:
                current.lchild = Node(score = int(x[i+1]), parent = current)
                current = current.lchild
            else:
                current.rchild = Node(score = int(x[i+1]), parent = current)
                current = current.rchild
            if x[i+3] != '(':
                temp_index = i + 3
                while x[temp_index] != ')':
                    temp_index += 1
                current.word = x[(i+3):temp_index]
                current.wordOrder = counter
                counter += 1
        elif x[i] == ")":
            current = current.parent

    return tree

def indexTree(T):
    '''
    :param T:  :param T: Assign indices to nodes from top-bottom & left-right.
    :return: Return a list of non-neutral leaves, sorted by indices.
                the next function will return key_leaves starting at a specific node
                this method also gives key_leaves starting at root.
                I implement in this way to reduce the amount of computation.
                Because we will always need key_leaves starting at root when calculating the key_nodes.
    '''
    temp = []
    key_leaves = []
    temp.append(T.root)
    counter = 0
    while len(temp) != 0:
        x = temp.pop(0)
        x.index = counter
        counter += 1
        if not x.isLeaf():
            temp.append(x.lchild)
            temp.append(x.rchild)
        elif x.score != 2:
            key_leaves.append(x)
    return key_leaves

def keyLeaves(node):
    temp = []
    key_leaves = []
    temp.append(node)
    while len(temp) != 0:
        x = temp.pop(0)
        if not x.isLeaf():
            temp.append(x.lchild)
            temp.append(x.rchild)
        elif x.score != 2:
            key_leaves.append(x)
    return key_leaves


def keyPaths(key_leaves):
    '''
    :param key_leaves: the list of key_leaves
    :return: the list of path of every key_leaves
    '''
    key_paths = []
    for i in key_leaves:
        temp = [i]
        while not i.isRoot():
            i = i.parent
            temp.append(i)
        key_paths.append(temp)
    return key_paths

def keyNodes(key_paths):
    '''
    :param key_paths: a list of path of every key_leaves
    :return: a list of key_nodes sorted by their index
    '''
    key_nodes = []
    key_nodes.append(key_paths[0][-1])

    for i in range(len(key_paths)-1):
        current = key_paths[i][::-1]
        next = key_paths[i+1][::-1]
        j = 1
        while current[j].index == next[j].index and j < min(len(current), len(next)):
            j += 1
        if j != 1:
            key_nodes.append(current[j-1])
        key_nodes.append(current[-1])

    key_nodes.append(key_paths[-1][0])
    key_nodes.sort(key=lambda node: node.index)
    return key_nodes


def keyNodesTree(key_nodes):
    '''
    :param a list of key_nodes sorted by their index
    :return the root node of the tree consisting of only key nodes
    '''
    root = key_nodes[0]
    for node in key_nodes[1:]:
        current = node
        while not (current.parent in key_nodes):
            current = current.parent
        if current.parent.lchild == current:
            current.parent.lchild = node
        else:
            current.parent.rchild = node
    return root


def Recover(node):
    if node.lchild.isLeaf() and node.rchild.isLeaf():
        x = '(' + str(node.score) + ' ('+ str(node.lchild.score) + ' ' + node.lchild.word + ') (' + str(node.rchild.score) + ' ' + node.rchild.word + '))'
    elif node.lchild.isLeaf() and (not node.rchild.isLeaf()):
        x = '(' + str(node.score) + ' ('+ str(node.lchild.score) + ' ' + node.lchild.word + ') ' + Recover(node.rchild) + ')'
    elif (not node.lchild.isLeaf()) and node.rchild.isLeaf():
        x = '(' + str(node.score) + ' ' + Recover(node.lchild) + ' (' + str(node.rchild.score) + ' ' + node.rchild.word + '))'
    else:
        x = '(' + str(node.score) + ' ' + Recover(node.lchild) + ' ' + Recover(node.rchild) + ')'
    return x


def wordList(x):
    '''
    :param a sentence in PTB format
    :return a list of words
    '''
    words = []
    for i in range(len(x)):
        if x[i] == '(' and x[i+3] != '(':
            j = i+3
            while x[j] != ')':
                j += 1
            words.append(x[i+3:j])
    return words


'''
import tools as t
x="(4 (2 You) (4 (3 (2 (0 'll) (4 probably)) (4 (4 love) (2 it))) (2 .)))"
tree = t.Parse(x)
key_leaves = t.indexTree(tree)
t.Recover(tree.root)

kl = t.keyLeaves(tree.root)
[i.word for i in kl]

key_paths = t.keyPaths(key_leaves)
key_nodes = t.keyNodes(key_paths)
root = t.keyNodesTree(key_nodes)
'''

