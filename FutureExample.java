import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            Thread.sleep(2000);
            return "Task completed";
        });

        try {
            System.out.println("Task submitted, doing other work");
            System.out.println("Is task done? " + future.isDone());

            String res = future.get();
            System.out.println("Result: " + res);

            System.out.println("Is task done? " + future.isDone());

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }

    }
}
