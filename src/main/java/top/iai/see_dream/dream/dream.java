package top.iai.see_dream.dream;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class dream {
    @SubscribeEvent
    public void Dream(TickEvent.PlayerTickEvent event) {
        System.out.println("1tick");
    }
}
