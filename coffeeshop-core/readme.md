# Coffee Shop Simulation
## _Multi Threaded Approach with thread pool using future, each task in pipeline manner with lock interface_

Coffee Shop Simulation is an attempt to understand performance of code and enhancing them using new features of the language and better design

## Features
- Core Java
	- Lock Interface
	- Callable Thread
	- Future
	- ExecutorService


## Performance Report
- test_01_thread 0.825s
- test_04_threads 3.068s
- test_10_threads 7.672s
- test_20_threads 15.456s
- test_30_threads 23.104s


## Full Report
-----------------------COFFEE SHOP STARTED-----------------------------
Load:1, Max:793ms, Avg:793ms, Total:793, Samples(ms): [793]
------------------------------------------------------------------------

-----------------------COFFEE SHOP STARTED-----------------------------
Load:4, Max:781ms, Avg:772ms, Total:3091, Samples(ms): [778, 760, 772, 781]
------------------------------------------------------------------------

-----------------------COFFEE SHOP STARTED-----------------------------
Load:10, Max:796ms, Avg:769ms, Total:7693, Samples(ms): [796, 770, 772, 782, 756, 753, 757, 782, 754, 771]
------------------------------------------------------------------------

-----------------------COFFEE SHOP STARTED-----------------------------
Load:20, Max:785ms, Avg:765ms, Total:15316, Samples(ms): [770, 781, 769, 754, 784, 770, 770, 756, 759, 754, 767, 756, 755, 785, 783, 753, 771, 757, 766, 756]
------------------------------------------------------------------------

-----------------------COFFEE SHOP STARTED-----------------------------
Load:30, Max:794ms, Avg:767ms, Total:23030, Samples(ms): [794, 784, 753, 762, 757, 767, 784, 771, 769, 769, 755, 772, 782, 760, 758, 781, 782, 755, 768, 780, 756, 753, 756, 768, 767, 753, 781, 769, 770, 754]
------------------------------------------------------------------------

