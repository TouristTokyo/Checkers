package ru.vsu.сs.shemenev.server;

public enum Command {
    MOVE("MOVE"),
    WIN("WIN"),
    LOSE("LOSE");

    private final String commandString;
    public static final String SEPARATOR = ":";

    Command(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
