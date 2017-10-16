package com.machinespray.ROYAL.polymorph;

import com.google.common.base.MoreObjects;
import com.machinespray.ROYAL.asm.AsmHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;


public class PolyEvents {
    public static LayerTexture texture = new LayerTexture();
    private static HashMap<EntityPlayer, Boolean> isPollied = new HashMap<>();
    private static Method rotateAroundXAndY;
    private static Method setLightmap;
    private static Method rotateArm;
    private static Method transformSideFirstPerson;
    private static Method transformFirstPerson;
    private static Method transformEatFirstPerson;
    private static Method renderMapFirstPerson;
    private static Method renderMapFirstPersonSide;
    private static Method getMapAngleFromPitch;
    //TODO make this method detect actual invisibility (placeholder for now)
    private boolean customGetInvisible = false;

    private boolean getAndSetIsPollied(EntityPlayer p) {
        if (isPollied.get(p) == null) {
            isPollied.put(p, false);
        } else {
            if (PolyPlayerData.getPoly(p) != 0) {
                isPollied.put(p, true);
            } else {
                isPollied.put(p, false);
            }
        }
        return isPollied.get(p);
    }

    private boolean getIsPollied(EntityPlayer p) {
        return PolyPlayerData.getPoly(p) != 0;
    }

    //Zombie: 2.5 armor, burn in daylight, 1.5 * fire damage
    //Wraith no boots/leggings

