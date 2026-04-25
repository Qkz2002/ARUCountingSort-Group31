ARU-Counting-Sort – Implementation and Benchmark
This repository contains a Java implementation of **ARU-Counting-Sort**, a novel variant of Counting Sort proposed by Asad R. Usmani (2019). The algorithm reduces time complexity from O(n + k) to O(n + √k) and space complexity from 2n + k to 2n + 2√k by splitting each element into quotient and remainder based on m = ⌈√k⌉.
Contents
Benchmark.java – Contains ARUCountingSort, TraditionalCountingSort, data generation, and a full benchmarking harness.
benchmark_results.csv – Generated after running the benchmark; stores average sorting times for each (n, k) configuration.
Requirements
Java 
No external libraries – only standard Java SE.
Compilation
Open a terminal in the project directory and run:
bash
javac Benchmark.java
Default Experiment Parameters
Parameter	Values
Array size (n)	1000, 10 000, 100 000
Max value (k)	1000, 100 000, 10 000 000
Repetitions	10 per configuration
Warm‑up runs	3
Data distribution	Uniform random non‑negative integers
Time measurement	System.nanoTime() → milliseconds (average)
Compared Algorithms
ARUCountingSort – Our implementation of the proposed algorithm.
TraditionalCountingSort – Classical Counting Sort (re‑implemented from pseudocode).
QuickSort – Java’s Arrays.sort() (Dual‑Pivot QuickSort)
Example========== n=1000, k=1000 ==========
ARU        : 0.153 ms (based on 10 runs)
Traditional: 0.092 ms (based on 10 runs)
Quick      : 0.181 ms (based on 10 runs)

========== n=1000, k=100000 ==========
ARU        : 0.170 ms (based on 10 runs)
Traditional: 0.325 ms (based on 10 runs)
Quick      : 0.054 ms (based on 10 runs)

========== n=1000, k=10000000 ==========
ARU        : 0.053 ms (based on 10 runs)
Traditional: 9.794 ms (based on 10 runs)
Quick      : 0.047 ms (based on 10 runs)

========== n=10000, k=1000 ==========
ARU        : 0.165 ms (based on 10 runs)
Traditional: 0.026 ms (based on 10 runs)
Quick      : 0.769 ms (based on 10 runs)

========== n=10000, k=100000 ==========
ARU        : 0.182 ms (based on 10 runs)
Traditional: 0.193 ms (based on 10 runs)
Quick      : 0.511 ms (based on 10 runs)

========== n=10000, k=10000000 ==========
ARU        : 0.083 ms (based on 10 runs)
Traditional: 7.616 ms (based on 10 runs)
Quick      : 0.397 ms (based on 10 runs)

========== n=100000, k=1000 ==========
ARU        : 1.187 ms (based on 10 runs)
Traditional: 0.609 ms (based on 10 runs)
Quick      : 4.321 ms (based on 10 runs)

========== n=100000, k=100000 ==========
ARU        : 1.143 ms (based on 10 runs)
Traditional: 1.118 ms (based on 10 runs)
Quick      : 5.193 ms (based on 10 runs)

========== n=100000, k=10000000 ==========
ARU        : 1.172 ms (based on 10 runs)
Traditional: 16.894 ms (based on 10 runs)
Quick      : 5.253 ms (based on 10 runs)
