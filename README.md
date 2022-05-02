## About Benchmarks
These benchmarks compare performance between Java `ConcurrentHashMap#compute` and Java `ConcurrentHashMap#compute` wrapped
into Scala `concurrent.Map#updateWith`. There's also a fix for `concurrent.Map#updateWith` to match performance of `ConcurrentHashMap#compute`

## How to Run Benchmarks
```bash
sbt jmh:run
```

## Results

**REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.**

```bash
Benchmark                                      Mode  Cnt   Score   Error   Units
MyBenchmark.custom_remove                     thrpt   25  10.415 ± 0.261  ops/us
MyBenchmark.custom_remove:custom_put          thrpt   25   5.748 ± 0.263  ops/us
MyBenchmark.custom_remove:custom_remove_with  thrpt   25   4.667 ± 0.144  ops/us
MyBenchmark.custom_update                     thrpt   25  17.016 ± 0.385  ops/us
MyBenchmark.java_remove                       thrpt   25  10.616 ± 0.222  ops/us
MyBenchmark.java_remove:java_put              thrpt   25   6.475 ± 0.180  ops/us
MyBenchmark.java_remove:java_remove_with      thrpt   25   4.141 ± 0.080  ops/us
MyBenchmark.java_update                       thrpt   25  17.801 ± 0.344  ops/us
MyBenchmark.scala_remove                      thrpt   25  64.077 ± 3.746  ops/us
MyBenchmark.scala_remove:scala_put            thrpt   25   6.394 ± 0.183  ops/us
MyBenchmark.scala_remove:scala_remove_with    thrpt   25  57.684 ± 3.812  ops/us
MyBenchmark.scala_update                      thrpt   25  12.004 ± 0.969  ops/us
```
