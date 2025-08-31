package mission.application.service;

import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import mission.application.domain.enums.MessageConstants;
import mission.application.domain.model.dto.OrderRequest;
import mission.application.port.in.MakeOrderUseCase;
import mission.application.port.out.InputPort;
import mission.application.port.out.LogPort;
import mission.application.port.out.LoggerPort;

public class OrderService implements MakeOrderUseCase {
    private final LoggerPort logger;
    private final InputPort input;
    private final LogPort logPort;


    private final BlockingQueue<Task<?>> queue;
    private final Semaphore slots;
    private final Thread dispatcher;
    private final AtomicInteger index = new AtomicInteger(0);
    private final AtomicInteger workerSeq = new AtomicInteger(1);
    private volatile boolean closed = false;

    private static final class Task<T> {
        final Callable<T> callable;
        final CompletableFuture<T> future;
        Task(Callable<T> c, CompletableFuture<T> f) { this.callable = c; this.future = f; }
    }

    public OrderService(LoggerPort logger, InputPort input, LogPort logPort) {
        this.logger = logger;
        this.input = input;
        this.logPort = logPort;
        this.queue = new LinkedBlockingQueue<>();
        this.slots = new Semaphore(5, true);
        this.dispatcher = new Thread(this::dispatchLoop, "order-dispatcher");
        this.dispatcher.start();
    }

    private void dispatchLoop() {
        try {
            while (!closed) {
                Task<?> task = queue.take();
                slots.acquire();
                Thread t = new Thread(() -> runTask(task), "order-worker-" + workerSeq.getAndIncrement());
                t.start();
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private <T> void runTask(Task<T> task) {
        try {
            T v = task.callable.call();
            task.future.complete(v);
        } catch (Throwable e) {
            task.future.completeExceptionally(e);
        } finally {
            slots.release();
        }
    }

    public void awaitIdle() throws InterruptedException {
        while (!(queue.isEmpty() && slots.availablePermits() == 5)) {
            Thread.sleep(10);
        }
    }

    @Override
    public void makeOrder(OrderRequest orderRequest, LocalTime duration) {
        submit(() -> {
            int currentIndex = index.getAndIncrement();
            logger.print(MessageConstants.RECEIVE_ORDER);
            logger.print(MessageConstants.DELIVERY_START,
                    currentIndex, duration.getHour(), duration.getMinute());

            Thread.sleep(duration.toSecondOfDay() * 1000L / 180);

            logger.print(MessageConstants.DELIVERY_COMPLETE);
            logPort.log(orderRequest.ordererName(), orderRequest.startPlace().name(),
                    orderRequest.endPlace().name(), duration);
            return null;
        });
    }

    private <T> void submit(Callable<T> c) {
        if (closed) throw new RejectedExecutionException("OrderService is closed");
        var f = new CompletableFuture<T>();
        try { queue.put(new Task<>(c, f)); }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Interrupted while enqueue", ie);
        }
    }
}
