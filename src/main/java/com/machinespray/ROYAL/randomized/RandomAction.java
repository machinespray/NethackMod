package com.machinespray.ROYAL.randomized;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.randomized.scroll.ScrollActionGroup;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;

public class RandomAction {
    public int id;
    public String name;
    public final RandomActionGroup owner;

    public RandomAction(RandomActionGroup group,String name) {
        this.owner = group;
        this.name = name;
    }


    public String getKnowledgeName() {
        return this.name.replace("_", " ").toLowerCase();
    }

    protected void discover(Entity player) {
        IKnowledgeHandler knowledge = Main.getHandler(player);
        if (!knowledge.hasKnowledge(getKnowledgeName())) {
            if (!player.world.isRemote)
                player.sendMessage(new TextComponentString(
                        "You discover this is a " + owner.type() + " of " + getKnowledgeName() + "!"));
            knowledge.addKnowledge(getKnowledgeName());
        }
    }

}
