package com.machinespray.ROYAL.randomized.scroll;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.randomized.RandomActionGroup;

import java.util.ArrayList;

public class ScrollActionGroup extends RandomActionGroup<ScrollAction> implements Constants {

    public static ScrollActionGroup INSTANCE = new ScrollActionGroup();
    protected ArrayList<ScrollAction> values = new ArrayList<>();

    public ScrollActionGroup(){
        ScrollActions.init(this);
    }

    @Override
    public ArrayList<ScrollAction> values() {
        return values;
    }

    @Override
    public String type() {
        return "scroll";
    }

    @Override
    public String[] nameProvider() {
        return scrollNames;
    }

    @Override
    public int id() {
        return 1;
    }
}
