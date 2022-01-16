# Coffee Shop Simulation

## _Multi Threaded Approach with thread pool using completable future, each task in pipeline manner with lock interface_

## _Reads/Write file with muliple instances for simulating inventory for Coffee beans and Milk_

## _Multiple machine instance locking and each machine use only a particular inventory_

## _Order from console will be pushed to pipedoutput stream and sent for processing through pipedinputstream_

## Introduction

Coffee Shop Simulation is an attempt to understand performance of code and enhancing them using new features of the language and better design

### Scenario:

- Runs a game loop, Place 10(N) orders through console, realtime processing is done using pipedstreams, it waits for machine to become available and also checks if ingredients are in stock, It also keeps track of customer and machines used to make coffee. It then creates a zip file to simulate the end product with customer and machine details

## Features

- Core Java
  - Lock Interface
  - Callable Thread
  - Future
  - ExecutorService
  - CompletableFuture
  - Buffered Reader/Writer
  - PrintWriter
  - PipedStreams
  - ZipStream

## Architecture of implementation

![Architecture](https://github.com/chitranjanB/CoffeeShop/blob/completablefuture_multiinstance_pipedstream/coffeeshop-core/assets/CoffeeShop_Architecture.drawio.png)

## Timeline with 10 customers

![Apex Chart](https://github.com/chitranjanB/CoffeeShop/blob/completablefuture_multiinstance_pipedstream/coffeeshop-core/assets/chart.png)
