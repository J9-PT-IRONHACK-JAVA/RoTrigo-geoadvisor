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
        String text1 ="""
                            
                %s   .d8888b.                    %s        d8888      888          d8b                           \s
                %s  d88P  Y88b                   %s       d88888      888          Y8P                           \s
                %s  888    888                   %s      d88P888      888                                        \s
                %s  888         .d88b.   .d88b.  %s     d88P 888  .d88888 888  888 888 .d8888b   .d88b.  888d888 \s
                %s  888  88888 d8P  Y8b d88""88b %s    d88P  888 d88" 888 888  888 888 88K      d88""88b 888P"   \s
                %s  888    888 88888888 888  888 %s   d88P   888 888  888 Y88  88P 888 "Y8888b. 888  888 888     \s
                %s  Y88b  d88P Y8b.     Y88..88P %s  d8888888888 Y88b 888  Y8bd8P  888      X88 Y88..88P 888     \s
                %s   "Y8888P88  "Y8888   "Y88P"  %s d88P     888  "Y88888   Y88P   888  88888P'  "Y88P"  888                                    
                %s  
                                                            ......                     \s
                                                       .~7?JYYYYYYJ7!^.                \s
                                                    .~?Y55555555YYJJJJJ7^.             \s
                                                   ~J5555555555YJJJJJJJJJ?~            \s
                                                 .7YYYY555555YJJJJJJJJJJJJJ7.          \s
                                                 7YYYYYYY55J!^::^!?JJJJJJJJY7          \s
                                                ^YYYYYYYYJ^        ^JJJJJJJJY^         \s
                                                7YYYYYYY?^          ~JJJJJJJY!         \s
                                                7YYYYY?!~:          !YJJJJJJY!         \s
                                                ~YYYJ!~~~~:        ~JJJJJJJJY^         \s
                                                 ?J7~~~~~~~~^::::~?YJJJJJJJY?          \s
                                                 .~~~~~~~~~~~~~!?YJJJJJJJJY?.          \s
                                                  .^!~~~~~~~~!?JJJJJJJJJJY7.           \s
                                                    :~~~~~~~7JJJJJJJJJJYJ~             \s
                                                     .^~~~7JJJJJJJJJJJJ7.              \s
                                                       .~?JJJJJJJJJJY?^                \s
                                                        .!JJJJJJJJJJ!.                 \s
                                                          :?JJJJJJ?:                   \s
                                                           .?JJJJ?.                    \s
                                                            .JJJJ:                     \s
                                                             ^JJ^                      \s
                                                              ..                       \s
                %s""".formatted(
                Colors.BLUE, Colors.WHITE,Colors.BLUE, Colors.WHITE,Colors.BLUE, Colors.WHITE,
                Colors.BLUE, Colors.GREEN,Colors.BLUE, Colors.WHITE,Colors.BLUE, Colors.WHITE,
                Colors.BLUE, Colors.WHITE,Colors.BLUE, Colors.WHITE,Colors.RED,Colors.RESET);

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
        return """
                                
                \033[1;31m    ░████░    \s\033[1;34m 
                \033[1;31m  ░████████░  \s\033[1;34m
                \033[1;31m ░████▒▒████░ \s\033[1;34m  ░░░░░░   ░░░░░░░   ░░░░░░    ░░░░░   ░░░░░░   ░░    ░░  ░░  ░░░░░░░   ░░░░░░   ░░░░░░ \s
                \033[1;31m ▒███    ███▒ \s\033[1;34m ▒▒        ▒▒       ▒▒    ▒▒  ▒▒   ▒▒  ▒▒   ▒▒  ▒▒    ▒▒  ▒▒  ▒▒       ▒▒    ▒▒  ▒▒   ▒▒\s
                \033[1;31m ░████▒▒████░ \s\033[1;34m ▒▒   ▒▒▒  ▒▒▒▒▒    ▒▒    ▒▒  ▒▒▒▒▒▒▒  ▒▒   ▒▒  ▒▒    ▒▒  ▒▒  ▒▒▒▒▒▒▒  ▒▒    ▒▒  ▒▒▒▒▒▒ \s
                \033[1;31m  ░████████░  \s\033[1;34m ▓▓    ▓▓  ▓▓       ▓▓    ▓▓  ▓▓   ▓▓  ▓▓   ▓▓   ▓▓  ▓▓   ▓▓       ▓▓  ▓▓    ▓▓  ▓▓   ▓▓\s
                \033[1;31m    ░████░    \s\033[1;34m  ██████   ███████   ██████   ██   ██  ██████     ████    ██  ███████   ██████   ██   ██\s
                \033[1;31m     ░██░     \s\033[1;34m ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                \033[1;31m      ░░      \s\033[1;34m ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
       
                """
                + Colors.WHITE_BRIGHT;
    }
    public static void exitProgram() {
        printSlow(Colors.WHITE_BRIGHT +
                """
                         .d8888b.                                                                                            \s
                        d88P  Y88b                                                                                           \s
                        Y88b.                                                                                                \s
                         "Y888b.    .d88b.   .d88b.       888  888  .d88b.  888  888      .d8888b   .d88b.   .d88b.  88888b. \s
                            "Y88b. d8P  Y8b d8P  Y8b      888  888 d88""88b 888  888      88K      d88""88b d88""88b 888 "88b\s
                              "888 88888888 88888888      888  888 888  888 888  888      "Y8888b. 888  888 888  888 888  888\s
                        Y88b  d88P Y8b.     Y8b.          Y88b 888 Y88..88P Y88b 888           X88 Y88..88P Y88..88P 888  888\s
                         "Y8888P"   "Y8888   "Y8888        "Y88888  "Y88P"   "Y88888       88888P'  "Y88P"   "Y88P"  888  888\s
                                                               888                                                           \s
                                                          Y8b d88P                                                           \s
                                                           "Y88P"
                                                                  
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
