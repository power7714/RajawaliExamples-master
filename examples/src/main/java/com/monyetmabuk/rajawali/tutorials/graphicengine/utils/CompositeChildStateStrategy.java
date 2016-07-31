package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class CompositeChildStateStrategy {
    private CompositeLayer compositeLayer = null;
    private JSONObject parameters = null;
    private State state = null;

    public CompositeChildStateStrategy(CompositeLayer layer, State state, JSONObject parameters) {
        this.compositeLayer = layer;
        this.state = state;
        this.parameters = parameters;
    }

    public void run() {
        Iterator it = this.compositeLayer.getInteractions().iterator();
        while (it.hasNext()) {
            Interaction interaction = (Interaction) it.next();
            if (interaction.getState().equalsIgnoreCase(this.state.state) && interaction.getEvent().equalsIgnoreCase(this.state.event) && interaction.getNextState().equalsIgnoreCase(this.state.nextState) && interaction.getLayer().equalsIgnoreCase(this.state.layer)) {
                interaction.run(this.compositeLayer);
            }
        }
    }
}