## Example of stock-exchange trading that reads a stream of orders, creates OrderBooks for each different instrument and then and creates real time view which could be ordered 'by level' and 'by order'.

### How To Run

```
mvn install
java -jar target/orders-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/orders2.xml
```

you can also provide alternatively number of threads to use to load balance the incoming traffic and timeout in milliseconds by:
```java -jar target/orders-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/orders2.xml number_of_threads timeout_in_ms```

for example running:
```
java -jar target/orders-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/orders2.xml 10 10
java -jar target/orders-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/orders2.xml 1 10
```
This example above shows if there was some additional call(say to get price info from 3rd party or something) which would wait just 10 ms
the result of concurrent and single threaded approach would be significantly different.

Fill free to play with orders file to see what happens(make sure sequence of orders is correct, EDIT/REMOVE  should follow ADD for same order id)

Main integration test AppRunnerIntegrationTest which does the same with given test orders file.

The main functionality is covered by tests but many other things are skipped like,
validation/testing of variety of cases during parsing as many things can go wrong but
this is a test so we assume order file is correctly built.

We also assume that sequence of orders are correct, although we have a simple check to make sure we don't add twice
same orders id, or we are not trying to delete something that doesn't exist, but this is just to make sure
our example order files are correct and if not we simply throw an exception.

Code implemented in concurrent way, although no microbenchmarks or performance tests run but this is just to show
some concurrency skills for the sake of the very test, and as data structure lock is highly contended the concurrent
 approach might not be the best to solve performance issue, or at least in the way it is done.

Also no GC/JVM statistics are taken to check the number of short/long objects, ideally this could be done to reduce
the number of redundant objects created.

### Architecture

Some notes on architecture of the app. It consist of mainly 3 parts: Parser, Queue distribution, Job executor.
Parser is parsing and adding all to the main queue, as soon as it starts, it notifies queue distributor to start distributing
orders to different threads by usage of load balancing.
Orders by same id should be executed by same job executor thread to guarantee sequential processing
 by same order id but concurrent processing by different id.
Once parser finishes it's job it notifies distributor so  distributor stops pooling the queue, same happens with job executors,
once they are all done, they notify to print the report.

Report is based on 'by orders' and 'by levels' structures, 'by orders' is using sorting by price/size/order id and
'by levels' is sorted by price, this is as of requirements.


