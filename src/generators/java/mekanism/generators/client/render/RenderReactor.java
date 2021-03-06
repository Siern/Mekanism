package mekanism.generators.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import mekanism.api.text.EnumColor;
import mekanism.client.MekanismClient;
import mekanism.client.model.ModelEnergyCube.ModelEnergyCore;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.generators.common.tile.reactor.TileEntityReactorController;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class RenderReactor extends TileEntityRenderer<TileEntityReactorController> {

    private ModelEnergyCore core = new ModelEnergyCore();

    public RenderReactor(TileEntityRendererDispatcher renderer) {
        super(renderer);
    }

    @Override
    public void render(@Nonnull TileEntityReactorController tile, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light,
          int overlayLight) {
        if (tile.isBurning()) {
            matrix.push();
            matrix.translate(0.5, -1.5, 0.5);

            long scaledTemp = Math.round(tile.getPlasmaTemp() / 1E8);
            float ticks = MekanismClient.ticksPassed + partialTick;
            double scale = 1 + 0.7 * Math.sin(Math.toRadians(ticks * 3.14 * scaledTemp + 135F));
            renderPart(matrix, renderer, overlayLight, EnumColor.AQUA, scale, ticks, scaledTemp, -6, -7, 0, 36);

            scale = 1 + 0.8 * Math.sin(Math.toRadians(ticks * 3 * scaledTemp));
            renderPart(matrix, renderer, overlayLight, EnumColor.RED, scale, ticks, scaledTemp, 4, 4, 0, 36);

            scale = 1 - 0.9 * Math.sin(Math.toRadians(ticks * 4 * scaledTemp + 90F));
            renderPart(matrix, renderer, overlayLight, EnumColor.ORANGE, scale, ticks, scaledTemp, 5, -3, -35, 106);

            matrix.pop();
        }
    }

    private void renderPart(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int overlayLight, EnumColor color, double scale, float ticks,
          long scaledTemp, int mult1, int mult2, int shift1, int shift2) {
        float ticksScaledTemp = ticks * scaledTemp;
        matrix.push();
        matrix.scale((float) scale, (float) scale, (float) scale);
        matrix.rotate(Vector3f.YP.rotationDegrees(ticksScaledTemp * mult1 + shift1));
        matrix.rotate(RenderEnergyCube.coreVec.rotationDegrees(ticksScaledTemp * mult2 + shift2));
        core.render(matrix, renderer, MekanismRenderer.FULL_LIGHT, overlayLight, color, 1);
        matrix.pop();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityReactorController tile) {
        return tile.isBurning();
    }
}