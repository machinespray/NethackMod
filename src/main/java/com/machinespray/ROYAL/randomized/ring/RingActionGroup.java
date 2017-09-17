package com.machinespray.ROYAL.randomized.ring;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.randomized.RandomActionGroup;
import com.machinespray.ROYAL.randomized.scroll.ScrollAction;
import com.machinespray.ROYAL.randomized.scroll.ScrollActions;

import java.util.ArrayList;

public class RingActionGroup extends RandomActionGroup<RingAction> implements Constants {

    public static RingActionGroup INSTANCE = new RingActionGroup();
    protected ArrayList<RingAction> values = new ArrayList<>();

    //public RingActionGroup(){ScrollActions.init(this);}

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
}
