package Sample.Test

object Array {

  def main(args: Array[String]): Unit = {
    println("Array Pratice")
    var K: Array[Int] = new Array[Int](3)
    K(0) = 2
    K(1) = 3
    K(2) = 4

    //  for (j <- 1 to 5)
    //      println(j)

    for (i <- K)
      println(i)

  }
}