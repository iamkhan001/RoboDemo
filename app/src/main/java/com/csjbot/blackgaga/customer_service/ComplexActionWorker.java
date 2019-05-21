package com.csjbot.blackgaga.customer_service;


import android.support.annotation.NonNull;

import java.util.ArrayList;

class ComplexActionWorker implements IComplexActionWorker {
    private final static String TAG = "worker";

    private final ArrayList<Runnable> jobQueue = new ArrayList<>();

    private final Object WorkerLock = new Object();
    private Thread worker;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<Runnable> jobs = new ArrayList<>();

            while (true) {
                synchronized (jobQueue) {
                    jobs.addAll(jobQueue);
                    jobQueue.clear();
                }

                if (jobs.isEmpty()) {
                    break;
                }

                for (Runnable job : jobs) {
                    job.run();
                }
                jobs.clear();


                synchronized (WorkerLock) {
                    state = IDLE;

                    try {
                        WorkerLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private final static int IDLE = 0;
    private final static int WORKING = 1;

    private int state;
    private final Object StateLock = new Object();

    public ComplexActionWorker() {
        state = IDLE;
        worker = new Thread(runnable);
    }


    @Override
    public boolean pushJob(@NonNull Runnable job) {
        synchronized (StateLock) {
            if (state == WORKING) {
                return false;
            }
            state = WORKING;
        }

        synchronized (jobQueue) {
            jobQueue.add(0, job);
        }

        execute();
        return true;
    }

    void clear() {
        synchronized (jobQueue) {
            jobQueue.clear();
        }
    }

    private void execute() {

        if (worker.getState() == Thread.State.TERMINATED) {
            worker = new Thread(runnable);
        }

        if (worker.getState() == Thread.State.WAITING) {
            synchronized (WorkerLock) {
                WorkerLock.notify();
            }
        }

        if (worker.getState() == Thread.State.NEW) {
            worker.start();
        }
    }
}
