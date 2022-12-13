package com.ironhack.geoadvisor.utils;

import java.io.IOException;

public class Prints {
    public static void printSlow(String text, int s) {
        for (String line: text.split("\\n")) {
            Tools.sleep(s);
            System.out.println(line);
        }
    }

    public static void welcome(){
        clearConsole(null);
        String text1 = Colors.YELLOW + """                                
                """;
        String text2 = """
                     
                """;
        printSlow(text1 + text2, 100);
    }

    public static void clearConsole(String title) {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {exitProgram();}

        if (title == null) return;
        System.out.println(Prints.header() + title);
    }



    public static String header(){
        return Colors.CYAN_BOLD + """
             
                """.formatted(
                Colors.RESET, Colors.CYAN_BOLD, Colors.RESET, Colors.CYAN_BOLD,
                Colors.RESET, Colors.CYAN_BOLD, Colors.YELLOW)
                + Colors.WHITE_BRIGHT;
    }
    public static void exitProgram() {
        printSlow(Colors.YELLOW +
                """
             
                                                                  
                                             """ + Colors.RESET, 100);
    }


    public static String tableHeadersToBold(String list){
        if (list == null) return null;
        String[] lines = list.split("\n");
        if (lines.length < 4) return list;
        String[] headers = lines[1].split("\\|");
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].length() == 0) continue;
            headers[i] = Colors.BLUE_BOLD_BRIGHT + Colors.WHITE_BACKGROUND + headers[i]
                    + Colors.BLACK + Colors.WHITE_BACKGROUND;
        }
        String newHeader = String.join("|", headers) + "|";
        lines[1] = newHeader;
        return String.join("\n",lines);
    }
}
