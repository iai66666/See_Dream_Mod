package top.iai.see_dream;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static top.iai.see_dream.Blocks.RegisterBlock.CONG_YU;
// 创建一个新的类ModTabs，用于存放mod创造模式tab
public class ModTabs {
    // 新建一个创造模式tab，CreativeTabs.getNextID()为获取下一个可用的tab id（未被使用的tab id），直接填入数值可能会导致模组冲突
    public static final CreativeTabs THE_DREAM = new CreativeTabs(CreativeTabs.getNextID(), "tap.See.Dream") {
        //sideonly注解表示该方法只在客户端执行，不加注解服务端运行会崩溃
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            // 设置创造模式tab的图标为cong_yu物品
            return new ItemStack(CONG_YU);
        }
    };
}
