import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Foo foo = new Foo();

        Thread t1 = new Thread();
        Thread t2 = new Thread();
        Thread t3 = new Thread();

        CompletableFuture.runAsync(() -> {
            foo.second(t2);
        });

        CompletableFuture.runAsync(() -> {
            foo.third(t3);
        });

        CompletableFuture.runAsync(() -> {
            foo.first(t1);
        });
    }
}

class Foo {
    Semaphore sem1 = new Semaphore(0);
    Semaphore sem2 = new Semaphore(0);

    public void first(Runnable r) {
        System.out.println("first");
        sem1.release();
    }

    public void second(Runnable r) {
        try {
            sem1.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("second");
        sem2.release();
    }

    public void third(Runnable r) {
        try {
            sem2.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("third");
    }
}