package com.machinespray.ROYAL;

import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nullable;
import java.util.List;

public class NetHackItem extends Item implements Constants {

	public NetHackItem(String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(Main.MODID, unlocalizedName);
	}

	public static String uppercase(String s) {
		if (s.length() == 0)
			return "";
		s = s.toLowerCase();
		for (int i = 0; i < s.length(); i++)
			if (s.toCharArray()[i] == ' ')
				s = s.substring(0, i + 1) + s.toUpperCase().substring(i + 1, i + 2) + s.substring(i + 2).toLowerCase();
		return s.toUpperCase().substring(0, 1) + s.substring(1);
	}

	public void register() {
		GameData.register_impl(this);
	}

	@SideOnly(Side.CLIENT)
	public void registerClient() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return hasUse() ? EnumRarity.UNCOMMON : super.getRarity(stack);
	}

	//Methods Relating to an item's use
	public String getUse() {
		return null;
	}

	public boolean hasUse() {
		return false;
	}

	public int getID() {
		return -2;
	}

	public String type() {
		return null;
	}

	//Methods related to the client displaying item tooltips
	@SideOnly(Side.CLIENT)
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null)
			if (Main.getHandler(player) != null) {
				if (((NetHackItem) stack.getItem()).hasUse()) {
					NetHackItem item = (NetHackItem) stack.getItem();
					if (item.hasUse())
						if (Main.getHandler(player).hasKnowledge(item.getUse().replace("_", "")) || player.isCreative())
							return uppercase(item.type()) + " of " + uppercase(item.getUse().replace("_", " "));
				}
			}
		return super.getItemStackDisplayName(stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag advanced) {
		EntityPlayer playerIn = Minecraft.getMinecraft().player;
		if (playerIn != null) {
			IKnowledgeHandler kh = Main.getHandler(playerIn);
			NetHackItem nhi = ((NetHackItem) stack.getItem());
			if (nhi.hasUse())
				if (playerIn.isCreative() || kh.hasKnowledge(nhi.getUse()))
					tooltip.add(super.getItemStackDisplayName(stack));
		}
	}

}
