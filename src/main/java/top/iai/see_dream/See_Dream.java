package top.iai.see_dream;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.command.ForgeCommand;
import top.iai.see_dream.Command.ToggleDream;
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
    @Mod.EventHandler
// 标记该方法为Minecraft Forge Mod的事件处理方法
    public static void onServerStarting(FMLServerStartingEvent event) {
    // 定义一个静态方法，当服务器启动时被调用，参数为FMLServerStartingEvent事件对象
        event.registerServerCommand(new ToggleDream());
    // 在服务器启动事件中注册一个新的服务器命令，命令实例为ToggleDream类的新对象
    }
}

