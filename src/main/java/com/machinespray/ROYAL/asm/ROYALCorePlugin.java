package com.machinespray.ROYAL.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

//-Dfml.coreMods.load=com.machinespray.ROYAL.asm.ROYALCorePlugin,lilliputian.core.FMLLoadingPlugin
@IFMLLoadingPlugin.Name("ROYAL Plugin")
@IFMLLoadingPlugin.TransformerExclusions("com.machinespray.ROYAL.asm")
public class ROYALCorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                "com.machinespray.ROYAL.asm.ROYALTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
