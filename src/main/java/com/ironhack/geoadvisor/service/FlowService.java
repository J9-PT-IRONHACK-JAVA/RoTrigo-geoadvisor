package com.ironhack.geoadvisor.service;

import com.ironhack.geoadvisor.dto.Location;
import com.ironhack.geoadvisor.enums.MenuOption;
import com.ironhack.geoadvisor.model.Restaurant;
import com.ironhack.geoadvisor.utils.Prints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowService {
    private final ConsoleService consoleSVC;
    private final GmapsService gmapsSVC;
    private final FavouriteService favouritesSVC;

    public void start(){
        Prints.welcome();
        consoleSVC.askContinue();

        mainMenu();
    }

    public void mainMenu() {
        String title = "What do you want to do?";
        var option = consoleSVC.askMainMenu(MenuOption.MAIN, title);
        switch (option){
            case SIMPLE_SEARCH -> simpleSearch();
            case CENTER_SEARCH -> centerSearch();
            case SEE_FAVOURITES -> showRestaurants(null);
            case EXIT -> exit();
        }
        mainMenu();
    }

    private void singleMenu(Restaurant restaurant) {
        var optionList = new ArrayList<MenuOption>();
        if (restaurant.isFavourite()) {
            optionList.add(MenuOption.REMOVE_FAVOURITE);
            optionList.add(MenuOption.UPDATE_FAVOURITE);
        } else {
            optionList.add(MenuOption.SAVE_FAVOURITE);
        }
        if (!restaurant.hasDetails()) optionList.add(MenuOption.GET_MORE_DETAILS);

        var option = consoleSVC.askSingleMenu(restaurant, optionList.toArray(MenuOption[]::new));
        if (option == null) return;
        switch (option) {
            case GET_MORE_DETAILS -> getDetails(restaurant);
            case SAVE_FAVOURITE, UPDATE_FAVOURITE -> saveFavourite(restaurant);
            case REMOVE_FAVOURITE -> removeFavourite(restaurant);
            case TO_MAIN_MENU -> mainMenu();
        }
    }

    private void removeFavourite(Restaurant restaurant) {
    }

    private void saveFavourite(Restaurant restaurant) {
        favouritesSVC.saveOrUpdate(restaurant);
    }

    private void getDetails(Restaurant restaurant) {
    }

    private void showRestaurants(List<Restaurant> restaurants) {
        String title = "Select a restaurant:";
        if (restaurants == null) {
            restaurants = favouritesSVC.getAll();
        }
        var restaurant = consoleSVC.askChooseObject(new ArrayList<>(restaurants), title);
        if (restaurant != null) {
            singleMenu((Restaurant)restaurant);
        } else {
            return;
        }
        showRestaurants(restaurants);
    }

    private void centerSearch() {
        mainMenu();
    }

    private void simpleSearch() {
        var location = askLocation();
        if (location == null) return;
        System.out.printf("""
            Address located successfully! These are the coordinates:
            \tlatitude = %s
            \tlongitude = %s
            """, location.getLatitude(), location.getLongitude());
        consoleSVC.askContinue();
        var restaurants = askRestaurants(location);
        if (restaurants == null) return;

        showRestaurants(restaurants);
    }

    private List<Restaurant> askRestaurants(Location location) {
        var radius = consoleSVC.askPositiveInt("Introduce the search radius (m):");
        if (radius == null) return null;
        var keyword = consoleSVC.ask(
                "Specify a keyword to filter your search (sushi, italian, paella, etc.)\n" +
                        "'all' or blank space = no filter");

        try {
            keyword = (keyword.equals("all")||keyword.equals(""))? null :keyword;
            var restaurants = gmapsSVC.getNearbyRestaurants(location, radius, keyword);
            if (restaurants != null && restaurants.size() > 0) return restaurants;
            System.out.println("""
                       Could not find any restaurants near that location.
                       Try again with a bigger radius or a different keyword.
                       """);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        var repeat = consoleSVC.askConfirmation("Do you want to try again?");
        if (repeat) askRestaurants(location);
        return null;
    }

    private Location askLocation(){
        var address = consoleSVC.ask("Please introduce a valid address:");
        try {
            var location = gmapsSVC.getLocation(address);
            if (location != null) return location;
            System.out.println("Could not find location... Try with being more accurate next time :)");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        var repeat = consoleSVC.askConfirmation("Do you want to try again?");
        if (repeat) askLocation();
        return null;
    }

    public void exit(){
        Prints.exitProgram();
        System.exit(0);
    }
}
