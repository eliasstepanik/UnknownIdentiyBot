package de.sailehd.support;

public class Debug {

    public static void log(String message){
        System.out.println(message);
    }

    public static void logError(String message){
        System.err.println(message);
    }

    public static void logWarning(String message){ System.out.println(TextColor.YELLOW + message + TextColor.RESET);}

    public static void clear(){
        System.out.print("\033[H\033[2J");

        System.out.flush();
    }
}
