import org.apache.spark.SparkContext 
import org.apache.spark.SparkContext._ 
import org.apache.spark._  

object FoF {

  def lineToTriples ( line: String ) : List[(Int,Int,Int)] = 
  {
    var fids:Array[Int] = line.split(" ").map(_.toInt)
    var outtriples = scala.collection.mutable.ListBuffer.empty[(Int,Int,Int)]

    // Output all possible triples of friends. 
    //   Propose yourself and B to A, yourself and A to B
    for ( i <- 1 until (fids.length -1) )
    {
      for ( j <- i+1 until (fids.length) )
      {
        var source = fids(0)
        var fi = fids(i)
        var fj = fids(j)

        var fsecond = math.min(fids(0),fids(j))
        var fthird = math.max(fids(0),fids(j))
        outtriples.append((fi,fsecond,fthird))

        fsecond = math.min(fids(0),fids(i))
        fthird = math.max(fids(0),fids(i))
        outtriples.append((fj,fsecond,fthird))
      }
    }
    return outtriples.toList
  }

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("FoF Spark")
    val sc = new SparkContext(conf)

    val t0 = System.nanoTime()

// Small test input
//    val triples = sc.textFile("../simple.input/*").flatMap(lineToTriples)
// Big input
    val triples = sc.textFile("../fof.input/*").flatMap(lineToTriples)

    // Same implementation as MR FoF
    //  -- convert to ((a,b,c,),1)
    //  -- sum triples count in reducer
    //  -- take only those with 2 or more 
    triples.map(x=>(x.toString,1))
            .reduceByKey(_+_)
            .filter{ case (x,y) => y>1 }
            .map{ case (x,y)=>x }
            .saveAsTextFile("./output")

    val t1 = System.nanoTime()
    val timedata = Array ( t0, t1, t1-t0 )
    sc.parallelize(timedata).saveAsTextFile("./time")
  }
}
