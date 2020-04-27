package service;

public class TextFormatter {

    public static String getCode(String code) {
        String escapeCode = "\u001b[0m";
        switch (code.toLowerCase()) {
            case "bold":
                escapeCode = "\u001b[1m";
                break;
            case "red":
                escapeCode = "\u001b[31m";
                break;
            case "blue":
                escapeCode = "\u001b[34m";
                break;
            case "magenta":
                escapeCode = "\u001b[35m";
                break;
            case "cyan":
                escapeCode = "\u001b[36m";
                break;
            case "yellow":
                escapeCode = "\u001b[33m";
                break;
            case "green":
                escapeCode = "\u001b[32m";
                break;
            case "reset":
                escapeCode = "\u001b[0m";
                break;
        }
        return escapeCode;
    }
}
