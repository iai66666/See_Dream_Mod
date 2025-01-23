package top.iai.see_dream.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.common.util.EnumHelper;
import top.iai.see_dream.RegisterUtil;

public class ItemPi extends ItemPickaxe {
    public static final Item.ToolMaterial PIX = EnumHelper.addToolMaterial("PIX", 3, 0, (float) Integer.MAX_VALUE, (float) Integer.MAX_VALUE, 10);
    public static final Item.ToolMaterial PI_1 = EnumHelper.addToolMaterial("PI", 3, 1, (float) Integer.MAX_VALUE,-20.0F, 10);

    public ItemPi(String name, ToolMaterial material)
    {
        super(material);
        RegisterUtil.initItem(this,name);
    }
}