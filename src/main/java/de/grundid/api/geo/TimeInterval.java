package de.grundid.api.geo;

import java.io.Serializable;

public class TimeInterval implements Comparable<TimeInterval>, Serializable {

    private int from;
    private int to;

    public TimeInterval(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override public int compareTo(TimeInterval o) {
        int c = from - o.from;
        if (c == 0) {
            c = to - o.to;
        }
        return c;
    }
}
