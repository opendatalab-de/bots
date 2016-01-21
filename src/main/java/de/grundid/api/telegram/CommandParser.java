package de.grundid.api.telegram;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {

    private String command;
    private String botName;
    private List<String> params = new ArrayList<>();

    public CommandParser(String message) throws ParseException {
        if (message != null && message.startsWith("/") && message.length() > 1) {
            String[] parts = message.substring(1).split(" ");
            String possibleCommand = parts[0];
            if (possibleCommand.contains("@")) {
                String[] commandParts = possibleCommand.split("@");
                command = commandParts[0];
                botName = commandParts[1];
            }
            else {
                command = possibleCommand;
            }
            params.addAll(Arrays.asList(parts).subList(1, parts.length));
        }
        else {
            throw new ParseException(message, 0);
        }
    }

    public static boolean isCommand(String s) {
        return (s != null && s.startsWith("/"));
    }

    public boolean hasBotName() {
        return StringUtils.hasText(botName);
    }

    public boolean hasParams() {
        return params != null && !params.isEmpty();
    }

    public String getBotName() {
        return botName;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getParams() {
        return params;
    }
}
