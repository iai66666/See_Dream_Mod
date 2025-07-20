package top.iai.see_dream.Events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraftforge.common.DimensionManager.getWorld;

public class Dream {
    WorldServer worldServer;
    private final Random random = new Random();
    public static boolean dream = false;
    private int waterTick = 0;
    private int grassTick = 0;
    int rangeX = 128;
    int rangeY = 32;
    int rangeZ = 128;
    int randomX = 0;
    int randomY = 0;
    int randomZ = 0;
    public static int[] xyz;
    Vec3d playerGazeDirection = new Vec3d(0,0,0);
    public Dream() {
    }
    //第一个玩家（如果想找虐想让所有玩家都同时做梦就写成玩家列表）
    EntityPlayer player;
    //各种表
    private static final List<Block> logsBlocks = Arrays.asList(Blocks.LOG, Blocks.LOG2);
    private static final List<Block> leavesBlocks = Arrays.asList(Blocks.LEAVES, Blocks.LEAVES2);
    private static final List<Block> dirtBlocks = Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.FARMLAND);
    private static final List<Block> stonesBlocks = Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE);
    private static final List<Block> plantsBlocks = Arrays.asList(Blocks.TALLGRASS, Blocks.DOUBLE_PLANT);
    private static final List<Block> airBlocks = Arrays.asList(Blocks.WATER, Blocks.LAVA);
    //表的表
    private static final List<List<Block>> blocksListsList = Arrays.asList(logsBlocks, leavesBlocks, dirtBlocks, stonesBlocks, plantsBlocks);
    //字典
    private static final Map<Block, List<Block>> blockTransformMap = new ConcurrentHashMap<>();

    static {
        //神秘增强for循环（写字典）
        for (List<Block> blocksList : blocksListsList) {
            synchronized (blockTransformMap) {
                for (Block block : blocksList) {
                    blockTransformMap.put(block, blocksList);
                }
            }
        }
    }
    //服务端代码
    @SubscribeEvent
    public void onSeverTick(TickEvent event) {
        if (!dream) return;
        worldServer = getWorld(0) != null ? getWorld(0) : null;
        if(worldServer == null) return;
        if (worldServer.playerEntities.isEmpty()) return;
        player = worldServer.playerEntities.get(0);
        if (event.phase == TickEvent.Phase.START) {
            dreaming();
        }
    }

    private void dreaming(){
            waterTick++;
            grassTick++;
            if (waterTick >= 400) {
                waterTick = 0;
                calculateDirection();
                setRandom();
                putBlocks();
            }
            if (grassTick >= 50) {
                grassTick = 0;
                calculateDirection();
                growGrass();
            }
    }
    //草方块增生
    private void growGrass() {
        //玩家眼睛的位置
        Vec3d rayStartPoint = player.getPositionVector().add(0, player.getEyeHeight(), 0);
        //射线最大距离
        double maxDistance = 128.0;
        //射线方向向下偏移
        Vec3d rayVec3d = new Vec3d(-playerGazeDirection.x, -playerGazeDirection.y - random.nextFloat() * 0.5, -playerGazeDirection.z);

        // 执行射线检测
        RayTraceResult result = worldServer.rayTraceBlocks(rayStartPoint, rayStartPoint.add(rayVec3d.x * maxDistance, rayVec3d.y * maxDistance, rayVec3d.z * maxDistance));

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            // 获取碰撞到的方块位置
            BlockPos hitPos = result.getBlockPos();
            //如果碰到的方块距离较小，不执行
            if (Math.sqrt(Math.pow(hitPos.getX() - player.posX, 2) + Math.pow(hitPos.getY() - player.posY + player.getEyeHeight(), 2) + Math.pow(hitPos.getZ() - player.posZ, 2)) < 5)
                return;
            // 获取当前方块
            Block currentBlock = worldServer.getBlockState(hitPos).getBlock();
            Block newBlock;

            // 查找该方块的变化表
            synchronized (blockTransformMap) {
                List<Block> possibleTransformations = blockTransformMap.get(currentBlock);

                // 如果有对应变化的方块，随机选择一个
                if (possibleTransformations != null && !possibleTransformations.isEmpty()) {
                    //草方块增生
                    if (currentBlock == Blocks.GRASS || possibleTransformations == plantsBlocks) {
                        //随机坐标
                        BlockPos growPos = hitPos.add(new BlockPos(random.nextInt(5) - 1, random.nextInt(3) - 1, random.nextInt(5) - 1));
                        //如果超过玩家的头或不是方块，不执行
                        if (growPos.getY() - player.posY >= 2 || worldServer.getBlockState(growPos).getBlock() != Blocks.AIR) return;
                        //判断下面有没有方块，没有就下移
                        while (worldServer.isAirBlock(growPos.down()) || worldServer.getBlockState(growPos.down()).getBlock() == Blocks.TALLGRASS || worldServer.getBlockState(growPos.down()).getBlock() == Blocks.DOUBLE_PLANT) {
                            growPos = growPos.down();
                        }
                        //把下面的方块变成泥土
                        worldServer.setBlockState(growPos.down(), Blocks.DIRT.getDefaultState(), 2);
                        newBlock = Blocks.GRASS;
                        if (newBlock != null) {
                            worldServer.setBlockState(growPos, newBlock.getDefaultState(), 2);
                        }
                        return;
                    }
                    newBlock = possibleTransformations.get(random.nextInt(possibleTransformations.size()));
                }
                else return;
                synchronized (newBlock) {
                    worldServer.setBlockState(hitPos, newBlock.getDefaultState(), 2);
                }
            }
        }
    }
    //放水
    private void putBlocks() {
        //在范围内取随机数
        BlockPos blockPos = new BlockPos(randomX, randomY, randomZ);

        //距离判断 + 视锥判断（先判断距离避免不必要的计算
        if (Math.sqrt(Math.pow(randomX - player.posX, 2) + Math.pow(randomY - player.posY + player.getEyeHeight(), 2) + Math.pow(randomZ - player.posZ, 2)) > 15
                && isPointInCone(randomX, randomY, randomZ, player.posX, player.posY + player.getEyeHeight(), player.posZ, playerGazeDirection.x, playerGazeDirection.y, playerGazeDirection.z, 80)) {

            //获取要替换的方块
            Block currentBlock = worldServer.getBlockState(blockPos).getBlock();
            //声明新方块
            Block newBlock;
            //尝试获取该方块的变化表
            synchronized (blockTransformMap) {
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
                worldServer.setBlockState(blockPos, newBlock.getDefaultState(), 2);
            }
        }
    }

    //计算玩家视线方向
    public void calculateDirection(){
        double var3 = Math.cos(-(player.rotationYaw % 360) * 0.017453292 - (float) Math.PI);
        double var5 = Math.sin(-(player.rotationYaw % 360) * 0.017453292 - (float) Math.PI);
        double var7 = -Math.cos(-player.rotationPitch * 0.017453292);
        double var9 = Math.sin(-player.rotationPitch * 0.017453292);
        playerGazeDirection = new Vec3d(var5 * var7, var9, var3 * var7);
    }
    //取随机数
    public void setRandom(){
        if((int) player.posX - rangeX < xyz[3] & (int) player.posX - rangeX < xyz[0]) {
            randomX = (int) player.posX - rangeX + random.nextInt(2 * rangeX + 1);
        }
        if ((int) (player.posY + player.getEyeHeight()) - rangeY < xyz[4] & (int) (player.posY + player.getEyeHeight()) - rangeY < xyz[1]) {
            randomY = (int) (player.posY + player.getEyeHeight()) - rangeY + random.nextInt(2 * rangeY + 1) + 16;
        }
        if ((int) player.posZ - rangeZ < xyz[5] & (int) player.posZ - rangeZ < xyz[2]){
            randomZ = (int) player.posZ - rangeZ + random.nextInt(2 * rangeZ + 1);
        }
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
        return !(cosThetaPrime >= cosTheta);
    }
}
