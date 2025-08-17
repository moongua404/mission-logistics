package mission.adapter;

import api.Console;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import mission.application.domain.enums.MessageConstants;
import mission.application.domain.exception.ConcurrencyException;
import mission.application.domain.exception.InvalidInputException;
import mission.application.domain.exception.InvalidOutputException;

public class Terminal {
    private final ExecutorService worker = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "terminal-worker");
        t.setDaemon(true);
        return t;
    });

    public String getLine() {
        Future<String> response = worker.submit(this::getLineInternal);
        try {
            return response.get();
        } catch (Exception e) {
            throw new ConcurrencyException(e.getMessage());
        }
    }

    public Future<Void> print(MessageConstants messageConstants, Object... args) {
        return worker.submit(() -> {
            this.printInternal(messageConstants, args);
            return null;
        });
    }

    private String getLineInternal() {
        try {
            return Console.readLine();
        } catch (Exception ex) {
            throw new InvalidInputException(ex.getMessage());
        }
    }

    private void printInternal(MessageConstants messageConstants, Object... args) {
        try {
            System.out.printf((messageConstants.toString()) + "%n", args);
        } catch (Exception e) {
            throw new InvalidOutputException("인수 목록이 맞지 않습니다.");
        }
    }
}
