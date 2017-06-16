package com.machinespray.ROYAL.sync;

enum EnumDataType {
    RING(0), SCROLL(1), POTION(2);
    final int id;

    EnumDataType(int id) {
        this.id = id;
    }

    static EnumDataType get(int id) {
        switch (id) {
            case 0:
                return RING;
            case 1:
                return SCROLL;
            case 2:
                return POTION;
        }
        return null;
    }
}
