# Coffee Shop Simulation
## _Multi Threaded Approach with thread pool using completable future, each task in pipeline manner with lock interface_
## _Reads/Write file with muliple instances for simulating inventory for Coffee beans and Milk_
## _Multiple machine instance locking and each machine use only a particular inventory_
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


## Full Report with 3 Customers and 1 pool
customer-2--ForkJoinPool.commonPool-worker-1::[grindCoffee|263ms|1923ms, makeEspreso|263ms|2186ms, steaminMilk|266ms|2452ms]
customer-0--ForkJoinPool.commonPool-worker-1::[grindCoffee|264ms|296ms, makeEspreso|261ms|616ms, steaminMilk|259ms|876ms]
customer-1--ForkJoinPool.commonPool-worker-1::[grindCoffee|258ms|1135ms, makeEspreso|261ms|1396ms, steaminMilk|264ms|1660ms]
