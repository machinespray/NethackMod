package com.machinespray.ROYAL.randomized.ring;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.randomized.RandomActionGroup;

import java.util.ArrayList;

public class RingActionGroup extends RandomActionGroup<RingAction> implements Constants {

    public static RingActionGroup INSTANCE = new RingActionGroup();
    protected ArrayList<RingAction> values = new ArrayList<>();

    public RingActionGroup(){RingActions.init(this);}

    @Override
    public ArrayList<RingAction> values() {
        return values;
    }

    @Override
    public String type() {
        return "ring";
    }

    @Override
    public String[] nameProvider() {
        return ringNames;
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public RingAction getAction(String name) {
        name = name.substring(0, name.length() - 1);
        return super.getAction(name);
    }
}
