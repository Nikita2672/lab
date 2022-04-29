package com.iwaa.server.collection;

import com.iwaa.common.controllers.CollectionAdmin;
import com.iwaa.common.data.Route;
import com.iwaa.common.io.CollectionFileReader;
import com.iwaa.server.config.IOConfigurator;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.StreamException;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CollectionAdminImpl implements CollectionAdmin {
    private HashSet<Route> routes;
    private ZonedDateTime creationDate;
    private IOConfigurator ioConfigurator;
    private final Set<Long> ids = new HashSet<>();

    public CollectionAdminImpl(HashSet<Route> routes) {
        this.routes = routes;
        this.creationDate = ZonedDateTime.now();
    }

    public void setIoConfigurator(IOConfigurator ioConfigurator) {
        this.ioConfigurator = ioConfigurator;
    }

    public Set<Long> getIds() {
        return ids;
    }

    public void setCreationDate() {
        creationDate = ZonedDateTime.now();
    }

    @Override
    public HashSet<Route> getCollection() {
        return routes;
    }

    @Override
    public void add(Route route) {
        if (!routes.isEmpty()) {
            Long maxId = ids.stream().reduce(Long::max).get();
            route.setId(++maxId);
            ids.add(maxId);
            routes.add(route);
        } else {
            ids.add(1L);
            route.setId(1L);
            routes.add(route);
        }
    }

    @Override
    public void addWithId(Route route) {
        ids.add(route.getId());
        routes.add(route);
    }

    @Override
    public void clear() {
        ids.clear();
        routes.clear();
    }

    @Override
    public Collection<Route> show() {
        return routes.stream().sorted((route1, route2) -> route1.getName().compareToIgnoreCase(route2.getName()))
                .collect(Collectors.toList());
    }

    public static CollectionAdminImpl configFromFile(CollectionFileReader<CollectionAdminImpl> collectionFileReader, File file) {
        try {
            CollectionAdminImpl collectionAdmin = collectionFileReader.read(file);
            for (Route route: collectionAdmin.getCollection()) {
                collectionAdmin.getIds().add(route.getId());
            }
            collectionAdmin.setCreationDate();
            if (collectionAdmin.getCollection() == null) {
                collectionAdmin.routes = new HashSet<>();
            }
            return collectionAdmin;
        } catch (ConversionException e) {
            System.out.println("Objects in file are invalid.");
        } catch (IllegalArgumentException e) {
            System.out.println((e.getMessage()));
        } catch (StreamException e) {
            System.out.println("Invalid file.");
        }
        System.out.println("You will work with empty collection via problems with file.");
        return new CollectionAdminImpl(null);
    }

    @Override
    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add("Collection type: " + routes.getClass().getName());
        info.add("Date: " + creationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        info.add("Size: " + routes.size());
        return info;
    }

    @Override
    public String outputGroupsByDistance() {
        Map<Long, Long> group = new HashMap<>();
        for (Route route : routes) {
            Long key = route.getDistance();
            if (group.containsKey(key)) {
                Long number = group.get(key) + 1;
                group.put(key, number);
            } else {
                group.put(key, 1L);
            }
        }
        if (group.isEmpty()) {
            return "Your collection is empty";
        }
        StringJoiner result = new StringJoiner("\n");
        for (Map.Entry<Long, Long> entry: group.entrySet()) {
            result.add("Number of elements with distance " + entry);
        }
        return result.toString();
    }

    @Override
    public boolean removeById(Long id) {
        ids.remove(id);
        return routes.removeIf(n -> Objects.equals(n.getId(), id));
    }

    @Override
    public boolean write() {
        return IOConfigurator.COLLECTION_FILE_WRITER.write(ioConfigurator.getOutputFile(), this);
    }

    @Override
    public String outFields() {
        StringJoiner result = new StringJoiner("\n");
        routes.stream().map(Route::getDistance).sorted(Comparator.reverseOrder()).forEach(it -> result.add(it.toString()));
        return result.toString();
    }

    @Override
    public String outFilter(Long distance) {
        StringJoiner result = new StringJoiner("\n");
        routes.stream().filter(it -> it.getDistance().compareTo(distance) < 0).forEach(it -> result.add(it.toString()));
        return result.toString();
    }
}
