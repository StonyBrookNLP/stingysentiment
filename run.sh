model=noise
leaf=2
workingSet=train

for re in 1 2 3
	do
		if java -classpath jre/lib/jfxswt.jar:jre/lib/jce.jar:jre/lib/rt.jar:jre/lib/jsse.jar:jre/lib/javaws.jar:jre/lib/management-agent.jar:jre/lib/jfr.jar:jre/lib/resources.jar:jre/lib/plugin.jar:jre/lib/charsets.jar:jre/lib/deploy.jar:jre/lib/ext/sunec.jar:jre/lib/ext/nashorn.jar:jre/lib/ext/localedata.jar:jre/lib/ext/cldrdata.jar:jre/lib/ext/jfxrt.jar:jre/lib/ext/sunpkcs11.jar:jre/lib/ext/sunjce_provider.jar:jre/lib/ext/dnsns.jar:jre/lib/ext/jaccess.jar:jre/lib/ext/zipfs.jar:build/classes/main:build/resources/main:lib/lucene-core-4.10.3.jar:lib/slf4j-api.jar:lib/ant-contrib-1.0b3.jar:lib/ejml-0.23.jar:lib/lucene-analyzers-common-4.10.3.jar:lib/joda-time.jar:lib/xom-1.2.10.jar:lib/commons-logging.jar:lib/jollyday-0.4.7.jar:lib/AppleJavaExtensions.jar:lib/javax.json.jar:lib/protobuf.jar:lib/jflex-1.5.1.jar:lib/lucene-queryparser-4.10.3.jar:lib/lucene-demo-4.10.3.jar:lib/jgrapht-core-0.9.1.jar:lib/lucene-queries-4.10.3.jar:lib/appbundler-1.0.jar:lib/junit.jar:lib/log4j-1.2.16.jar:lib/javax.servlet.jar:lib/javacc.jar:lib/slf4j-simple.jar:lib/commons-lang3-3.1.jar edu.stanford.nlp.sentiment.SentimentPipeline -sentimentModel data/edu/stanford/nlp/sentiment/models/model_phrase_${model}_leaf${leaf}_${re}/model.ser.gz -file data/edu/stanford/nlp/sentiment/trees/${workingSet}.txt -input TREES -output PENNTREES > outputs/${model}/${model}_leaf${leaf}_re${re}_${workingSet}.txt
		then
			echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++Successful for ${model}_leaf${leaf}_re${re}_${workingSet}.txt"
		else
			echo"-------------------------------------------------------Wrong for ${model}_leaf${leaf}_re${re}_${workingSet}.txt"
		fi
	done