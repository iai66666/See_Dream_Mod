package top.iai.see_dream.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import top.iai.see_dream.RegisterUtil;

@SuppressWarnings("deprecation")
// “挂画”方块类
public class BlockBaseDraw extends Block {
    // 构造函数，初始化BlockBase对象
    public BlockBaseDraw(Material material, String name) {
        // 调用父类Block的构造函数，传入材质参数
        super(material);
        // 设置默认状态为朝北
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        // 注册当前方块到游戏世界中
        RegisterUtil.initBlock(this, name);
    }

    // 定义方块的朝向属性
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    // 定义不同朝向的边界框
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0d, 0.0D, 0.0625D, 1d, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.9375D, 0d, 0.0D, 1.0D, 1d, 1d);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0d, 0.0D, 1.0D, 1d, 0.0625D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.9375D,0.0D , 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D,0.0D , 1.0D, 0.0625D, 1.0D);

    @SuppressWarnings({"EnhancedSwitchMigration", "DefaultNotLastCaseInSwitch"})
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        // 根据朝向返回对应的边界框
        switch (state.getValue(FACING)) {
            case NORTH:
            default:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
                return EAST_AABB;
            case UP:
                return UP_AABB;
            case DOWN:
                return DOWN_AABB;
        }
    }

    /**
     * 当相邻方块发生变化时调用，标记此状态应在相邻方块变化时执行任何检查。
     * 可能的情况包括红石电源更新、仙人掌方块因相邻实体方块弹出等。
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        // 获取朝向
        EnumFacing enumfacing = state.getValue(FACING);

        // 如果相邻方块不是实体方块，则掉落
        if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }

        // 调用父类方法
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    /**
     * 将给定的元数据转换为对应此方块的 BlockState。
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        // 根据元数据获取朝向
        EnumFacing facing = EnumFacing.byIndex(meta);

        // 如果朝向是 Y 轴，则默认朝北
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }

        // 返回带有朝向属性的默认状态
        return this.getDefaultState().withProperty(FACING, facing);
    }

    /**
     * 将 BlockState 转换为对应的元数据值。
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        // 返回朝向的索引值
        return state.getValue(FACING).getIndex();
    }

    /**
     * 返回给定旋转后的 BlockState。如果不适用，则返回传入的 BlockState。
     * @deprecated 尽可能通过 {@link IBlockState#withRotation(Rotation)} 调用。实现/重写是可以的。
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        float x = (float) placer.getLookVec().x;
        float z = (float) placer.getLookVec().z;
        EnumFacing facing = EnumFacing.getFacingFromVector(x, 0.0f, z).getOpposite();
        worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(FACING, facing));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        // 返回false，表示该方块不是不透明的
        return false;
    }

    // 重写isFullCube方法，判断方块是否是一个完整的立方体
    @Override
    public boolean isFullCube(IBlockState state) {
        // 返回false，表示该方块不是一个完整的立方体
        return false;
    }
}
