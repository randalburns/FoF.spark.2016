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

    val input = sc.textFile("./simple.input/*").flatMap(lineToTriples).map(x=>(x.toString,1)).reduceByKey(_+_).filter{ case (x,y) => y>1}.map{ case (x,y)=>x}.saveAsTextFile("./output")
  }
}
