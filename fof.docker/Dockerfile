from ubuntu:latest

RUN apt-get -y update
RUN apt-get -y install\
  apt-transport-https \
  apt-utils \
  emacs \
  nano \
  scala \
  vim \
  wget

RUN apt-get -y update
RUN apt-get -y install default-jre default-jdk

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get -y update
RUN apt-get -y install sbt

WORKDIR /usr/local/spark
  
RUN wget http://d3kbcqa49mib13.cloudfront.net/spark-2.0.1-bin-hadoop2.7.tgz && tar -xzf ./spark-2.0.1-bin-hadoop2.7.tgz && rm ./spark-2.0.1-bin-hadoop2.7.tgz 

WORKDIR /usr/local/hadoop
RUN wget http://apache.cs.utah.edu/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz && tar -xvzf ./hadoop-2.7.3.tar.gz && tar -xvzf ./hadoop-2.7.3.tar.gz


# Change line below in any way to get a fresh pull
ENV PULL=11.16.16
RUN mkdir /FoF 
WORKDIR /FoF
RUN git clone https://github.com/randalburns/FoF.spark.2016.git 

WORKDIR /FoF/FoF.spark.2016/fof.simple
RUN sbt package

WORKDIR /FoF/FoF.spark.2016/fof.opt
RUN sbt package

ENV JAVA_HOME=/usr/lib/jvm/default-java/
ENV HADOOP_CLASSPATH=/usr/lib/jvm/default-java/lib/tools.jar

WORKDIR /FoF/FoF.spark.2016

