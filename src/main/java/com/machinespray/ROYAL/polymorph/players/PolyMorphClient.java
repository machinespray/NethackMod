package com.machinespray.ROYAL.polymorph.players;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PolyMorphClient extends PlayerMorphClient implements IPlayerMorph {
    private EntityLivingBase entity;
    private RenderLivingBase renderer;
    private ModelBase model;

    public PolyMorphClient(IPlayerMorph playerMorph, ResourceLocation texture, EntityLivingBase entity, RenderLivingBase renderer, ModelBase model) throws Exception {
        super(playerMorph, texture);
        this.entity = entity;
        this.renderer = renderer;
        if (playerMorph.isHumanoid())
            throw new Exception("Attempt to register humanoid morph as a non-humanoid morph");
    }

    @Override
    public boolean isHumanoid() {
        return false;
    }

    @Override
    public float getWidth() {
        return entity.width;
    }

    @Override
    public float getHeight() {
        return entity.height;
    }

    @Override
    public float getEyeHeight() {
        return entity.getEyeHeight();
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public ModelBase getModel() {
        return model;
    }

    public RenderLivingBase getRenderer() {
        return renderer;
    }
}
