with open("sample.out") as f:
	for line in f:
		if line[0] == '(' and line[1] in {'0','1','2','3','4'}:
			print line[:-1]
	f.close()
