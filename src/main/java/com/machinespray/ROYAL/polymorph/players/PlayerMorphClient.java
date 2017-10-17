package com.machinespray.ROYAL.polymorph.players;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerMorphClient implements IPlayerMorph {
    private int id;
    private ResourceLocation texture;

    public PlayerMorphClient(IPlayerMorph playerMorph, ResourceLocation texture) throws Exception {
        id = playerMorph.getId();
        this.texture = texture;
        if (!playerMorph.isHumanoid())
            throw new Exception("Attempt to register non-humanoid morph as a humanoid morph");

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isHumanoid() {
        return true;
    }

    @Override
    public float getWidth() {
        return 0.6F;
    }

    @Override
    public float getHeight() {
        return 1.8F;
    }
    @Override
    public float getEyeHeight() {
        return 1.62F;
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
