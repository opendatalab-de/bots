package de.grundid.api.telegram.laboeuf;

import java.util.Map;

/**
 * Created by lukas on 21.01.16.
 */
public class RandomShiaMessage {
    private final String[] shiaMessages = {
            "JUST DO IT! http://lukashimsel.me/pics/doit/2.png",
            "Make your dreams come true",
            "Yesterday you said tomorrow so JUST DO IT. NOTHING IS IMPOSSIBLE http://lukashimsel.me/pics/doit/1.png",
            "Some people dream success while you're gonna wake up and work harder. Nothing is impossible",
            "You should get to the point where anyone else would quit you're not gonna stop there.http://lukashimsel.me/pics/doit/3.png",
            "Know what you're waiting for to  DO IThttp://lukashimsel.me/pics/doit/4.png"
    };

    public String getRandomShiaMessage(){
        int index = (int) Math.round(Math.random()*shiaMessages.length);
        return shiaMessages[index];
    }

}
