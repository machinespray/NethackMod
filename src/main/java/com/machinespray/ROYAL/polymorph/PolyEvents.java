package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.machinespray.ROYAL.Main.proxy;


public class PolyEvents {
    public static LayerTexture texture = new LayerTexture();

    //Zombie: 2.5 armor, burn in daylight, 1.5 * fire damage
    //Wraith no boots/leggings

    public static void init() {
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(texture);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(texture);

    }

    protected void setSize(float width, float height, Entity e) {
        if (width != e.width || height != e.height) {
            float f = e.width;
            e.width = width;
            e.height = height;

            if (e.width < f) {
                double d0 = (double) width / 2.0D;
                e.setEntityBoundingBox(new AxisAlignedBB(e.posX - d0, e.posY, e.posZ - d0, e.posX + d0, e.posY + (double) e.height, e.posZ + d0));
                return;
            }

            AxisAlignedBB axisalignedbb = e.getEntityBoundingBox();
            e.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) e.width, axisalignedbb.minY + (double) e.height, axisalignedbb.minZ + (double) e.width));

        }
    }

    @SubscribeEvent
    public void onVanillaMessesWithMe(TickEvent.PlayerTickEvent e) {
        if (PolyPlayerData.getPoly(e.player) == PolyPlayerData.SILVERFISH) {
            /*
            EntityPlayer player = e.player;
            double d0 = (double) player.width / 2.0D;
            player.height = 0.8F;
            AxisAlignedBB box = new AxisAlignedBB(player.posX - d0, player.posY, player.posZ - d0, player.posX + d0, player.posY + (double) player.height, player.posZ + d0);
            try {
                ReflectionHelper.findField(Entity.class, "av", "field_70121_D", "boundingBox").set(player, box);
            } catch (Exception er) {
                System.out.println(er.getMessage());
            }*/
            if (e.player.getArrowCountInEntity() > 0)
                e.player.setArrowCountInEntity(0);
            setSize(e.player.width, 0.6F, e.player);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderPlayerEvent.Pre e) {
        EntityPlayer p = e.getEntityPlayer();
        if (PolyPlayerData.getPoly(p) != 0) {
            p.setInvisible(true);

        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) e.getEntityLiving();
            //Fire Damage(Zombie)
            if (PolyPlayerData.getPoly(p) == PolyPlayerData.ZOMBIE || PolyPlayerData.getPoly(p) == PolyPlayerData.WRAITH) {
                if (p.inventory.armorItemInSlot(3).isEmpty())
                    if (p.world.isDaytime() && p.world.canSeeSky(new BlockPos(p.posX, p.posY + (double) p.getEyeHeight(), p.posZ)))
                        p.setFire(3);
                //Armor(Zombie)
                if (p.world.isRemote) {
                    proxy.getPlayer().getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.5D);
                } else {
                    p.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.5D);
                }
                //Lack of leggins/boots(Wraith)
                if (!p.world.isRemote) {
                    if (!p.inventory.armorInventory.get(0).isEmpty()) {
                        EntityItem item = new EntityItem(p.world);
                        item.setPosition(p.posX, p.posY, p.posZ);
                        item.setEntityItemStack(p.inventory.armorInventory.get(0).splitStack(1));
                        item.setPickupDelay(60);
                        p.world.spawnEntity(item);
                    }
                    if (!p.inventory.armorInventory.get(1).isEmpty()) {
                        EntityItem item = new EntityItem(p.world);
                        item.setPosition(p.posX, p.posY, p.posZ);
                        item.setEntityItemStack(p.inventory.armorInventory.get(1).splitStack(1));
                        item.setPickupDelay(60);
                        p.world.spawnEntity(item);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onPlayerDamage(LivingAttackEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) e.getEntityLiving();
            if (e.getSource().damageType.equals("inWall")) {
                if (!p.noClip)
                    if (PolyPlayerData.getPoly(p) != 0)
                        if (!e.getEntity().world.getBlockState(new BlockPos(Math.floor(p.posX), Math.ceil(p.height + p.posY) - 1, Math.floor(p.posZ))).causesSuffocation())
                            e.setCanceled(true);

            }
        }

    }


}
