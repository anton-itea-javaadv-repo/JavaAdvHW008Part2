package ua.itea.javaadv.hw008p2;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Transporter implements Runnable {
    private final Thread transporterThread;

    private static final int PAUSE_SEC = 5;
    private Trolley trolley = null;
    private final Exchanger<Trolley> loaderExchanger;
    private final Exchanger<Trolley> unloaderExchanger;

    private boolean shouldContinue = true;
    private boolean gotoUnloader = false;

    public Transporter(Exchanger<Trolley> loaderExchanger, Exchanger<Trolley> unloaderExchanger) {
        this.loaderExchanger = loaderExchanger;
        this.unloaderExchanger = unloaderExchanger;

        transporterThread = new Thread(this);
        transporterThread.start();
    }

    public void join() {
        try {
            transporterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (shouldContinue || trolley != null) {
            System.out.println("Transporter is moving");
            try {
                if (!gotoUnloader) {
                    if (trolley != null) {
                        System.out.println("Transporter is giving the trolley to loader.");
                    } else {
                        System.out.println("Transporter is waiting for trolley from loader.");
                    }
                    trolley = loaderExchanger.exchange(trolley);
                } else {
                    if (trolley != null) {
                        System.out.println("Transporter is giving the trolley to unloader.");
                    } else {
                        System.out.println("Transporter is waiting for trolley from unloader.");
                    }
                    trolley = unloaderExchanger.exchange(trolley);
                }
                if (trolley != null) {
                    shouldContinue = trolley.isShouldContinue();
                    gotoUnloader = !gotoUnloader;
                    System.out.println("Transporter got trolley with [" + trolley.getCount() + "].");
                    doWork();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Transporter finished work.");
    }

    private void doWork() throws InterruptedException {
        System.out.println("Transporter is working now.");
        for (int i = 1; i <= PAUSE_SEC; i++) {
            System.out.print(".");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println();
    }
}
