package jkas.androidpe.logger;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author JKas
 */
public class LoggerLayoutUI {
    private LogListener logListeners;
    private static Map<String /*tag*/, LoggerLayoutUI> cachLoggerLayoutUI = new WeakHashMap<>();

    public static LoggerLayoutUI get(String tag) {
        return cachLoggerLayoutUI.get(tag.intern());
    }

    public LoggerLayoutUI() {
        logListeners = null;
    }

    public static void clear() {
        cachLoggerLayoutUI.clear();
    }

    public static void addCach(String tag) {
        cachLoggerLayoutUI.put(tag.intern(), new LoggerLayoutUI());
    }

    public void setLogListener(LogListener listener) {
        logListeners = listener;
    }

    private void log(String src, Level level, String... msg) {
        logListeners.log(new LogMsg(src, level.name(), msg));
    }

    public void w(String src, String... messages) {
        log(src, Level.WARNING, messages);
    }

    public void i(String src, String... messages) {
        log(src, Level.INFO, messages);
    }

    public void e(String src, String... messages) {
        log(src, Level.ERROR, messages);
    }

    public void s(String src, String... messages) {
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
