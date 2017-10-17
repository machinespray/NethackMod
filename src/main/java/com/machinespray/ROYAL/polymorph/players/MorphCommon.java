package com.machinespray.ROYAL.polymorph.players;

public class MorphCommon implements IPlayerMorph {
    private static int sid = 1;
    private int id;
    private boolean isHuman;
    private float width;
    private float height;

    @Deprecated //Only so that PlayerMorphClient doesn't have to call super, don't use this as a constructor
    public MorphCommon() {
    }

    public MorphCommon(boolean isHuman, float width, float height) {
        this.isHuman = isHuman;
        this.width = width;
        this.height = height;
        id = sid++;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isHumanoid() {
        return isHuman;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getEyeHeight() {
        return (float) 0.9*height;
    }
}
