package dev.igabaydulin.scala.concurrent.map

object CollectionConverters {

  implicit class ConcurrentMapHasAsScala[K, V](m: java.util.concurrent.ConcurrentMap[K, V]) extends scala.AnyRef {
    def asScala: scala.collection.concurrent.Map[K, V] = new CustomJConcurrentMapWrapper(m)
  }
}
