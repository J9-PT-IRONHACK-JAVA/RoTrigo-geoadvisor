package com.ironhack.geoadvisor.service;

import com.ironhack.geoadvisor.dto.Location;
import com.ironhack.geoadvisor.enums.MenuOption;
import com.ironhack.geoadvisor.model.Restaurant;
import com.ironhack.geoadvisor.utils.Coordinates;
import com.ironhack.geoadvisor.utils.Prints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (option == null) start();
        switch (Objects.requireNonNull(option)){
            case SIMPLE_SEARCH -> simpleSearch();
            case CENTER_SEARCH -> centerSearch();
            case SEE_FAVOURITES -> showRestaurants(null);
            case EXIT -> exit();
        }
        mainMenu();
    }

    private void singleMenu(Restaurant restaurant, Location... locations) {
        var option = consoleSVC.askSingleMenu(restaurant, getSingleMenuOptions(restaurant), locations);
        if (option == null) return;
        switch (option) {
            case GET_MORE_DETAILS -> {
                restaurant = getDetails(restaurant);
                singleMenu(restaurant);
            }
            case SAVE_FAVOURITE, UPDATE_FAVOURITE -> saveFavourite(restaurant);
            case REMOVE_FAVOURITE -> removeFavourite(restaurant);
            case TO_MAIN_MENU -> mainMenu();
        }
    }

    private static MenuOption[] getSingleMenuOptions(Restaurant restaurant) {
        var optionList = new ArrayList<MenuOption>();
        if (restaurant.isFavourite()) {
            optionList.add(MenuOption.REMOVE_FAVOURITE);
        } else {
            optionList.add(MenuOption.SAVE_FAVOURITE);
        }
        if (!restaurant.hasDetails()) {
            optionList.add(MenuOption.GET_MORE_DETAILS);
        } else {
            optionList.add(MenuOption.UPDATE_FAVOURITE);
        }
        optionList.add(MenuOption.TO_MAIN_MENU);
        return optionList.toArray(MenuOption[]::new);
    }

    private void removeFavourite(Restaurant restaurant) {
        favouritesSVC.remove(restaurant);
    }

    private void saveFavourite(Restaurant restaurant) {
        favouritesSVC.saveOrUpdate(restaurant);
    }

    private Restaurant getDetails(Restaurant restaurant) {
        try {
            restaurant = gmapsSVC.getRestaurantDetails(restaurant);
            System.out.println("Restaurant details updated!");
            return restaurant;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return restaurant;
        }
    }

    private void showRestaurants(List<Restaurant> restaurants, Location... locations) {
        String title = "Select a restaurant:";
        if (restaurants == null) {
            restaurants = favouritesSVC.getAll();
        }
        var restaurantObj = consoleSVC.askChooseObject(new ArrayList<>(restaurants), title, locations);
        if (restaurantObj != null) {
            singleMenu((Restaurant)restaurantObj, locations);
        } else {
            return;
        }
        for (Restaurant restaurant : restaurants) {
            restaurant.setFavourite(favouritesSVC.exists(restaurant));
        }
        showRestaurants(restaurants, locations);
    }

    private void centerSearch() {
        var numLocations = consoleSVC.askPositiveInt("How many locations will you provide?");
        if (numLocations == null) return;

        var locations = new ArrayList<Location>();
        for (int i = 0; i < numLocations; i++) {
            var location = askLocation("Introduce Address %s".formatted(i+1));
            locations.add(location);
        }

        var center = Coordinates.getCenter(locations.toArray(Location[]::new));
        System.out.printf("""
            Center located successfully! These are the coordinates:
            \tlatitude = %s
            \tlongitude = %s
            """, center.getLatitude(), center.getLongitude());

        getAddressFromLocation(center);
        consoleSVC.askContinue();
        var restaurants = searchRestaurants(center);
        if (restaurants == null) return;

        showRestaurants(restaurants, locations.toArray(Location[]::new));
    }

    private void getAddressFromLocation(Location center) {
        try {
            var centerAddress = gmapsSVC.getAddress(center);
            if (centerAddress == null || centerAddress.equals("")) {
                System.out.println("Couldn't get address from coordinates :(");
            } else {
                System.out.println(centerAddress);
            }
        } catch (Exception e) {
            System.err.println("Couldn't get address from coordinates :(");
        }
    }

    private void simpleSearch() {
        var location = askLocation(null);
        if (location == null) return;
        System.out.printf("""
            Address located successfully! These are the coordinates:
            \tlatitude = %s
            \tlongitude = %s
            """, location.getLatitude(), location.getLongitude());
        consoleSVC.askContinue();
        var restaurants = searchRestaurants(location);
        if (restaurants == null) return;

        showRestaurants(restaurants, location);
    }

    private List<Restaurant> searchRestaurants(Location location) {
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
        if (repeat) searchRestaurants(location);
        return null;
    }

    private Location askLocation(String title){
        if (title == null) title = "Please introduce a valid address:";
        var address = consoleSVC.ask(title);
        try {
            var location = gmapsSVC.getLocation(address);
            if (location != null) return location;
            System.out.println("Could not find location... Try with being more accurate next time :)");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        var repeat = consoleSVC.askConfirmation("Do you want to try again?");
        if (repeat) askLocation(title);
        return null;
    }

    public void exit(){
        Prints.exitProgram();
        System.exit(0);
    }
}
