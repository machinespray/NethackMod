package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.items.NetHackItem;

public class ClientProxy extends CommonProxy {
    @Override
    public void preinit() {
        super.preinit();
        //RoyalItems.registerClient();
        //RoyalBlocks.registerClient();
    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public void postinit() {
        super.postinit();
    }

    @Override
    public String getUse(NetHackItem item) {
        try {
            if (item.getID() != -2)
                if (item.actionGroup != null)
                    return item.actionGroup.getAction(item.getUnlocalizedName()).getKnowledgeName();
        } catch (Exception e) {
        }
        return null;
    }

}
