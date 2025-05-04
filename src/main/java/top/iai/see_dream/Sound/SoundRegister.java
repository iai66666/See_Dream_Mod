package top.iai.see_dream.Sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import top.iai.see_dream.See_Dream;
// 声音注册
public class SoundRegister {
    // 使用 @ObjectHolder 自动注入注册名
    @GameRegistry.ObjectHolder("see_dream:jbmask")
    public static final SoundEvent JBMASK = createSound("jbmask");

    // 集中管理所有声音事件
    private static SoundEvent createSound(String name) {
        ResourceLocation location = new ResourceLocation(See_Dream.MODID, name);
        return new SoundEvent(location).setRegistryName(location);
    }

    // 处理器
    @Mod.EventBusSubscriber(modid = See_Dream.MODID)
    public static class SoundRegistryHandler {
        @SubscribeEvent
        public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    JBMASK
                    // 添加更多声音
            );
        }
    }
}
