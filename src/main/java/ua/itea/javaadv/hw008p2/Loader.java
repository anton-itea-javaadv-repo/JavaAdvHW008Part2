package ua.itea.javaadv.hw008p2;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Loader implements Runnable {
    private final Thread loaderThread;

    private static final int PORTION = 3;
    private final Heap heapToUnload;
    private final Exchanger<Trolley> loaderExchanger;
    private Trolley trolley;

    private boolean shouldContinue = true;

    public Loader(Heap heapToUnload, Exchanger<Trolley> loaderExchanger, Trolley trolley) {
        this.heapToUnload = heapToUnload;
        this.loaderExchanger = loaderExchanger;
        this.trolley = trolley;

        this.loaderThread = new Thread(this);
        this.loaderThread.start();
    }

    public void join() {
        try {
            loaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @java.lang.Override
    public void run() {
        while (shouldContinue) {
            System.out.println("Loader is moving");
            try {
                if (trolley != null) {
                    doWork();
                    System.out.println("Loading done. Loader's heap is: "
                            + heapToUnload.getStockCount()
                            + ". Loader passes the trolley to transporter.");
                    trolley.setShouldContinue(shouldContinue);
                } else {
                    System.out.println("Loader is waiting for the trolley from transporter.");
                }
                trolley = loaderExchanger.exchange(trolley);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Loader finished work.");
    }

    private void doWork() {
        while (shouldContinue && !trolley.isFull()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int fromHeap = heapToUnload.getFromHeap(PORTION);
            System.out.print("Loader put " + fromHeap + " of smth to trolley.");
            trolley.putToTrolleyHeap(fromHeap);
            System.out.println(" Trolley have " + trolley.getCount() + " now. Heap have " + heapToUnload.getStockCount() + ".");
            shouldContinue = !heapToUnload.isExhausted();
        }
    }
}
