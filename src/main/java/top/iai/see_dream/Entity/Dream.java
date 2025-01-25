package top.iai.see_dream.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

//不在视野中的方块会随机变化
public class Dream extends Entity
{
    private final Random random = new Random();
    private long lastExecutionTime = 0;
    public Dream(World worldIn)
    {
        super(worldIn);
    }
    //各种表
    private static final List<Block> logsBlocks = Arrays.asList(Blocks.LOG, Blocks.LOG2);
    private static final List<Block> leavesBlocks = Arrays.asList(Blocks.LEAVES, Blocks.LEAVES2);
    private static final List<Block> dirtBlocks = Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.SAND, Blocks.FARMLAND);
    private static final List<Block> stonesBlocks = Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE);
    private static final List<Block> airBlocks = Arrays.asList(Blocks.ICE, Blocks.GLASS, Blocks.TNT);
    //表的表
    private static final List<List<Block>> blocksListsList = Arrays.asList(logsBlocks, leavesBlocks, dirtBlocks, stonesBlocks);
    //字典
    private static final Map<Block, List<Block>> blockTransformMap = new HashMap<>();

    static {
        //神秘增强for循环（写字典）
        for (List<Block> blocksList : blocksListsList) {
            for (Block block : blocksList) {
                blockTransformMap.put(block, blocksList);
            }
        }
    }
    /*初版：看着就吓人
    static {
        blockTransformMap.put(Blocks.LOG, Arrays.asList(Blocks.LOG, Blocks.LOG2));
        blockTransformMap.put(Blocks.LOG2, Arrays.asList(Blocks.LOG, Blocks.LOG2));

        blockTransformMap.put(Blocks.LEAVES, Arrays.asList(Blocks.LEAVES, Blocks.LEAVES2));
        blockTransformMap.put(Blocks.LEAVES2, Arrays.asList(Blocks.LEAVES, Blocks.LEAVES2));

        blockTransformMap.put(Blocks.DIRT, Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.SAND, Blocks.FARMLAND));
        blockTransformMap.put(Blocks.GRASS, Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.SAND, Blocks.FARMLAND));
        blockTransformMap.put(Blocks.GRASS_PATH, Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.SAND, Blocks.FARMLAND));
        blockTransformMap.put(Blocks.SAND, Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.SAND, Blocks.FARMLAND));
        blockTransformMap.put(Blocks.FARMLAND, Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.SAND, Blocks.FARMLAND));

        blockTransformMap.put(Blocks.STONE, Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE));
        blockTransformMap.put(Blocks.COAL_ORE, Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE));
        blockTransformMap.put(Blocks.IRON_ORE, Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE));
        blockTransformMap.put(Blocks.GOLD_ORE, Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE));
        blockTransformMap.put(Blocks.DIAMOND_ORE, Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE));

        airBlocks.addAll(Arrays.asList(Blocks.ICE, Blocks.DIRT, Blocks.LOG, Blocks.STONE));
    }*/
    @Override
    public void onUpdate()
    {if (world != null) {
        //每？秒执行一次
        long currentTime = world.getTotalWorldTime();
        if (currentTime % 20 == 0 && currentTime != lastExecutionTime) {
            lastExecutionTime = currentTime;
            Dreaming();
        }
    }
    }
    //做梦
    private void Dreaming() {
        //玩家的一些数据
        double posX = 0;
        double posY = 0;
        double posZ = 0;
        float yaw = 0;
        float pitch = 0;

        if (world != null) {
        World world = Minecraft.getMinecraft().world;
            //偷iai的代码 iai:草
            synchronized(world.playerEntities){
                for (EntityPlayer player : world.playerEntities) {
                    //这些个if语句防止空指针的，不写就有可能崩
                    if (player != null) {
                        posX = player.posX;
                        posY = player.posY + player.getEyeHeight();
                        posZ = player.posZ;
                        yaw = player.rotationYaw % 360;
                        pitch = player.rotationPitch;
                    } else return;
                }
            }
            //定义变化范围，范围半径为128长、128宽、8高
            int rangeX = 128;
            int rangeY = 8;
            int rangeZ = 128;

            //在范围内取随机数
            int randomX = (int)posX - rangeX + random.nextInt(2 * rangeX + 1);
            int randomY = (int)posY - rangeY + random.nextInt(2 * rangeY + 1);//这里让头顶距离高一点
            int randomZ = (int)posZ - rangeZ + random.nextInt(2 * rangeZ + 1);

            //要替换的方块坐标
            BlockPos blockPos = new BlockPos(randomX, randomY, randomZ);

            //计算玩家视线方向
            double var3 = Math.cos(-yaw * 0.017453292 - (float)Math.PI);
            double var5 = Math.sin(-yaw * 0.017453292 - (float)Math.PI);
            double var7 = -Math.cos(-pitch * 0.017453292);
            double var9 = Math.sin(-pitch * 0.017453292);
            Vec3d VEC = new Vec3d(var5 * var7, var9, var3 * var7);

            //视锥判断+距离判断
            if (!isPointInCone(randomX, randomY, randomZ, posX, posY, posZ, VEC.x,  VEC.y,  VEC.z, 65) && Math.sqrt(Math.pow(randomX - posX, 2) + Math.pow(randomY - posY, 2) + Math.pow(randomZ - posZ, 2)) > 20) {
                //获取要替换的方块
                Block currentBlock = world.getBlockState(blockPos).getBlock();
                //声明新方块
                Block newBlock;
                //尝试获取该方块的变化表
                List<Block> possibleTransformations = blockTransformMap.get(currentBlock);
                //判断是不是特殊方块
                if (possibleTransformations != null && !possibleTransformations.isEmpty()) {
                    // 从对应的表中随机选择一个方块
                    newBlock = possibleTransformations.get(random.nextInt(possibleTransformations.size()));
                }
                //啥也不是，从空气表中选
                else {
                    newBlock = airBlocks.get(random.nextInt(airBlocks.size()));
                }
                //替换方块
                synchronized(newBlock){
                    world.setBlockState(blockPos, newBlock.getDefaultState(), 3);
                }
            }
            /*
            // 玩家范围：128^3区域
            int startX = (int) posX - 128;
            int startY = (int) posY - 32;
            int startZ = (int) posZ - 128;
            int endX = (int) posX + 128;
            int endY = (int) posY + 32;
            int endZ = (int) posZ + 128;
            // 遍历该范围内的方块,这部分循环逻辑只有天顶星电脑才能挺住，**建议重写**
            // 确实是一坨，注释保留当笑话看：）神秘for循坏
            for (int x = startX; x <= endX; x++) {
                for (int z = startZ; z <= endZ; z++) {
                    for (int y = startY; y <= endY; y++) {
                        // 只有玩家看不见且距离20以上的方块才进行变化
                        BlockPos blockPos = new BlockPos(x, y, z);
                        if (!isPointInCone(x, y, z, posX, posY, posZ, pitch, yaw, 1, 65) && Math.sqrt(Math.pow(x - posX, 2) + Math.pow(y - posY, 2) + Math.pow(z - posZ, 2)) > 20) {
                            // 随机？%概率改变方块
                            if (random.nextFloat() < 0.8) {
                                // 随机选择新的方块
                                Block newBlock = possibleBlocks.get(random.nextInt(possibleBlocks.size()));
                                //替换方块
                                world.setBlockState(blockPos, newBlock.getDefaultState(), 3);
                            }
                        }
                    }
                }
            }*/
        }else return;
    }

    //判断一个点是否在圆锥内
    //(Px, Py, Pz) 是要判断的点
    //(Vx, Vy, Vz) 是圆锥顶点
    //(dX, dY, Dz) 是圆锥轴线的方向单位向量
    //angle 是圆锥的半角，单位度
    public static boolean isPointInCone(double Px, double Py, double Pz, double Vx, double Vy, double Vz, double dX, double dY, double Dz, double angle)
    {
        //计算点 P 和圆锥顶点 V 之间的向量
        double vectorVPx = Px - Vx;
        double vectorVPy = Py - Vy;
        double vectorVPz = Pz - Vz;
        //计算向量 VP 和圆锥轴线方向 d 的点积
        double dotProduct = vectorVPx * dX + vectorVPy * dY + vectorVPz * Dz;
        //计算向量 VP 的模长
        double lengthVP = Math.sqrt(vectorVPx * vectorVPx + vectorVPy * vectorVPy + vectorVPz * vectorVPz);
        //计算点积和模长的商得到向量 VP 和轴线方向 d 之间的余弦值
        double cosThetaPrime = dotProduct / lengthVP;
        //计算圆锥的半角的余弦值
        double cosTheta = Math.cos(Math.toRadians(angle));
        //判断点是否在圆锥内
        boolean TEST = cosThetaPrime >= cosTheta;
        return TEST;
    }

    @Override
    protected void entityInit() {

    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
