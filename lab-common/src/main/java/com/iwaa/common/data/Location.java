package com.iwaa.common.data;

import java.io.Serializable;

public class Location implements Serializable {
    private Long x; //Поле не может быть null
    private int y;
    private Integer z; //Поле не может быть null

    public Location(Long x, int y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Long getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
