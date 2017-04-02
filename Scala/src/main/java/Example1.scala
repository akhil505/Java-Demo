
object Example1 {

  def main(args: Array[String]) {
    println("Hello");
    val li = 1 to 6 toList
    val add1 = (x: Int) => x + 1
    val sq = (y: Int) => y * y

    val re = li map add1
    val sqr = li map sq
    val fn = li map add1 map sq
    val ss = sq compose add1
    val nn = li map ss
    println(nn)
    /* println(ss)*/
    println(fn)
    println(sqr)
    println(re)
    println(li)

    def timesTwo(i: Int): Int = {
      println("hello world")
      i * 2
    }
    println(timesTwo(2))
  }

}