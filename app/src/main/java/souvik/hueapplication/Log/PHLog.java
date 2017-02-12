package souvik.hueapplication.Log;

/**
 * Created by Souvik Das on 10-Feb-17.
 */

public class PHLog {

    private static boolean info = false;
    private static boolean debug = false;
    private static boolean warn = false;
    private static boolean error = false;

    private static LogLevel sdkLogLevel;

    public enum LogLevel {
        SUPPRESS,
        INFO,
        ERROR,
        WARN,
        DEBUG
    }

    public static void setSdkLogLevel(LogLevel logLevel) {
        sdkLogLevel = logLevel;
        resetLogLevel();

        switch (logLevel) {
            case DEBUG :
                debug = true;
            case WARN :
                warn = true;
            case ERROR :
                error = true;
            case INFO :
                info = true;
        }
    }

    public static void resetLogLevel() {

        info = debug = warn = error = false;
    }

    public static void i(String TAG, String message) {
        if(info) {
            System.out.println(TAG + " : " + message);
        }
    }

    public static void d(String TAG, String message) {
        if(isLoggable() && debug) {
            System.out.println(TAG + " : " + message);
        }
    }

    public static void w(String TAG, String message) {
        if(isLoggable() && warn) {
            System.out.println(TAG + " : " + message);
        }
    }

    public static void e(String TAG, String message) {
        if(isLoggable() && error) {
            System.err.println(TAG + " : " + message);
        }
    }

    public static boolean isLoggable() {
        if(sdkLogLevel != LogLevel.SUPPRESS)
            return true;
        return false;
    }
}
