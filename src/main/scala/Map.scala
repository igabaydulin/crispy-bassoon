package dev.igabaydulin.scala.concurrent.map

import java.util
import scala.collection.{AbstractIterator, StrictOptimizedIterableOps, StrictOptimizedMapOps, concurrent, mutable}

trait JMapWrapperLike[K, V, +CC[X, Y] <: mutable.MapOps[X, Y, CC, _], +C <: mutable.MapOps[K, V, CC, C]]
  extends mutable.MapOps[K, V, CC, C]
    with StrictOptimizedMapOps[K, V, CC, C]
    with StrictOptimizedIterableOps[(K, V), mutable.Iterable, C] {

  def underlying: java.util.Map[K, V]

  override def size: Int = underlying.size

  def get(k: K): Option[V] = {
    val v = underlying.get(k)
    if (v != null)
      Some(v)
    else if (underlying.containsKey(k))
      Some(null.asInstanceOf[V])
    else
      None
  }

  def addOne(kv: (K, V)): this.type = {
    underlying.put(kv._1, kv._2);
    this
  }

  def subtractOne(key: K): this.type = {
    underlying remove key;
    this
  }

  override def put(k: K, v: V): Option[V] = {
    val present = underlying.containsKey(k)
    val result = underlying.put(k, v)
    if (present) Some(result) else None
  }

  override def update(k: K, v: V): Unit = underlying.put(k, v)

  override def remove(k: K): Option[V] =
    if (underlying.containsKey(k)) Some(underlying.remove(k)) else None

  def iterator: Iterator[(K, V)] = new AbstractIterator[(K, V)] {
    val ui: util.Iterator[util.Map.Entry[K, V]] = underlying.entrySet.iterator

    def hasNext: Boolean = ui.hasNext

    def next(): (K, V) = {
      val e = ui.next();
      (e.getKey, e.getValue)
    }
  }

  override def foreachEntry[U](f: (K, V) => U): Unit = {
    val i = underlying.entrySet().iterator()
    while (i.hasNext) {
      val entry = i.next()
      f(entry.getKey, entry.getValue)
    }
  }

  override def clear(): Unit = underlying.clear()
}

abstract class AbstractJMapWrapper[K, V]
  extends mutable.AbstractMap[K, V]
    with JMapWrapperLike[K, V, mutable.Map, mutable.Map[K, V]] with Serializable

class CustomJConcurrentMapWrapper[K, V](underlying: java.util.concurrent.ConcurrentMap[K, V])
  extends AbstractJMapWrapper[K, V]
    with concurrent.Map[K, V] {

  override def get(k: K): Option[V] = Option(underlying get k)

  override def isEmpty: Boolean = underlying.isEmpty

  override def knownSize: Int = if (underlying.isEmpty) 0 else super.knownSize

  override def empty = new CustomJConcurrentMapWrapper(new java.util.concurrent.ConcurrentHashMap[K, V])

  override def updateWith(key: K)(remappingFunction: Option[V] => Option[V]): Option[V] =
    Option(underlying.compute(key, (_: K, v: V) => remappingFunction(Option(v)).getOrElse(null.asInstanceOf[V])))

  def putIfAbsent(k: K, v: V): Option[V] = Option(underlying.putIfAbsent(k, v))

  def remove(k: K, v: V): Boolean = underlying.remove(k, v)

  def replace(k: K, v: V): Option[V] = Option(underlying.replace(k, v))

  def replace(k: K, oldvalue: V, newvalue: V): Boolean =
    underlying.replace(k, oldvalue, newvalue)

  override def underlying: util.Map[K, V] = underlying
}