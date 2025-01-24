package top.iai.see_dream.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import top.iai.see_dream.Entity.Dream;
import top.iai.see_dream.Entity.EntityNull;
import top.iai.see_dream.RegisterUtil;

import java.util.Random;

//“末地烛？”方块类
public class JB extends BlockDirectional {
    // 构造函数
    protected JB(Material materialIn, String name) {
        super(materialIn);
        // 设置默认状态，方向向上
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        // 注册方块
        RegisterUtil.initBlock(this, name);
    }

    // 重写获取渲染类型的方法
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        // 返回默认渲染类型
        return EnumBlockRenderType.MODEL;
    }

    // 重写邻居方块变化时的方法
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        // 如果不是客户端
        if(!worldIn.isRemote) {
            // 在该位置生成一个牛奶桶物品
            spawnAsEntity(worldIn, pos, new ItemStack(Items.MILK_BUCKET));
        }
    }

    // 重写方块被右键时的方法
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // 如果不是客户端
        if(!worldIn.isRemote) {
            // 在该位置生成一个牛奶桶物品
            spawnAsEntity(worldIn, pos, new ItemStack(Items.MILK_BUCKET));

            //鹿JB做梦（不是
            Dream dream = new Dream(worldIn);
            dream.setPosition(Minecraft.getMinecraft().player.posX,Minecraft.getMinecraft().player.posY,Minecraft.getMinecraft().player.posZ);
            worldIn.spawnEntity(dream);

            return true;
        }
        return true;
    }

    // 定义三个轴对齐的边界框
    protected static final AxisAlignedBB END_ROD_VERTICAL_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB END_ROD_NS_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB END_ROD_EW_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    // 重写根据旋转调整状态的方法
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    // 重写根据镜像调整状态的方法
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withProperty(FACING, mirrorIn.mirror((EnumFacing)state.getValue(FACING)));
    }

    // 重写获取边界框的方法
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        // 根据方向轴获取不同的边界框
        switch (((EnumFacing)state.getValue(FACING)).getAxis())
        {
            case X:
            default:
                return END_ROD_EW_AABB;
            case Z:
                return END_ROD_NS_AABB;
            case Y:
                return END_ROD_VERTICAL_AABB;
        }
    }

    // 重写判断是否为不透明立方体的方法
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    // 重写判断是否为完整立方体的方法
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    // 重写判断是否可以放置方块的方法
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    // 重写根据放置位置和朝向获取状态的方法
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));

        if (iblockstate.getBlock() == Blocks.END_ROD)
        {
            EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing == facing)
            {
                return this.getDefaultState().withProperty(FACING, facing.getOpposite());
            }
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }

    // 末地烛粒子方法，直接复制的原版代码
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

        if (rand.nextInt(5) == 0)
        {
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0 + (double)enumfacing.getXOffset() * d3, d1 + (double)enumfacing.getYOffset() * d3, d2 + (double)enumfacing.getZOffset() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
        }
    }

    // 重写根据元数据获取状态的方法
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.byIndex(meta));
        return iblockstate;
    }

    // 重写获取渲染层的方法
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    // 重写根据状态获取元数据的方法
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    // 创建方块状态容器
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    // 重写获取推动反应的方法
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }

    // 重写获取方块面形状的方法
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
