with open('data/edu/stanford/nlp/sentiment/trees/dev.txt') as f:
	for line in f:
		if line[1] != '2':
			print line[:-1]

