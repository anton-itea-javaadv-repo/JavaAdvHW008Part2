package ua.itea.javaadv.hw008p2;

import java.util.concurrent.Exchanger;

public class Main {
    public static void main(String[] args) {
        System.out.println("Creating stuff");
        Trolley trolley = new Trolley();
        Exchanger<Trolley> loaderExchanger = new Exchanger<>();
        Exchanger<Trolley> unloaderExchanger = new Exchanger<>();
        System.out.println("Creating Loader");
        Heap heap = new Heap(100);
        Loader l = new Loader(heap, loaderExchanger, trolley);
        System.out.println("Creating Unloader");
        Heap heapForUnloader = new Heap(0);
        Unloader u = new Unloader(heapForUnloader, unloaderExchanger, null);
        System.out.println("Creating Transporter");
        Transporter t = new Transporter(loaderExchanger, unloaderExchanger);
        System.out.println("Work in progress");

        l.join();
        t.join();
        u.join();

        System.out.println("Finished");
    }
}
