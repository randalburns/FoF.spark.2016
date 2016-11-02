

## Install docker

## Pull the docker container

## Run the docker container and login

## Build and run the FoF example

<code>
  sbt package
  ~spark-2.0.1-bin-hadoop2.7/bin/spark-submit --class "FoF" ./target/scala-2.11/fof-spark_2.11-1.0.jar 
</code>




## Old
## Minimal spark

Verify that you have scala installed
<code>
  scala
</code>
Otherwise, install scala
<code>
  sudo apt install scala
</code>

Download and install spark into:
<code>
  ~/spark-2.0.1-bin-hadoop2.7
</code>

Change to that directory 

Run the spark standalone server
<code>
  ./sbin/start-master.sh
</code>

Launch the spark shell
<code>
  ./bin/spark-shell
</code>

If you end up with a Scala prompt, you're good

# Set up a spark project

Pull the repository
<code>
  git pull .....
</code>

Install sbt
<code>
  


