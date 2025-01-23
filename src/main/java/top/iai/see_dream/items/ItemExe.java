package top.iai.see_dream.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import top.iai.see_dream.Entity.EntityNull;
import top.iai.see_dream.RegisterUtil;

import java.util.Objects;

public class ItemExe extends Item {
    // 构造函数
    public ItemExe(String name){
        super(); // 调用父类构造函数
        this.maxStackSize = 1; // 设置最大堆叠数量为1
        RegisterUtil.initItem(this,name); // 注册物品
    }

    // 重写onItemRightClick方法，处理玩家右键点击物品时的逻辑
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        ItemStack itemstack = playerIn.getHeldItem(handIn); // 获取玩家手中持有的物品堆叠

        // 如果玩家不在创造模式
        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1); // 消耗一个物品
        }

        // 播放声音
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        // 如果当前不是客户端世界（即允许生成实体）
        if (!worldIn.isRemote)
        {
            EntityNull entitynull = new EntityNull(worldIn, playerIn); // 创建EntityNull实体
            entitynull.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F); // 设置实体投射参数
            worldIn.spawnEntity(entitynull); // 在世界中生成实体
        }

        // 为玩家添加使用该物品的统计信息
        playerIn.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));

        // 返回操作结果和更新后的物品堆叠
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
}

