package top.iai.see_dream;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import top.iai.see_dream.Events.Dream;

// 注解Mod，声明模组信息
@Mod(modid = See_Dream.MODID, name = See_Dream.NAME, version = See_Dream.VERSION, useMetadata = true)
public class See_Dream {
    public static final String MODID = "see_dream";
    public static final String NAME = "梦现";
    public static final String VERSION = "0.0.1";
    public See_Dream() {
    MinecraftForge.EVENT_BUS.register(new Dream());
    }
}

