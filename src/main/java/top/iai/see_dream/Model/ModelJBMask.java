package top.iai.see_dream.Model;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports

// 用blockbench做的模型，这辈子不想再做了

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelJBMask extends ModelBiped { // 继承ModelBiped
	private final ModelRenderer JBMASK;

	public ModelJBMask() {
		//super(1.0F, 0, 30, 30); // 这个用了貌似会出问题
		this.textureWidth = 30;
		this.textureHeight = 30;

		// 将自定义部件绑定到 bipedHead
		JBMASK = new ModelRenderer(this);
		JBMASK.setRotationPoint(0.0F, 0.0f, 0.0F); // 这个不要动 我也不知道为什么
		JBMASK.cubeList.add(new ModelBox(JBMASK, -1, 0, -8.0F, -11.0F, -5.0F, 15, 15, 0, 0.0F, false));
		// 参数微调过了 真阴间

		// 替换原版头盔模型
		this.bipedHead.addChild(JBMASK);
		this.bipedHeadwear.isHidden = true; // 隐藏原版第二层头盔
	}

}