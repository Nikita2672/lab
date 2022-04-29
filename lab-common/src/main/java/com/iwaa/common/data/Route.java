package com.iwaa.common.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Route implements Comparable<Route>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Location from; //Поле не может быть null
    private final Location to; //Поле не может быть null
    private final Long distance; //Поле не может быть null, Значение поля должно быть больше 1

    public Route(Long id, String name, Coordinates coordinates,
                 Date creationDate, Location from, Location to, Long distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route(String name, Coordinates coordinates,
                 Location from, Location to,
                 Long distance) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", name='" + name + '\'' + ", coordinates=" + coordinates + ", creationDate=" + creationDate + ", from=" + from + ", to=" + to + ", distance=" + distance + '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Route route) {
        Long routeValue = route.getDistance();
        Long thisValue = this.getDistance();

        if (routeValue == null) {
            routeValue = -1L;
        }

        if (routeValue - thisValue != 0) {
            if (thisValue - routeValue < 0) {
                return -1;
            } else {
                return 1;
            }
        } else {
            long result = this.getId() - route.getId();
            if (result > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(id, route.id) && Objects.equals(name, route.name) && Objects.equals(coordinates, route.coordinates) && Objects.equals(creationDate, route.creationDate) && Objects.equals(from, route.from) && Objects.equals(to, route.to) && Objects.equals(distance, route.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance);
    }
}
