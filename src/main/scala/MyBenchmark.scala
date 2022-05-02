package dev.igabaydulin.scala.concurrent.map

import MyBenchmark.{Maps, Values}

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import java.util.UUID
import java.util.UUID.randomUUID
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}
import scala.collection.concurrent
import scala.jdk.CollectionConverters._

@Threads(12)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 5)
@Warmup(iterations = 5)
class MyBenchmark {

  /** Benchmarks when key exists in map and is updated with new value */

  @Group("scala_update")
  @GroupThreads(6)
  @Benchmark
  def scala_update_with(maps: Maps, values: Values, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.scalaConcurrentHashMap.updateWith(maps.key) { _ => Some(values.newValue) })
  }

  @Group("java_update")
  @GroupThreads(6)
  @Benchmark
  def java_update_with(maps: Maps, values: Values, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.javaConcurrentHashMap.compute(maps.key, (_, _) => values.newValue))
  }

  @Group("custom_update")
  @GroupThreads(6)
  @Benchmark
  def custom_update_with(maps: Maps, values: Values, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.customConcurrentHashMap.updateWith(maps.key) { _ => Some(values.newValue) })
  }

  /** Benchmarks when we remove value from map and put it back in parallel */

  @Group("scala_remove")
  @GroupThreads(6)
  @Benchmark
  def scala_remove_with(maps: Maps, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.scalaConcurrentHashMap.updateWith(maps.key) { _ => None })
  }

  @Group("scala_remove")
  @GroupThreads(6)
  @Benchmark
  def scala_put(maps: Maps, values: Values, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.scalaConcurrentHashMap.put(maps.key, values.newValue))
  }

  @Group("java_remove")
  @GroupThreads(6)
  @Benchmark
  def java_remove_with(maps: Maps, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.javaConcurrentHashMap.compute(maps.key, (_, _) => null))
  }

  @Group("java_remove")
  @GroupThreads(6)
  @Benchmark
  def java_put(maps: Maps, values: Values, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.javaConcurrentHashMap.put(maps.key, values.newValue))
  }

  @Group("custom_remove")
  @GroupThreads(6)
  @Benchmark
  def custom_remove_with(maps: Maps, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.customConcurrentHashMap.updateWith(maps.key) { _ => None })
  }

  @Group("custom_remove")
  @GroupThreads(6)
  @Benchmark
  def custom_put(maps: Maps, values: Values, blackHole: Blackhole): Unit = {
    blackHole.consume(maps.customConcurrentHashMap.put(maps.key, values.newValue))
  }
}

object MyBenchmark {

  @State(Scope.Group)
  class Maps {

    var key: UUID = _

    var javaConcurrentHashMap: ConcurrentHashMap[UUID, UUID] = _
    var scalaConcurrentHashMap: concurrent.Map[UUID, UUID] = _
    var customConcurrentHashMap: concurrent.Map[UUID, UUID] = _

    @Setup
    def up(): Unit = {
      key = randomUUID()

      javaConcurrentHashMap = new ConcurrentHashMap[UUID, UUID]()
      scalaConcurrentHashMap = new ConcurrentHashMap[UUID, UUID]().asScala
      customConcurrentHashMap = new CustomJConcurrentMapWrapper(new ConcurrentHashMap[UUID, UUID]())

      javaConcurrentHashMap.put(key, randomUUID())
      scalaConcurrentHashMap.put(key, randomUUID())
      customConcurrentHashMap.put(key, randomUUID())
    }
  }

  @State(Scope.Thread)
  class Values {

    var newValue: UUID = _

    @Setup
    def up(): Unit = {
      newValue = randomUUID()
    }
  }
}