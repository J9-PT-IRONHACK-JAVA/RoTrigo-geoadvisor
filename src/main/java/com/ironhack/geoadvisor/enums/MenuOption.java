package com.ironhack.geoadvisor.enums;

import com.ironhack.geoadvisor.utils.Colors;

public enum MenuOption {
    EXIT("Exit application"),
    BACK (Colors.RESET + "<< BACK" + Colors.WHITE_BRIGHT),
    TO_MAIN_MENU ("Go back to start menu"),
    CENTER_SEARCH("Search equidistant restaurants"),
    SIMPLE_SEARCH("Check restaurants around my location"),
    SEE_FAVOURITES("See favourite restaurants"),
    GET_MORE_DETAILS("Get more details"),
    REMOVE_FAVOURITE("Remove favourite"),
    UPDATE_FAVOURITE("Update restaurant info"),
    SAVE_FAVOURITE("Save as favourite");

    public static final MenuOption[] MAIN = {
            SIMPLE_SEARCH,
            CENTER_SEARCH,
            SEE_FAVOURITES,
            EXIT
    };

    private final String text;

    MenuOption(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }
}
