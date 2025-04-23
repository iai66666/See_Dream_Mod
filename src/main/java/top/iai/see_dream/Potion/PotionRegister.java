package top.iai.see_dream.Potion;

import top.iai.see_dream.See_dream;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = See_Dream.MODID)
public class PotionRegister {
    //以下是注册表/列表
    public static final List<Potion> POTIONS = new ArrayList<Potion>();
    public static final Potion FIRST =new PotionSimpleBase(false,0xcccc01,"first",0);
    @SubscribeEvent
    //药水注册表
    public static void registerPotion(RegistryEvent.Register<Potion> event) {
        //由于该注册表为早期框架结构，所以需要另外添加POTIONS.add();
        POTIONS.add(FIRST);
        event.getRegistry().registerAll(POTIONS.toArray(new Potion[0]));
    }

}
