import java.util.concurrent.atomic.AtomicInteger;

public class C {
    public static void main(String[] args) {
        AtomicInteger integer = new AtomicInteger(1);
        AtomicInteger integer2 = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        if (integer.compareAndSet(1, 0)) {
                            System.out.println(Thread.currentThread().getName() + "我暂停了");
                            integer.incrementAndGet();
                            integer2.incrementAndGet();
                        } else
                            System.out.println("我不是==" + Thread.currentThread().getName());
                    }

                }
            });
            thread.start();
        }
    }
}
