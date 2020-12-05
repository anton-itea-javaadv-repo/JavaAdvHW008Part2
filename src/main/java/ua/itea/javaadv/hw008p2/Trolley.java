package ua.itea.javaadv.hw008p2;

public class Trolley {
    private final Heap trolleyHeap;
    private boolean shouldContinue = true;

    public Trolley() {
        this.trolleyHeap = new Heap(0);
    }

    public Trolley(int trolleyHeap) {
        this.trolleyHeap = new Heap(trolleyHeap);
    }

    public int getCount() {
        return trolleyHeap.getStockCount();
    }

    public boolean isExhausted() {
        return trolleyHeap.isExhausted();
    }

    public boolean isFull() {
        return trolleyHeap.getStockCount() == 6;
    }

    public int getFromTrolleyHeap(int portion) {
        return this.trolleyHeap.getFromHeap(portion);
    }

    public void putToTrolleyHeap(int portion) {
        this.trolleyHeap.putToHeap(portion);
    }

    public boolean isShouldContinue() {
        return shouldContinue;
    }

    public void setShouldContinue(boolean shouldContinue) {
        this.shouldContinue = shouldContinue;
    }
}
