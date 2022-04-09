**REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.**

```
Benchmark                                              Mode    Cnt     Score       Error     Units
MyBenchmark.custom_concurrent_hashmap_wrapper_compute  thrpt   50  11312764,281  507019,551  ops/s
MyBenchmark.java_concurrent_hashmap_wrapper_compute    thrpt   50  10780518,563  276077,935  ops/s
MyBenchmark.scala_concurrent_hashmap_wrapper_compute   thrpt   50   7501445,275  178224,669  ops/s
MyBenchmark.scala_trie_map_compute                     thrpt   50   7493336,255  167925,781  ops/s
```

### MyBenchmark#custom_concurrent_hashmap_wrapper_compute
```
11312764,281 (99.9%) 507019,551 ops/s [Average]
(min, avg, max) = (9794246,955, 11312764,281, 12913312,645), stdev = 1024204,577
CI (99.9%): [10805744,730, 11819783,833] (assumes normal distribution)
```

### MyBenchmark#java_concurrent_hashmap_wrapper_compute
```
10780518,563 (99.9%) 276077,935 ops/s [Average]
(min, avg, max) = (9857430,832, 10780518,563, 11577912,149), stdev = 557691,086
CI (99.9%): [10504440,629, 11056596,498] (assumes normal distribution)
```  

### MyBenchmark#scala_concurrent_hashmap_wrapper_compute
```
7501445,275 (99.9%) 178224,669 ops/s [Average]
(min, avg, max) = (6904810,821, 7501445,275, 8149346,247), stdev = 360022,647
CI (99.9%): [7323220,606, 7679669,943] (assumes normal distribution)
```

### MyBenchmark#scala_trie_map_compute
```
7493336,255 (99.9%) 167925,781 ops/s [Average]
(min, avg, max) = (6927207,369, 7493336,255, 8376885,676), stdev = 339218,384
CI (99.9%): [7325410,474, 7661262,036] (assumes normal distribution)
```