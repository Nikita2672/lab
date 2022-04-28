package com.iwaa.common.controllers;

import com.iwaa.common.data.Route;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public interface CollectionAdmin {
    HashSet<Route> getCollection();

    void add(Route route);
    void clear();
    Collection<Route> show();
    List<String> getInfo();
    String outputGroupsByDistance();
    boolean removeById(Long id);
    boolean write();
    String outFields();
    String outFilter(Long distance);
    void addWithId(Route route);
}
