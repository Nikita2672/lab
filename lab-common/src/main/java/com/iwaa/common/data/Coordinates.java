package com.iwaa.common.data;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private float x; //Максимальное значение поля: 245
    private Float y; //Максимальное значение поля: 362, Поле не может быть null

    public Coordinates(Float y, float x) {
        this.y = y;
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + '}';
    }
}
