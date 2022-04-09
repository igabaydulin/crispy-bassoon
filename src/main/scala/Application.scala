package dev.igabaydulin.scala.concurrent.map

import java.util.concurrent.ConcurrentHashMap
import scala.collection.concurrent

object Application extends App {
  {
    import scala.jdk.CollectionConverters._
    val map: concurrent.Map[String, Int] = new ConcurrentHashMap[String, Int]().asScala
  }

  {
    import CollectionConverters._
    val map: concurrent.Map[String, Int] = new ConcurrentHashMap[String, Int]().asScala
    println(map.updateWith("test") {v => v.map(_ + 1)})
  }
}
