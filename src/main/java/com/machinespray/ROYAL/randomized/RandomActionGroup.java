package com.machinespray.ROYAL.randomized;

import java.util.ArrayList;
import java.util.Random;

public abstract class RandomActionGroup<T extends RandomAction> {

    public abstract ArrayList<T> values();

    public abstract String type();

    public abstract String[] nameProvider();

    public abstract int id();

    private static Random random = new Random();
    private static ArrayList<Integer> ids = new ArrayList<Integer>();

    public void match(long seed) {
        ids.clear();
        random.setSeed(seed);
        for (int i = 0; i < values().size(); i++) {
            RandomAction I = values().get(i);
            int id = random.nextInt(nameProvider().length);
            while (ids.contains(id)) {
                id = random.nextInt(nameProvider().length);
            }
            I.id = id;
            ids.add(id);
        }
    }

    public T getAction(String name) {
        int id = -1;
        name = name.split("\\.")[1].replace("_", " ");
        for (int i = 0; i < nameProvider().length; i++) {
            if (nameProvider()[i].equals(name))
                id = i;
        }
        for (int i = 0; i < values().size(); i++) {
            T I = values().get(i);
            if (id == I.id) {
                return I;
            }
        }
        return null;
    }

}
