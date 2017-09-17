package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.polymorph.PolyEvents;
import com.machinespray.ROYAL.polymorph.PolyPlayerData;

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
        PolyPlayerData.initClient();
    }

    @Override
    public void postinit() {
        super.postinit();
        PolyEvents.init();
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
