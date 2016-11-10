#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/default-java/
export HADOOP_CLASSPATH=${JAVA_HOME}lib/tools.jar
/usr/local/spark/hadoop-2.7.3/bin/hadoop com.sun.tools.javac.Main FoF.java 
jar cf fof.jar FoF*.class
rm -rf output/ time/
/usr/local/spark/hadoop-2.7.3/bin/hadoop jar fof.jar FoF ../fof.input ./output
