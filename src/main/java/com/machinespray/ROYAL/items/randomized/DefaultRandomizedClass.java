package com.machinespray.ROYAL.items.randomized;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.NetHackItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DefaultRandomizedClass<T> extends NetHackItem implements IRandomizedClass {
    private String type;
    private Item[] items;

    public T[] getActions() {
        return actions;
    }

    private T[] actions;
    private HashMap<Item, T> actionMap;
    private DefaultRandomizedClass parent;
    private static Random random = new Random();

    public DefaultRandomizedClass(String name, DefaultRandomizedClass parent) {
        super(name);
        this.setCreativeTab(Main.royalTab);
        this.parent = parent;
    }

    public DefaultRandomizedClass(String type, String[] names, T[] actions, RandomObjectFactory factory) {
        super(type + "Base");
        this.type = type;
        items = new Item[names.length];
        String[] newNames = initNames(names);
        for (int i = 0; i < newNames.length; i++) {
            items[i] = factory.create(newNames[i], this);
        }
        this.actions = actions;
        actionMap = new HashMap<Item, T>();
    }

    public void match(long seed) {
        random.setSeed(seed);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < actions.length; i++) {
            T I = actions[i];
            int id = random.nextInt(items.length);
            while (ids.contains(id)) {
                id = random.nextInt(items.length);
            }
            actionMap.put(items[id], I);
            ids.add(id);

        }
    }

    @Override
    public String getUse() {
        return parent.actionMap.get(this).toString().toLowerCase();
    }

    public Object getActualUse() {
        return parent.actionMap.get(this);
    }

    @Override
    public boolean hasUse() {
        return parent.actionMap.get(this) != null;
    }

    @Override
    public String type() {
        return parent.type;
    }

    public String[] initNames(String[] names) {
        return null;
    }

    @Override
    public void register() {
        for (Item i : items)
            GameRegistry.register(i);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        for (Item i : items)
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
    }

    public void put(int id, String knowledge) {
        for (T t : actions)
            if (t.toString().equals(knowledge))
                actionMap.put(items[id], t);
    }

    public int getIdFromAction(String action) {
        for (int i = 0; i < items.length; i++)
            if(actionMap.get(items[i])!=null)
            if (actionMap.get(items[i]).toString().equals(action))
                return i;
        return -1;
    }
}
