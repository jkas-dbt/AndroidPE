package jkas.androidpe.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author JKas
 */
public class Logger {
    public static int warn = 0, info = 0, error = 0, success = 0;
    private static final List<LogListener> logListeners = new ArrayList<>();

    public static void initFromZero() {
        logListeners.clear();
        warn = 0;
        info = 0;
        error = 0;
        success = 0;
    }

    public static void addLogListener(LogListener listener) {
        if (!logListeners.contains(listener)) logListeners.add(Objects.requireNonNull(listener));
    }

    public static void removeLogListener(LogListener listener) {
        logListeners.remove(Objects.requireNonNull(listener));
    }

    private static void log(String src, Level level, String... msg) {
        for (final var listener : logListeners) listener.log(new LogMsg(src, level.name(), msg));
    }

    public static void warn(String src, String... messages) {
        warn++;
        log(src, Level.WARNING, messages);
    }

    public static void info(String src, String... messages) {
        info++;
        log(src, Level.INFO, messages);
    }

    public static void error(String src, String... messages) {
        error++;
        log(src, Level.ERROR, messages);
    }

    public static void success(String src, String... messages) {
        success++;
        log(src, Level.SUCCESS, messages);
    }

    public enum Level {
        WARNING,
        ERROR,
        INFO,
        SUCCESS;
    }

    public interface LogListener {
        void log(LogMsg logMsg);
    }
}
