package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/28/2016.
 */

public class State {
    public String event = null;
    public String layer = null;
    public String nextState = null;
    public String state = null;

    public State(String state, String event, String nextState, String layer) {
        this.state = state;
        this.event = event;
        this.nextState = nextState;
        this.layer = layer;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("State ").append("----------------------------").append("\n");
        sb.append("State ");
        sb.append("state : ").append(this.state).append(", ");
        sb.append("event : ").append(this.event).append(", ");
        sb.append("next_state : ").append(this.nextState).append(", ");
        sb.append("layer : ").append(this.layer);
        return sb.toString();
    }
}