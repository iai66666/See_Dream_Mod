package top.iai.see_dream.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.common.util.EnumHelper;
import top.iai.see_dream.RegisterUtil;

public class ItemPi extends ItemPickaxe {
    // 定义一个名为PIX的工具材料，具有较高的挖掘等级和无限耐久度(钻石镐pro max)
    public static final Item.ToolMaterial PIX = EnumHelper.addToolMaterial("PIX",
            3, 0, (float) Integer.MAX_VALUE, (float) Integer.MAX_VALUE, 10);
    // 定义一个名为PI的工具材料，具有较高的挖掘等级和极低的耐久度(钻石镐？)
    public static final Item.ToolMaterial PI_1 = EnumHelper.addToolMaterial("PI",
            3, 1, (float) Integer.MAX_VALUE,-20.0F, 10);

    public ItemPi(String name, ToolMaterial material)
    {
        // 调用父类的构造函数，传入工具材料
        super(material);
        // 使用RegisterUtil工具类注册物品
        // this表示当前对象，name表示物品名称
        RegisterUtil.initItem(this,name);
    }

}