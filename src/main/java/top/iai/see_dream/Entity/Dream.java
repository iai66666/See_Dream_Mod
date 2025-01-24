package top.iai.see_dream.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//不在视野中的方块会随机变化
public class Dream extends Entity
{
    private final Random random = new Random();
    //计时用的
    private long lastExecutionTime = 0;

    public Dream(World worldIn)
    {
        super(worldIn);
    }

    //可能变成的方块列表
    private static final List<Block> possibleBlocks = new ArrayList<>();
    static
    {
        //在这里添加可以变成的方块
        possibleBlocks.add(Blocks.ICE);//我超，冰！
    }

    @Override
    public void onUpdate()
    {if (world != null) {
        //每秒执行一次
        long currentTime = world.getTotalWorldTime();
        if (currentTime % 100 == 0 && currentTime != lastExecutionTime) {
            lastExecutionTime = currentTime;
            Dreaming();
        }
    }else return;
    }
    //做梦
    private void Dreaming() {
        double posX = 0;
        double posY = 0;
        double posZ = 0;
        float yaw = 0;
        float pitch = 0;
        if (world != null) {
        World world = Minecraft.getMinecraft().world;
            //偷iai的代码 iai:草
            for (EntityPlayer player : world.playerEntities) {
                //这些个if语句防止空指针的，不写就有可能崩
                if (player != null) {
                    posX = player.posX;
                    posY = player.posY;
                    posZ = player.posZ;
                    yaw = player.cameraYaw;
                    pitch = player.cameraPitch;
                } else return;
        }
            // 玩家范围：128^3区域
            int startX = (int) posX - 128;
            int startY = (int) posY - 32;
            int startZ = (int) posZ - 128;
            int endX = (int) posX + 128;
            int endY = (int) posY + 32;
            int endZ = (int) posZ + 128;

            // 遍历该范围内的方块,这部分循环逻辑只有天顶星电脑才能挺住，**建议重写**
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
            }
        }else return;
    }
    //判断一个点是否在圆锥内
    // (Px, Py, Pz) 是要判断的点
    //(Vx, Vy, Vz) 是圆锥顶点
    //(dX, dY, Dz) 是圆锥轴线的方向单位向量
    //angle 是圆锥的半角，单位度
    public static boolean isPointInCone(double Px, double Py, double Pz, double Vx, double Vy, double Vz, double dX, double dY, double Dz, double angle) {

        // 1. 计算点 P 和圆锥顶点 V 之间的向量
        double vectorVPx = Px - Vx;
        double vectorVPy = Py - Vy;
        double vectorVPz = Pz - Vz;

        // 2. 计算向量 VP 和圆锥轴线方向 d 的点积
        double dotProduct = vectorVPx * dX + vectorVPy * dY + vectorVPz * Dz;

        // 3. 计算向量 VP 的模长
        double lengthVP = Math.sqrt(vectorVPx * vectorVPx + vectorVPy * vectorVPy + vectorVPz * vectorVPz);

        // 4. 计算点积和模长的商得到向量 VP 和轴线方向 d 之间的余弦值
        double cosThetaPrime = dotProduct / lengthVP;

        // 5. 计算圆锥的半角的余弦值
        double cosTheta = Math.cos(Math.toRadians(angle));

        // 6. 判断点是否在圆锥内
        return cosThetaPrime >= cosTheta;
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
