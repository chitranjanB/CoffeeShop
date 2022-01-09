# Coffee Shop Simulation

## _Multi Threaded Approach with thread pool using completable future, each task in pipeline manner with lock interface_

## _Reads/Write file with muliple instances for simulating inventory for Coffee beans and Milk_

## _Multiple machine instance locking and each machine use only a particular inventory_

## _Order from console will be pushed to pipedoutput stream and sent for processing through pipedinputstream_

### Scenario:

- Runs a game loop, Place 10 orders through console, realtime processing is done using pipedstreams, it waits for machine to become available and also checks if ingredients are in stock

## Introduction

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
  - PipedStreams

## Timeline with 10 customers

![Apex Chart](https://github.com/chitranjanB/CoffeeShop/blob/completablefuture_multiinstance_fileio/coffeeshop-core/chart.png)
