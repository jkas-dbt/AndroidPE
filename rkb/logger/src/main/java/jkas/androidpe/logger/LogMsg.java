package jkas.androidpe.logger;

import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class LogMsg {
    public String src, level, message = "";
    public int resColor = R.color.info;

    public LogMsg(String src, String level, String... listMsg) {
        this.src = src;
        this.level = level;
        for (String msg : listMsg) this.message += msg + "\n";

        if (level.equals(Logger.Level.ERROR.name())) resColor = R.color.error;
        else if (level.equals(Logger.Level.INFO.name())) resColor = R.color.info;
        else if (level.equals(Logger.Level.SUCCESS.name())) resColor = R.color.success;
        else if (level.equals(Logger.Level.WARNING.name())) resColor = R.color.warning;
    }
}
