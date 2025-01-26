package top.iai.see_dream.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import top.iai.see_dream.See_Dream;


@Mod.EventBusSubscriber(modid = See_Dream.MODID)
public class RegisterEntity {
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event){
        //生物名称，所编写的生物类，名称，追踪范围，生物蛋颜色1，颜色2
        //registerEntity("dream_dream", DreamF.class, 1000, 20, 14833957, 0);
    }
    public static void registerEntity(String name, Class<? extends Entity> entity, int id, int range,int color1,int color2) {

        EntityRegistry.registerModEntity(new ResourceLocation( See_Dream.MODID + ":" + name), entity, name, id, See_Dream.MODID, range, 1, true, color1, color2);

    }
}

