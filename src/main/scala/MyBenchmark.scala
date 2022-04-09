package dev.igabaydulin.scala.concurrent.map

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import java.util.UUID
import java.util.UUID.randomUUID
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}
import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.jdk.CollectionConverters._
import scala.util.Random

@Threads(Threads.MAX)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 10)
@Warmup(iterations = 10)
@State(Scope.Benchmark)
class MyBenchmark {

  private final val NUMBER_OF_TOTAL_KEYS = 1
  private final val NUMBER_OF_DUPLICATED_KEYS = 100
  private final val NUMBER_OF_UNIQUE_KEYS = 0
  private final val OPS = NUMBER_OF_DUPLICATED_KEYS + NUMBER_OF_UNIQUE_KEYS

  var keys: List[UUID] = List.empty
  var duplicatedKeys: List[(UUID, UUID)] = List.empty
  var uniqueKeys: List[(UUID, UUID)] = List.empty
  var keysToPut: List[(UUID, UUID)] = List.empty

  val javaConcurrentHashMap: ConcurrentHashMap[UUID, UUID] = new ConcurrentHashMap[UUID, UUID]()
  val scalaConcurrentHashMap: concurrent.Map[UUID, UUID] = new ConcurrentHashMap[UUID, UUID]().asScala
  val trieMap: concurrent.Map[UUID, UUID] = new TrieMap[UUID, UUID]()
  val customConcurrentHashMap: concurrent.Map[UUID, UUID] = new CustomJConcurrentMapWrapper(
    new ConcurrentHashMap[UUID, UUID]())

  @Benchmark
  @OperationsPerInvocation(OPS)
  def scala_trie_map_compute(blackHole: Blackhole): Unit = {
    keysToPut.foreach(put(scalaConcurrentHashMap, blackHole))
  }

  @Benchmark
  @OperationsPerInvocation(OPS)
  def java_concurrent_hashmap_wrapper_compute(blackHole: Blackhole): Unit = {
    keysToPut.foreach(
      entry => blackHole.consume(javaConcurrentHashMap.compute(entry._1, (_, _) => entry._2)))
  }

  @Benchmark
  @OperationsPerInvocation(OPS)
  def scala_concurrent_hashmap_wrapper_compute(blackHole: Blackhole): Unit = {
    keysToPut.foreach(put(scalaConcurrentHashMap, blackHole))
  }

  @Benchmark
  @OperationsPerInvocation(OPS)
  def custom_concurrent_hashmap_wrapper_compute(blackHole: Blackhole): Unit = {
    keysToPut.foreach(put(customConcurrentHashMap, blackHole))
  }

  private def put(map: concurrent.Map[UUID, UUID], blackHole: Blackhole): ((UUID, UUID)) => Unit =
    entry => blackHole.consume(map.updateWith(entry._1) { _: Option[UUID] => Some(entry._2) })

  @Setup
  def setUp(): Unit = {
    val random = new Random();
    keys = List.fill(NUMBER_OF_TOTAL_KEYS)(randomUUID)
    duplicatedKeys = 1.to(NUMBER_OF_DUPLICATED_KEYS)
      .map(_ => (keys(random.nextInt(NUMBER_OF_TOTAL_KEYS)), randomUUID())).toList
    uniqueKeys = List.fill(NUMBER_OF_UNIQUE_KEYS)((randomUUID(), randomUUID()))
    keysToPut = scala.util.Random.shuffle(duplicatedKeys.concat(uniqueKeys))

    keys.foreach(
      (i: UUID) => {
        scalaConcurrentHashMap.putIfAbsent(i, randomUUID())
        customConcurrentHashMap.putIfAbsent(i, randomUUID())
      }
    )
  }
}
