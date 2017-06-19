package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.machinespray.ROYAL.Main.proxy;

public class PolyEvents {
    private static LayerTexture zombieLayer;
    private static ResourceLocation zombieTexture;

    public static void init() {
        ModelPlayer zombie = new ModelPlayer(0.0F, false);
        zombieTexture = new
                ResourceLocation("royal", "textures/polymorph/zombie.png");
        zombie.isChild = false;
        zombieLayer = new
                LayerTexture(new RenderPlayer(Minecraft.getMinecraft().getRenderManager()), zombie, zombieTexture);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(zombieLayer);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(zombieLayer);
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderPlayerEvent.Pre e) {
        if (PolyPlayerData.zombies.contains(e.getEntityPlayer().getUniqueID().toString()))
            e.getEntityPlayer().setInvisible(true);
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) e.getEntityLiving();
            if (PolyPlayerData.zombies.contains(p.getUniqueID().toString())) {
                if (p.inventory.armorItemInSlot(3).isEmpty())
                    if(p.world.isDaytime()&&p.world.canSeeSky(new BlockPos(p.posX, p.posY + (double)p.getEyeHeight(), p.posZ)))
                    p.setFire(3);
                if(p.world.isRemote) {
                    proxy.getPlayer().getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.5D);
                }else{
                    p.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.5D);
                }
            }
        }
    }
    @SubscribeEvent
    public void onPlayerDamage(LivingHurtEvent e){
        if(e.getEntityLiving() instanceof EntityPlayer&&e.getSource().isFireDamage()){
            EntityPlayer p = (EntityPlayer) e.getEntityLiving();
        if (PolyPlayerData.zombies.contains(p.getUniqueID().toString()))
            e.setAmount(e.getAmount()*1.5F);
        }

    }


}
