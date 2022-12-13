package com.ironhack.geoadvisor.enums;

import com.ironhack.geoadvisor.utils.Colors;

public enum MenuOption {
    EXIT("Exit"),
    BACK (Colors.RESET + "<< BACK" + Colors.WHITE_BRIGHT),
    TO_MAIN_MENU ("Go back to start menu");

    public static final MenuOption[] MAIN = {
            TO_MAIN_MENU,
            BACK,
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
