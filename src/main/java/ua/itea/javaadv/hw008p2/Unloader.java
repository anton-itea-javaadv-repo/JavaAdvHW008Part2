package ua.itea.javaadv.hw008p2;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Unloader implements Runnable {
    private final Thread unloaderThread;

    private static final int PORTION = 2;
    private final Heap heapToLoad;
    private final Exchanger<Trolley> unloaderExchanger;
    private Trolley trolley;

    private boolean shouldContinue = true;

    public Unloader(Heap heapToLoad, Exchanger<Trolley> unloaderExchanger, Trolley trolley) {
        this.heapToLoad = heapToLoad;
        this.unloaderExchanger = unloaderExchanger;
        this.trolley = trolley;

        this.unloaderThread = new Thread(this);
        this.unloaderThread.start();
    }

    public void join() {
        try {
            unloaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (trolley == null || !trolley.isExhausted() || shouldContinue) {
            System.out.println("Unloader is moving");
            try {
                if (trolley != null) {
                    shouldContinue = trolley.isShouldContinue();
                    doWork();
                    System.out.print("Trolley exhausted. Unloader's heap is: "
                            + heapToLoad.getStockCount() + ".");
                }
                if (shouldContinue) {
                    if (trolley != null) {
                        System.out.println(" Unloader passes the trolley to transporter.");
                    } else {
                        System.out.println("Unloader is waiting for the trolley from transporter.");
                    }
                    trolley = unloaderExchanger.exchange(trolley);
                } else {
                    System.out.println();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Unloader finished work.");
    }

    private void doWork() {
        while (!trolley.isExhausted()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int fromTrolleyHeap = trolley.getFromTrolleyHeap(PORTION);
            System.out.print("Unloader getting " + fromTrolleyHeap + " of smth from trolley.");
            heapToLoad.putToHeap(fromTrolleyHeap);
            System.out.println(" Trolley have " + trolley.getCount() + " now. Heap have " + heapToLoad.getStockCount() + ".");
        }
    }
}
