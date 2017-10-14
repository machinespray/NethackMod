package com.machinespray.ROYAL.polymorph;

import com.machinespray.ROYAL.asm.AsmHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class PolyEvents {
    public static LayerTexture texture = new LayerTexture();

    //Zombie: 2.5 armor, burn in daylight, 1.5 * fire damage
    //Wraith no boots/leggings

    public static void init() {
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(texture);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(texture);

    }

    protected static void setSize(float width, float height, Entity e) {
        if (width != e.width || height != e.height) {
            float f = e.width;
            e.width = width;
            e.height = height;

            //if (e.width < f) {
            double d0 = (double) width / 2.0D;
            e.setEntityBoundingBox(new AxisAlignedBB(e.posX - d0, e.posY, e.posZ - d0, e.posX + d0, e.posY + (double) e.height, e.posZ + d0));
            return;
            //}
        }
    }

    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent e) {
        if (!Loader.isModLoaded("lilliputian"))
            updatePlayer(e.player);
    }

    public static void updatePlayer(EntityLivingBase e) {
        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;
            if (PolyPlayerData.getPolySize(player) != 0F) {
                if (player.getArrowCountInEntity() > 0)
                    player.setArrowCountInEntity(0);
                float scale = 1;
                if (Loader.isModLoaded("lilliputian"))
                    scale = (float) AsmHooks.getModdedScale(player);
                if (PolyPlayerData.getPolyEntity(player) == null) {
                    setSize(0.6F * scale, scale * 1.8F, player);
                } else {
                    setSize(PolyPlayerData.getPolyEntity(player).width * scale, PolyPlayerData.getPolyEntity(player).width * scale, player);
                }
            }
            if (PolyPlayerData.getPoly(player) == PolyPlayerData.DONKEY)
                player.stepHeight = 1.0F;
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

    /*
    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer p = Minecraft.getMinecraft().player;
        AxisAlignedBB axisalignedbb = p.getEntityBoundingBox();
        if(axisalignedbb!=null) {
            EntityPlayer entityIn = p;
            double x = 0;
            double y = 0;
            double z = 0;
            GlStateManager.depthMask(false);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            RenderGlobal.drawBoundingBox(axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z, 1.0F, 1.0F, 1.0F, 1.0F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            Vec3d vec3d = entityIn.getLook(event.getPartialTicks());
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x, y + (double) entityIn.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
            bufferbuilder.pos(x + vec3d.x * 2.0D, y + (double) entityIn.getEyeHeight() + vec3d.y * 2.0D, z + vec3d.z * 2.0D).color(0, 0, 255, 255).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
    }*/
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
                    //proxy.getPlayer().getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.5D);
                } else {
                    p.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.5D);
                }
                //Lack of leggins/boots(Wraith)
                if (!p.world.isRemote) {
                    if (!p.inventory.armorInventory.get(0).isEmpty()) {
                        EntityItem item = new EntityItem(p.world);
                        item.setPosition(p.posX, p.posY, p.posZ);
                        //item.setEntityItemStack(p.inventory.armorInventory.get(0).splitStack(1));
                        item.setPickupDelay(60);
                        p.world.spawnEntity(item);
                    }
                    if (!p.inventory.armorInventory.get(1).isEmpty()) {
                        EntityItem item = new EntityItem(p.world);
                        item.setPosition(p.posX, p.posY, p.posZ);
                        //item.setEntityItemStack(p.inventory.armorInventory.get(1).splitStack(1));
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
