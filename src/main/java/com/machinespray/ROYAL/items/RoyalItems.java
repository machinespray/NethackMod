package com.machinespray.ROYAL.items;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Values;
import com.machinespray.ROYAL.items.artifact.*;
import com.machinespray.ROYAL.items.potion.PotionBase;
import com.machinespray.ROYAL.items.potion.PotionEnlightenment;
import com.machinespray.ROYAL.items.potion.PotionPolymorph;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RoyalItems implements Constants {
    public static NetHackItem base;
    public static NetHackItem runicBook;
    public static PotionBase potionPolymorph;
    public static PotionBase potionEnlightenment;
    public static NetHackItem goldDust;

    public static void initItems() {
        base = new NetHackItem("base");
        runicBook = new NetHackItem("runic_book");
        potionPolymorph = new PotionPolymorph();
        potionEnlightenment = new PotionEnlightenment();
        new Grayswandir();
        new FireBrand();
        new FrostBrand();
        new KingBlade();
        goldDust = new NetHackItem("goldDust");
    }

    public static void registerItems() {
        Values.scrollInstance.register();
        Values.ringInstance.register();
        base.register();
        potionPolymorph.register();
        potionEnlightenment.register();
        runicBook.register();
        for (ArtifactBase i : ArtifactBase.artifacts)
            i.register();
        goldDust.register();

    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        Values.scrollInstance.registerClient();
        Values.ringInstance.registerClient();
        // for (ItemScroll i : scrolls)
        //    i.registerClient();
        // for (ItemRing i : rings)
        //    i.registerClient();
        base.registerClient();
        runicBook.registerClient();
        goldDust.registerClient();
        potionPolymorph.registerClient();
        potionEnlightenment.registerClient();
        for (ArtifactBase i : ArtifactBase.artifacts)
            i.registerClient();
        //AmuletUndying.registerClient();
    }
}
