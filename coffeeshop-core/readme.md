# Coffee Shop Simulation
## _Multi Threaded Approach with thread pool using completable future, each task in pipeline manner with lock interface_
## _Reads/Write file with muliple instances for simulating inventory for Coffee beans and Milk_
## _Multiple machine instance locking and each machine use only a particular inventory_
## _Order polling using an input file which can be appended with subsequent orders in realtime_
e.g. machine-1 uses inventory-0 ...


Coffee Shop Simulation is an attempt to understand performance of code and enhancing them using new features of the language and better design

## Features
- Core Java
	- Lock Interface
	- Callable Thread
	- Future
	- ExecutorService
	- CompletableFuture
	- Buffered Reader/Writer
	- PrintWriter


## Timeline with 10 customers
![Apex Chart](https://github.com/chitranjanB/CoffeeShop/blob/completablefuture_multiinstance_fileio/coffeeshop-core/chart.png)
