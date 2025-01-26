package top.iai.see_dream.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class dream {
    @SubscribeEvent
    public void Dream(TickEvent.PlayerTickEvent event) {
        if(Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null ){
            //Minecraft.getMinecraft().player.sendChatMessage("text");
        }
    }
}
