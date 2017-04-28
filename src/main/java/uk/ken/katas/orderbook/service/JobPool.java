package uk.ken.katas.orderbook.service;


import static uk.ken.katas.orderbook.AppRunner.threadSize;
import static uk.ken.katas.orderbook.domain.LoadBalancer.getRecyclingResourceId;

public class JobPool {
    private final JobExecutor[] jobExecutors;

    public JobPool() {
        jobExecutors = new JobExecutor[threadSize];
        for (int i = 0; i < threadSize; i++) {
            jobExecutors[i] = new JobExecutor();
            jobExecutors[i].start();
        }
    }

    public JobExecutor getJobExecutor(long orderId) {
        return jobExecutors[getRecyclingResourceId(threadSize, orderId)];
    }
}
