package com.machinespray.ROYAL;

public enum EnumDataType {
    RING(), SCROLL(), POTION();
    public int id;

    EnumDataType() {
        this.id = Values.sid++;
        Values.types.put(id, this);
    }

    public static EnumDataType get(int id) {
        return Values.types.get(id);
    }

    public String toString() {
        return this.name().toLowerCase().replace("_", " ");
    }
}
