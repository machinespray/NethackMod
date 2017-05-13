package com.machinespray.ROYAL.items;

import java.util.ArrayList;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.items.artifact.ArtifactBase;
import com.machinespray.ROYAL.items.artifact.FireBrand;
import com.machinespray.ROYAL.items.artifact.FrostBrand;
import com.machinespray.ROYAL.items.artifact.Grayswandir;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.machinespray.ROYAL.items.rings.ItemRing;
import com.machinespray.ROYAL.items.scrolls.ItemScroll;

public class RoyalItems implements Constants {
    public static ArrayList<ItemScroll> scrolls = new ArrayList<ItemScroll>();
    public static NetHackItem base;
    public static ArrayList<ItemRing> rings = new ArrayList<ItemRing>();
    public static NetHackItem unicornHorn;
    //public static NetHackItem AmuletUndying;

    public static void initItems() {
        ItemScroll.initNames();
        ItemRing.initNames();
        base = new NetHackItem("base");
        unicornHorn = new UnicornHorn();
        new Grayswandir();
        new FireBrand();
        new FrostBrand();
        //AmuletUndying = new NetHackItem("amuletOfUndying");
    }

    public static void registerItems() {
        for (ItemScroll i : scrolls)
            i.register();
        for (ItemRing i : rings)
            i.register();
        base.register();
        unicornHorn.register();
        for (ArtifactBase i : ArtifactBase.artifacts)
            i.register();
        //AmuletUndying.register();

    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        for (ItemScroll i : scrolls)
            i.registerClient();
        for (ItemRing i : rings)
            i.registerClient();
        base.registerClient();
        unicornHorn.registerClient();
        for (ArtifactBase i : ArtifactBase.artifacts)
            i.registerClient();
        //AmuletUndying.registerClient();
    }
}
