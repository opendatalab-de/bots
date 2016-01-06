package de.grundid.api.telegram;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class CommandParserTest {

    @Test
    public void itShouldParseCommand() throws Exception {
        CommandParser parser = new CommandParser("/start");
        assertEquals("start", parser.getCommand());
        assertFalse(parser.hasBotName());
        assertFalse(parser.hasParams());
    }

    @Test
    public void itShouldParseCommandWithParams() throws Exception {
        CommandParser parser = new CommandParser("/start 123 test");
        assertEquals("start", parser.getCommand());
        assertTrue(parser.hasParams());
        assertEquals("123", parser.getParams().get(0));
        assertEquals("test", parser.getParams().get(1));
    }

    @Test
    public void itShouldParseCommandWithBotName() throws Exception {
        CommandParser parser = new CommandParser("/start@testBot");
        assertEquals("start", parser.getCommand());
        assertEquals("testBot", parser.getBotName());
        assertTrue(parser.hasBotName());
        assertFalse(parser.hasParams());
    }

    @Test(expected = ParseException.class)
    public void itShouldFailParse1() throws Exception {
        new CommandParser("/");
    }

    @Test(expected = ParseException.class)
    public void itShouldFailParse2() throws Exception {
        new CommandParser("");
    }

    @Test(expected = ParseException.class)
    public void itShouldFailParse3() throws Exception {
        new CommandParser(null);
    }
}