__author__ = 'Nacturne'

import argparse
import os

parser = argparse.ArgumentParser()

parser.add_argument('-i', type=str, default=None)
parser.add_argument('-o', type=str, default=None)
parser.add_argument('-s', type=str, default=None)

args = parser.parse_args()

from lib import tools as t

if args.s:
    tree = t.Parse(args.s)
    key_leaves = t.indexTree(tree)
    key_leaves.sort(key = lambda x: x.wordOrder)
    key_paths = t.keyPaths(key_leaves)
    key_nodes = t.keyNodes(key_paths)

    all_nodes = tree.allNodes()

    for node in all_nodes:
        if not (node in key_nodes):
            node.score = 2

    new_sentence = t.Recover(tree.root)
    print 'The mechanical sentence is:\n'+new_sentence

else:
    fileNames_i = os.listdir('input/')
    fileNames_o = os.listdir('output/')

    if not (args.i in fileNames_i):
        print("Cannot find the file '" + str(args.i) + "'.")
    elif args.o in fileNames_o:
        print("File name '" + str(args.o) + "' has already existed.")
    else:
        file_in = open('input/' + args.i, 'r')
        file_out = open('output/' + args.o, 'w')

        for line in file_in:
            tree = t.Parse(line)
            key_leaves = t.indexTree(tree)
            key_leaves.sort(key = lambda x: x.wordOrder)
            key_paths = t.keyPaths(key_leaves)
            key_nodes = t.keyNodes(key_paths)

            all_nodes = tree.allNodes()

            for node in all_nodes:
                if not (node in key_nodes):
                    node.score = 2
            new_sentence = t.Recover(tree.root)
            file_out.write(new_sentence + '\n')

        file_in.close()
        file_out.close()

        print("Done!\nThe mechanical sentences are stored in '/output/" + args.o +"'.")