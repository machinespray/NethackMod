package com.machinespray.ROYAL.items;

public class UnicornHorn extends NetHackItem {
    public UnicornHorn() {
        super("unicornHorn");
    }

    @Override
    public String type() {
        return Type.UNICORN_HORN.toString();
    }
}
