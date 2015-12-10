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
    key_root = t.keyNodesTree(key_nodes)
    line_simplified = t.Recover(key_root)
    print 'The simpified sentence is:\n'+line_simplified

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
            key_root = t.keyNodesTree(key_nodes)
            line_simplified = t.Recover(key_root)

            file_out.write(line_simplified + '\n')

        file_in.close()
        file_out.close()

        print("Done!\nOutput stored in '/output/" + args.o +"'.")