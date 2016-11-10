import org.apache.spark.SparkContext 
import org.apache.spark.SparkContext._ 
import org.apache.spark._  

object FoF {

  def lineToSortedTriples ( line: String ) : List[(Int,Int,Int)] = 
  {
    var fids:Array[Int] = line.split(" ").map(_.toInt)
    var outtriples = scala.collection.mutable.ListBuffer.empty[(Int,Int,Int)]

    var minel : Int = 0
    var midel : Int = 0
    var maxel : Int = 0

    // Output all possible triples of friends. 
    //   Propose yourself and B to A, yourself and A to B
    for ( i <- 1 until (fids.length -1) )
    {
      for ( j <- i+1 until (fids.length) )
      {
        minel = math.min ( fids(0), math.min( fids(i), fids(j)))
        maxel = math.max ( fids(0), math.max( fids(i), fids(j)))
        if ( fids(0) == minel ) {
          midel = math.min ( fids(i), fids(j) ) 
        } else if ( fids(0) == maxel ) {
          midel = math.max ( fids(i), fids(j) ) 
        } else {
          midel = fids(0)
        }

        outtriples.append ((minel,midel,maxel))
      }
    }
    return outtriples.toList
  }


  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("FoF Spark")
    val sc = new SparkContext(conf)

    val t0 = System.nanoTime()

// Small test input
//    val triples = sc.textFile("../simple.input/*").flatMap(lineToSortedTriples)
// Big input
    val triples = sc.textFile("../fof.input/*").flatMap(lineToSortedTriples)

    triples.map(x=>(x,1))
            .reduceByKey(_+_)
            .filter{ case (x,y) => y>2 }
            .map{ case (x,y)=>x }
            .flatMap{ case (t) => List((t._1,t._2,t._3),(t._2,t._1,t._3),(t._3,t._1,t._2)) }
            .saveAsTextFile("./output")

    val t1 = System.nanoTime()
    val timedata = Array ( t0, t1, t1-t0 )
    sc.parallelize(timedata).saveAsTextFile("./time")
  }
}
