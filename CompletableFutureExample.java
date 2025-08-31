import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                 Thread.currentThread().interrupt();
            }
            return "Result from CompletableFuture";
        })
                .thenAccept(result ->  {
                    System.out.println("Completed " + result);
                    System.out.println("Processing after result");
                });

        System.out.println("Main thread is free to do other tasks");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("************************************");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    System.out.println("supplyAsync running in: " + Thread.currentThread().getName());
                    return "Hello";
                })
                .thenApply(s -> {
                    // thenApply runs in the same thread as supplyAsync if available.
                    System.out.println("thenApply running in: " + Thread.currentThread().getName());
                    return s + " World";
                })
                .thenApplyAsync(s -> {
                    // thenApplyAsync uses a different thread from the default ForkJoinPool.
                    System.out.println("thenApplyAsync running in: " + Thread.currentThread().getName());
                    return s + "! CompletableFuture is awesome.";
                });

        System.out.println("Final result: " + future.join());

        System.out.println("********************************");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return "Future 1";
            } catch (InterruptedException e) {
                return "Interrupted";
            }
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                return "Future 2";
            } catch (InterruptedException e) {
                return "Interrupted";
            }
        });

        // Combine results of two futures
        CompletableFuture<String> combined = future1.thenCombine(future2,
                (result1, result2) -> result1 + " + " + result2);
        System.out.println("Combined result: " + combined.join());

        // Wait for all futures to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2);
        allOf.thenRun(() -> System.out.println("Both futures completed!"));

        // Wait for any one future to complete
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2);
        System.out.println("First completed: " + anyOf.join());
    }
}
