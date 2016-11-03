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
    return outtriples.toList//.map(x => (x,0))
  }


  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("FoF Spark")
    val sc = new SparkContext(conf)

    val t0 = System.nanoTime()

// Small test input
//    val triples = sc.textFile("./simple.input/*").flatMap(lineToTriples)
// Big input
    val triples = sc.textFile("./fof.input/*").flatMap(lineToTriples)

    // Same implementation as MR FoF
    //  -- convert to ((a,b,c,),1)
    //  -- sum triples count in reducer
    //  -- take only those with 2 or more 
//    triples.map(x=>(x.toString,1))
//            .reduceByKey(_+_)
//            .filter{ case (x,y) => y>1 }
//            .map{ case (x,y)=>x }
//            .saveAsTextFile("./output")

    // Optimized implementation
    //  -- convert to ((a,b,c,),partition)
    //  -- ???
    //  -- ???
    //  -- take just the keys
    //  -- output only unique keys
    val ptriples = triples.mapPartitionsWithIndex((idx, it)=> it.map(x => (x.toString,idx.toString)))
    ptriples.join(ptriples)
            .filter{ case(k,(v1,v2)) => v1!=v2 }
            .keys
            .distinct
            .saveAsTextFile("./output")

    val t1 = System.nanoTime()
    val timedata = Array ( t0, t1, t1-t0 )
    sc.parallelize(timedata).saveAsTextFile("./time")
  }
}
