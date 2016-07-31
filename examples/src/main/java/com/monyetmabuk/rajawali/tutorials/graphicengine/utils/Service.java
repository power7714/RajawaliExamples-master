package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class Service {
    private String name;
    private String type;

    public Service(String name, String type) {
        setName(name);
        setType(type);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}