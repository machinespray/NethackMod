package com.machinespray.ROYAL.items;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.items.artifact.*;
import com.machinespray.ROYAL.items.potion.PotionBase;
import com.machinespray.ROYAL.items.potion.PotionEnlightenment;
import com.machinespray.ROYAL.items.potion.PotionPolymorph;
import com.machinespray.ROYAL.items.rings.ItemRing;
import com.machinespray.ROYAL.items.scrolls.ItemScroll;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class RoyalItems implements Constants {
    public static ArrayList<ItemScroll> scrolls = new ArrayList<ItemScroll>();
    public static NetHackItem base;
    public static NetHackItem runicBook;
    public static ArrayList<ItemRing> rings = new ArrayList<ItemRing>();
    public static PotionBase potionPolymorph;
    public static PotionBase potionEnlightenment;
    //public static NetHackItem AmuletUndying;

    public static void initItems() {
        ItemScroll.initNames();
        ItemRing.initNames();
        base = new NetHackItem("base");
        runicBook = new NetHackItem("runic_book");
        potionPolymorph = new PotionPolymorph();
        potionEnlightenment = new PotionEnlightenment();
        new Grayswandir();
        new FireBrand();
        new FrostBrand();
        new KingBlade();
        //AmuletUndying = new NetHackItem("amuletOfUndying");
    }

    public static void registerItems() {
        for (ItemScroll i : scrolls)
            i.register();
        for (ItemRing i : rings)
            i.register();
        base.register();
        potionPolymorph.register();
        potionEnlightenment.register();
        runicBook.register();
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
        runicBook.registerClient();
        potionPolymorph.registerClient();
        potionEnlightenment.registerClient();
        for (ArtifactBase i : ArtifactBase.artifacts)
            i.registerClient();
        //AmuletUndying.registerClient();
    }
}
