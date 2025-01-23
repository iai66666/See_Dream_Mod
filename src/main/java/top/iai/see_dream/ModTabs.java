package top.iai.see_dream;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static top.iai.see_dream.Blocks.RegisterBlock.CONG_YU;

public class ModTabs {
    public static final CreativeTabs THE_DREAM = new CreativeTabs(CreativeTabs.getNextID(), "tap.See.Dream") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(CONG_YU);
        }
    };
}
