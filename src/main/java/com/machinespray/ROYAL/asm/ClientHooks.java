package com.machinespray.ROYAL.asm;

import com.machinespray.ROYAL.Main;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class ClientHooks {
    private static Field playerInfo;
    private static Field skinType;

    static {
        try {
            playerInfo = AbstractClientPlayer.class.getDeclaredField(Main.isObf() ? "field_175157_a" : "playerInfo");
            playerInfo.setAccessible(true);
            skinType = NetworkPlayerInfo.class.getDeclaredField(Main.isObf() ? "field_178863_g" : "skinType");
            skinType.setAccessible(true);
        } catch (Exception e) {
            System.out.println("Error in obtaining information on the player's status in mod ROYAL");
            e.printStackTrace();
        }
    }

    public static Field getPlayerInfo() {
        return playerInfo;
    }

    public static Field getSkinType() {
        return skinType;
    }
}