    public static void init() {
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(texture);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(texture);
        try {
            //TODO add mappings
            rotateAroundXAndY = ItemRenderer.class.getDeclaredMethod("rotateArroundXAndY", float.class, float.class);
            setLightmap = ItemRenderer.class.getDeclaredMethod("setLightmap");
            rotateArm = ItemRenderer.class.getDeclaredMethod("rotateArm", float.class);
            transformSideFirstPerson = ItemRenderer.class.getDeclaredMethod("transformSideFirstPerson", EnumHandSide.class, float.class);
            transformFirstPerson = ItemRenderer.class.getDeclaredMethod("transformFirstPerson", EnumHandSide.class, float.class);
            transformEatFirstPerson = ItemRenderer.class.getDeclaredMethod("transformEatFirstPerson", float.class, EnumHandSide.class, ItemStack.class);
            renderMapFirstPersonSide = ItemRenderer.class.getDeclaredMethod("renderMapFirstPersonSide", float.class, EnumHandSide.class, float.class, ItemStack.class);
            getMapAngleFromPitch = ItemRenderer.class.getDeclaredMethod("getMapAngleFromPitch", float.class);
            renderMapFirstPerson = ItemRenderer.class.getDeclaredMethod("renderMapFirstPerson", ItemStack.class);
            rotateAroundXAndY.setAccessible(true);
            setLightmap.setAccessible(true);
            rotateArm.setAccessible(true);
            transformSideFirstPerson.setAccessible(true);
            transformFirstPerson.setAccessible(true);
            renderMapFirstPersonSide.setAccessible(true);
            getMapAngleFromPitch.setAccessible(true);
            renderMapFirstPerson.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        boolean wasPollied = getIsPollied(p);
        if (getAndSetIsPollied(p)) {
            p.setInvisible(true);
        } else if (wasPollied) {
            p.setInvisible(false);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox()) {
            EntityPlayer p = Minecraft.getMinecraft().player;
            AxisAlignedBB axisalignedbb = p.getEntityBoundingBox();
            if (axisalignedbb != null) {
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

    //Render Hand
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderHand(RenderHandEvent e) {
        if (getIsPollied(Minecraft.getMinecraft().player)) {
            e.setCanceled(true);
            if (PolyPlayerData.getPolyEntity(Minecraft.getMinecraft().player) == null)
                doRenderCustomHand(e);
        }
    }

    //Copy-Pasted/Modified vanilla code below
    private void doRenderCustomHand(RenderHandEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean flag = mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();
        EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        if (mc.gameSettings.thirdPersonView == 0 && !flag && !mc.gameSettings.hideGUI && !mc.playerController.isSpectator()) {
            renderer.enableLightmap();
            try {
                renderItemInFirstPerson(e.getPartialTicks(), renderer.itemRenderer);
            } catch (Exception exception) {
                // Fail custom hand render
                exception.printStackTrace();
            }
            renderer.disableLightmap();
        }
    }

    public void renderItemInFirstPerson(float partialTicks, ItemRenderer itemRenderer) throws InvocationTargetException, IllegalAccessException {
        AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
        float f = abstractclientplayer.getSwingProgress(partialTicks);
        EnumHand enumhand = (EnumHand) MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive()) {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW) //Forge: Data watcher can desync and cause this to NPE...
            {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }
        rotateAroundXAndY.invoke(itemRenderer, f1, f2);
        setLightmap.invoke(itemRenderer);
        rotateArm.invoke(itemRenderer, partialTicks);
        GlStateManager.enableRescaleNormal();
        if (flag) {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (itemRenderer.prevEquippedProgressMainHand + (itemRenderer.equippedProgressMainHand - itemRenderer.prevEquippedProgressMainHand) * partialTicks);
            renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, itemRenderer.itemStackMainHand, f5, itemRenderer);
        }

        if (flag1) {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            float f6 = 1.0F - (itemRenderer.prevEquippedProgressOffHand + (itemRenderer.equippedProgressOffHand - itemRenderer.prevEquippedProgressOffHand) * partialTicks);
            renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, itemRenderer.itemStackOffHand, f6, itemRenderer);
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(p_187456_2_);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(p_187456_2_ * (float) Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_187456_1_ * -0.6F, f4 + -0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(p_187456_2_ * p_187456_2_ * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = mc.player;
        //Insert Custom Texture
        mc.getTextureManager().bindTexture(PolyPlayerData.getPolyTexture(abstractclientplayer));
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer) mc.getRenderManager().<AbstractClientPlayer>getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();

        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);
        }

        GlStateManager.enableCull();
    }

    public void renderItemInFirstPerson(AbstractClientPlayer p_187457_1_, float p_187457_2_, float p_187457_3_, EnumHand p_187457_4_, float p_187457_5_, ItemStack p_187457_6_, float p_187457_7_, ItemRenderer itemRenderer) throws InvocationTargetException, IllegalAccessException {
        boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (p_187457_6_.isEmpty()) {
            if (flag && !customGetInvisible) {
                renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
            }
        } else if (p_187457_6_.getItem() instanceof net.minecraft.item.ItemMap) {
            if (flag && itemRenderer.itemStackOffHand.isEmpty()) {
                renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
            } else {
                renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
            }
        } else {
            boolean flag1 = enumhandside == EnumHandSide.RIGHT;

            if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_) {
                int j = flag1 ? 1 : -1;

                switch (p_187457_6_.getItemUseAction()) {
                    case NONE:
                        transformSideFirstPerson.invoke(itemRenderer, enumhandside, p_187457_7_);
                        break;
                    case EAT:
                    case DRINK:
                        transformEatFirstPerson.invoke(itemRenderer, p_187457_2_, enumhandside, p_187457_6_);
                        transformSideFirstPerson.invoke(itemRenderer, enumhandside, p_187457_7_);
                        break;
                    case BLOCK:
                        transformSideFirstPerson.invoke(itemRenderer, enumhandside, p_187457_7_);
                        break;
                    case BOW:
                        transformSideFirstPerson.invoke(itemRenderer, enumhandside, p_187457_7_);
                        GlStateManager.translate((float) j * -0.2785682F, 0.18344387F, 0.15731531F);
                        GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate((float) j * 35.3F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate((float) j * -9.785F, 0.0F, 0.0F, 1.0F);
                        float f5 = (float) p_187457_6_.getMaxItemUseDuration() - ((float) Minecraft.getMinecraft().player.getItemInUseCount() - p_187457_2_ + 1.0F);
                        float f6 = f5 / 20.0F;
                        f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                        if (f6 > 1.0F) {
                            f6 = 1.0F;
                        }

                        if (f6 > 0.1F) {
                            float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                            float f3 = f6 - 0.1F;
                            float f4 = f7 * f3;
                            GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                        }

                        GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                        GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                        GlStateManager.rotate((float) j * 45.0F, 0.0F, -1.0F, 0.0F);
                }
            } else {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * (float) Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * ((float) Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(p_187457_5_ * (float) Math.PI);
                int i = flag1 ? 1 : -1;
                GlStateManager.translate((float) i * f, f1, f2);
                transformSideFirstPerson.invoke(itemRenderer, enumhandside, p_187457_7_);
                transformFirstPerson.invoke(itemRenderer, enumhandside, p_187457_5_);
            }

            itemRenderer.renderItemSide(p_187457_1_, p_187457_6_, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }

        GlStateManager.popMatrix();
    }

    private void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_) throws InvocationTargetException, IllegalAccessException {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        float f = MathHelper.sqrt(p_187463_3_);
        float f1 = -0.2F * MathHelper.sin(p_187463_3_ * (float) Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
        GlStateManager.translate(0.0F, -f1 / 2.0F, f2);
        float f3 = (float) getMapAngleFromPitch.invoke(itemRenderer, p_187463_1_);
        GlStateManager.translate(0.0F, 0.04F + p_187463_2_ * -1.2F + f3 * -0.5F, -0.72F);
        GlStateManager.rotate(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
        this.renderArms();
        float f4 = MathHelper.sin(f * (float) Math.PI);
        GlStateManager.rotate(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        renderMapFirstPerson.invoke(itemRenderer, itemRenderer.itemStackMainHand);
    }

    private void renderArms() throws InvocationTargetException, IllegalAccessException {
        if (!customGetInvisible) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            renderArm(EnumHandSide.RIGHT);
            renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(PolyPlayerData.getPolyTexture(mc.player));
        Render<AbstractClientPlayer> render = mc.getRenderManager().<AbstractClientPlayer>getEntityRenderObject(mc.player);
        RenderPlayer renderplayer = (RenderPlayer) render;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);

        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm(mc.player);
        } else {
            renderplayer.renderLeftArm(mc.player);
        }

        GlStateManager.popMatrix();
    }

    private void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide p_187465_2_, float p_187465_3_, ItemStack p_187465_4_) throws InvocationTargetException, IllegalAccessException {
        float f = p_187465_2_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.translate(f * 0.125F, -0.125F, 0.0F);

        if (!customGetInvisible) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0F, 0.0F, 0.0F, 1.0F);
            renderArmFirstPerson(p_187465_1_, p_187465_3_, p_187465_2_);
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51F, -0.08F + p_187465_1_ * -1.2F, -0.75F);
        float f1 = MathHelper.sqrt(p_187465_3_);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(p_187465_3_ * (float) Math.PI);
        GlStateManager.translate(f * f3, f4 - 0.3F * f2, f5);
        GlStateManager.rotate(f2 * -45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * f2 * -30.0F, 0.0F, 1.0F, 0.0F);
        renderMapFirstPerson.invoke(Minecraft.getMinecraft().getItemRenderer(), p_187465_4_);
        GlStateManager.popMatrix();
    }

}
