package org.byron4j.fuckjavathreadconcurrency.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int THREAD_COUNT = CPU_COUNT * 2;
    public static final ExecutorService executorService = new ThreadPoolExecutor(THREAD_COUNT,
                                                                            THREAD_COUNT,
                                                                            0L,
                                                                            TimeUnit.MILLISECONDS,
                                                                            new LinkedBlockingDeque<Runnable>(),
                                                                            new DefaultThreadFactory(),
                                                                            new AbortPolicy());
    static volatile boolean isLoadMade = false;

    public static synchronized void makeLoad() {
        if(isLoadMade)  {
            return;
        }

        for (int i = 0; i < THREAD_COUNT; ++i) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; ; ++i) {
                        if (i % 1000000 == 0) {
                            sleep(1);
                        }
                    }
                }
            });
        }
    }

    public static void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
        }
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "fuckjava-thread-concurrency-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }

            return t;
        }
    }


    public static class AbortPolicy implements RejectedExecutionHandler {
        /**
         * Creates an {@code AbortPolicy}.
         */
        public AbortPolicy() { }

        /**
         * Always throws RejectedExecutionException.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         * @throws RejectedExecutionException always
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }
}
