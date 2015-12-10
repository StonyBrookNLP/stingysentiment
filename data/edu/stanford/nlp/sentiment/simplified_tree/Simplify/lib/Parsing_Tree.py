__author__ = 'Nacturne'


class Node(object):
    def __init__(self, score = -1, lchild = None, rchild = None, parent = None):
        self.score = score
        self.lchild = lchild
        self.rchild = rchild
        self.parent = parent
        self.index = None
        self.word = None
        self.wordOrder = None

    def isRoot(self):
        if self.parent == None:
            return True
        else:
            return False

    def isLeaf(self):
        if self.lchild == None and self.rchild == None:
            return True
        else:
            return False

    def level(self): # Return the level of the node
        counter = 0
        p = self
        while not p.isRoot():
            p = p.parent
            counter = counter +1
        return counter

    def sign(self):
        if self.score < 2:
            sign = -1
        elif self.score == 2:
            sign = 0
        else:
            sign = 1

        return sign

    def sub_nodes(self):
        l = []
        temp = []

        temp.append(self)

        while len(temp) != 0:
            x = temp.pop()
            l.append(x)
            if x.lchild != None:
                temp.append(x.lchild)
            if x.rchild != None:
                temp.append(x.rchild)
        return l[1:]

    def dist_leaf(self):
        if self.isLeaf():
            dist = 0
        else:
            l = self.sub_nodes()
            l = [i.level() for i in l]
            dist = max(l) - self.level()
        return dist


class PTree(object):
    def __init__(self, score):
        self.root = Node(score = score, lchild = None, rchild = None, parent = None)

    def order_print(self, start = -1):
        #use this if-else to make sure that the defalt value for 'start' is self.root
        if start == -1:
            node = self.root
        else:
            node = start

        if node == None:
            return

        left = self.order_print(start = node.lchild)
        right = self.order_print(start = node.rchild)

        res = [node.score]

        if left != None:
            res.extend(left)
        if right != None:
            res.extend(right)
        return res


    def allNodes(self):
        l = []
        temp = []

        temp.append(self.root)

        while len(temp) != 0:
            x = temp.pop()
            l.append(x)

            if x.lchild != None:
                temp.append(x.lchild)
            if x.rchild != None:
                temp.append(x.rchild)
        return l


