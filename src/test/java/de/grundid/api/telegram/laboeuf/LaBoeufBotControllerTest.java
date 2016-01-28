package de.grundid.api.telegram.laboeuf;

import de.grundid.api.telegram.CommandParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lukas on 24.01.16.
 */
public class LaBoeufBotControllerTest {
    @Test
    public void testLaBoeuf() throws Exception {
        /* ich kenn mich nicht mit Java aus, deshalb der Test */
        List<String> text = new ArrayList<>();
        text.add("hello");
        text.add("Hello");
        text.add("hELlo");
        text.add("HELLO");
        assertTrue("HELLO".equals(text.get(2).toUpperCase()) && "hello".equals(text.get(0)));
        assertEquals(4, text.size());
    }

    @Test
    public void testArguments() throws Exception {
        CommandParser parser = new CommandParser("/are you famous?");
        List<String> params = parser.getParams();
        String command = parser.getCommand();
        assertEquals(2, params.size());
        assertEquals("ARE", command.toUpperCase());
        assertEquals("YOU", params.get(0).toUpperCase());
        assertTrue("YOU".equals(params.get(0).toUpperCase()));
        assertEquals("FAMOUS?", params.get(1).toUpperCase());
    }

    @Test
    public void testShiaInsults() throws Exception {
        CommandParser parser = new CommandParser("/shia insult FRANZ");
        List<String> params = parser.getParams();
        String command = parser.getCommand();
        assertTrue("SHIA".equals(command.toUpperCase()));
    }
}