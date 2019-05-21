package com.csjbot.blackgaga.customer_service;

import android.support.annotation.NonNull;

import java.util.concurrent.SynchronousQueue;

class ComplexActionWorker2 implements IComplexActionWorker {
    private final SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
    private boolean isRunning = false;

    ComplexActionWorker2() {
        isRunning = true;

        Runnable consumerRunnerable = () -> {
            while (isRunning) {
                try {
                    Runnable job = queue.take();
                    if (job != null) {
                        job.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread consumerThread = new Thread(consumerRunnerable, " ComplexActionWorker");
        consumerThread.start();
    }

    void destroy() {
        isRunning = false;
    }

    @Override
    public boolean pushJob(@NonNull Runnable job) {
        return queue.offer(job);
    }
}
