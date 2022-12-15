package com.ironhack.geoadvisor.service;

import com.github.freva.asciitable.AsciiTable;
import com.ironhack.geoadvisor.enums.MenuOption;
import com.ironhack.geoadvisor.model.Restaurant;
import com.ironhack.geoadvisor.utils.Colors;
import com.ironhack.geoadvisor.utils.Prints;
import com.ironhack.geoadvisor.utils.Tools;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ConsoleService {
    private final Scanner prompt = new Scanner(System.in);



    public MenuOption askMainMenu(MenuOption[] options, String title) {
        return askMenu(title, options, true);
    }

    public MenuOption askSingleMenu(Object object, MenuOption[] options) {
        String className = object.getClass().getSimpleName();
        String title = "\t%s%s MENU\n\n".formatted(Colors.YELLOW_BOLD, className.toUpperCase()) + Colors.WHITE_BRIGHT
                + "%s\n\n What do you want to do with this %s?".formatted(object.toString(), className);
        return askMenu(title, options, true);
    }


    public String askFieldValue(String field) {
        System.out.printf(" > %s:\n", field);
        return getInput();
    }

    private static String buildMenu(String title, MenuOption... options) {
        StringBuilder menu = new StringBuilder(String.format("%s\n", title));
        if (options.length > 0) {
            menu.append("\n");
            for (int i = 0; i < options.length; i++) {
                menu.append("\s\s").append(i + 1).append(") ").append(options[i].getText()).append("\n");
            }
        }
        return menu.toString();
    }

    private MenuOption askMenu(String title, MenuOption[] options, boolean clear) {
        var menu = buildMenu(title, options);
        var optionsMap = getOptionsMap(options);
        do {
            if (clear) Prints.clearConsole("");
            System.out.println(menu);
            String input = getInput();

            if (input == null) return null;
            if (optionsMap.containsKey(input)) return optionsMap.get(input);
            System.out.printf("%sCommand not recognized!\n%s", Colors.RED, Colors.WHITE_BRIGHT);
        } while (true);
    }

    public String ask(String title) {
        System.out.println(title);
        return getInput();
    }

    public Integer askPositiveInt(String title) {
        do {
            System.out.println(title);
            String input = getInput();
            if (input == null) return null;
            if (input.matches("\\d+")) {
                return Integer.parseInt(input);
            }
            System.out.printf("%sIntroduce a positive number!\n%s", Colors.RED, Colors.WHITE_BRIGHT);
        } while (true);
    }

    private static HashMap<String, MenuOption> getOptionsMap(MenuOption[] options) {
        var optionsMap = new HashMap<String, MenuOption>();
        for (int i = 0; i < options.length; i++) {
            optionsMap.put(String.valueOf(i + 1), options[i]);
        }
        return optionsMap;
    }

    private String getInput() {
        System.out.print(Colors.GREEN);
        String input = prompt.nextLine().trim();
        if (input.equals("exit")) {
            Prints.exitProgram();
            System.exit(0);
        } else if (input.equals("back")){
            return null;
        }
        System.out.print(Colors.WHITE_BRIGHT);
        return input;
    }

    public String askSearchInput() {
        String title = "Please type anything to search:";
        return ask(title);
    }

    public Object askChooseObject(List<Object> objects, String title) {
        if (objects.size() == 0) {
            System.out.print("No results found\n");
            askContinue();
            return null;
        }
        var className = objects.get(0).getClass().getSimpleName();
        title = (title != null) ? title : "Please select a %s:".formatted(className);
        return askListMenu(title, objects);
    }


    public boolean askConfirmation(String title) {
        var response = ask(title + " (type 'yes' to confirm)");
        return response.equals("yes");
    }

    public Object askListMenu(String title, List<Object> list) {
        int offset = 0;
        do {
            Prints.clearConsole("");
            var menu = buildListMenu(title, list, offset);
            System.out.println(menu);
            String input = getInput();

            if (input == null) return null;
            if (input.equals("+") && offset < list.size()-10) {
                offset += 10;
                continue;
            } else if (input.equals("-") && offset > 0) {
                offset -= 10;
                continue;
            }

            if (input.matches("export (\\w|\\d|-)+.json")) {
                exportTable(input, list);
                askContinue();
                continue;
            }
            if (input.matches("\\d+")) {
                int intInput = Integer.parseInt(input);
                if (intInput > 0 && intInput <= list.size()) {
                    return list.get(intInput - 1);
                }
            }
            System.out.printf("%sCommand not recognized!\n%s", Colors.RED, Colors.WHITE_BRIGHT);
        } while (true);
    }

    private void exportTable(String input, List<Object> table){
        try {
            String fileName = input.split("\s")[1];
            String path = Tools.exportTableToJson(table, fileName);
            System.out.printf("%sList exported successfully to '%s'\n" , Colors.GREEN, path);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String buildListMenu(String title, List<Object> list, int offset) {
        StringBuilder menu = new StringBuilder(String.format("%s\n\n", title));
        var table = Colors.BLACK + Colors.WHITE_BACKGROUND;
        if (list.size() > 0) {
            var object = list.get(0);
            if (object instanceof Restaurant) table += buildRestaurantsTable(list, offset);
        }
        menu.append(Prints.tableHeadersToBold(table));
        menu.append(Colors.RESET + "\n('back' --> go back | 'export [FILE_NAME.json]' = extract to JSON)\n");
        if (list.size() > 10) {
            int totalPages = (int)Math.ceil(list.size()/10.0);
            int currentPage = offset/10 + 1;
            if (currentPage == 1) {
                menu.append("'+' --> next page | %s/%s".formatted(currentPage, totalPages));
            } else if (currentPage == totalPages) {
                menu.append("'-' --> previous page | %s/%s".formatted(currentPage, totalPages));
            } else {
                menu.append("'+' --> next page | '-' --> previous page | %s/%s".formatted(currentPage, totalPages));
            }
        }
        return menu.toString();
    }

    public String buildRestaurantsTable(List<Object> restaurants, int offset) {
        var headersArray = new String[]{" ", "NAME", "RATING", "PRICE", "ADDRESS", " "};
        var data = new ArrayList<String[]>();
        int limit = Math.min(offset + 10, restaurants.size());
        for (int i = offset; i < limit; i++) {
            var r = (Restaurant)restaurants.get(i);
            String favourite = r.isFavourite() ? "❤️ " : " ";
            String priceLevel = "💰".repeat(r.getPriceLevel());
            data.add(new String[]{
                    String.valueOf(i+1), r.getName(), String.valueOf(r.getRating()),
                    priceLevel, r.getAddress(), favourite
            });
        }
        return AsciiTable.getTable(headersArray, data.toArray(String[][]::new));
    }



    public void printReport(String[] headers, List<List<Object>> result) {
        Prints.clearConsole("");
        if (result == null) {
            System.out.println("No results found");
        } else {
            String[][] data = result.stream().map(l -> l.stream().map(Object::toString)
                            .toArray(String[]::new)).toArray(String[][]::new);
            var report = Colors.BLACK + Colors.WHITE_BACKGROUND + AsciiTable.getTable(headers, data);
            System.out.println(Prints.tableHeadersToBold(report));
        }
        askContinue();
    }

    public void askContinue() {
        System.out.println(Colors.RESET + "Press any key... ");
        getInput();
    }


}
