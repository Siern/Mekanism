package mekanism.generators.client.gui;

import java.util.Arrays;
import java.util.Collections;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.client.gui.element.GuiProgress;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge.Type;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.EmptyTileContainer;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.generators.client.gui.element.GuiReactorTab;
import mekanism.generators.client.gui.element.GuiReactorTab.ReactorTab;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.tile.reactor.TileEntityReactorController;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

public class GuiReactorFuel extends GuiReactorInfo {

    private TextFieldWidget injectionRateField;

    public GuiReactorFuel(EmptyTileContainer<TileEntityReactorController> container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    public void init() {
        super.init();
        ResourceLocation resource = getGuiLocation();
        addButton(new GuiEnergyInfo(() -> tile.isFormed() ? Arrays.asList(MekanismLang.STORING.translate(EnergyDisplay.of(tile.getEnergy(), tile.getMaxEnergy())),
              GeneratorsLang.PRODUCING_AMOUNT.translate(EnergyDisplay.of(tile.getReactor().getPassiveGeneration(false, true)))) : Collections.emptyList(),
              this, resource));
        addButton(new GuiGasGauge(() -> tile.deuteriumTank, Type.SMALL, this, resource, 25, 64));
        addButton(new GuiGasGauge(() -> tile.fuelTank, Type.STANDARD, this, resource, 79, 50));
        addButton(new GuiGasGauge(() -> tile.tritiumTank, Type.SMALL, this, resource, 133, 64));
        addButton(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tile.getActive() ? 1 : 0;
            }
        }, ProgressBar.SMALL_RIGHT, this, resource, 45, 75));
        addButton(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tile.getActive() ? 1 : 0;
            }
        }, ProgressBar.SMALL_LEFT, this, resource, 99, 75));
        addButton(new GuiReactorTab(this, tile, ReactorTab.HEAT, resource));
        addButton(new GuiReactorTab(this, tile, ReactorTab.STAT, resource));

        String prevRad = injectionRateField != null ? injectionRateField.getText() : "";
        addButton(injectionRateField = new TextFieldWidget(font, getGuiLeft() + 98, getGuiTop() + 115, 26, 11, ""));
        injectionRateField.setMaxStringLength(2);
        injectionRateField.setText(prevRad);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawString(tile.getName(), 46, 6, 0x404040);
        drawCenteredText(GeneratorsLang.REACTOR_INJECTION_RATE.translate(tile.getReactor() == null ? MekanismLang.NONE : tile.getReactor().getInjectionRate()),
              0, getXSize(), 35, 0x404040);
        drawString(GeneratorsLang.REACTOR_EDIT_RATE.translate(), 50, 117, 0x404040);
    }

    @Override
    public void tick() {
        super.tick();
        injectionRateField.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (injectionRateField.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                //Manually handle hitting escape making the field lose focus
                injectionRateField.setFocused2(false);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                setInjection();
                return true;
            }
            return injectionRateField.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int keyCode) {
        if (injectionRateField.isFocused()) {
            if (Character.isDigit(c)) {
                //Only allow a subset of characters to be entered into the frequency text box
                return injectionRateField.charTyped(c, keyCode);
            }
            return false;
        }
        return super.charTyped(c, keyCode);
    }

    private void setInjection() {
        if (!injectionRateField.getText().isEmpty()) {
            int toUse = Math.max(0, Integer.parseInt(injectionRateField.getText()));
            toUse -= toUse % 2;
            TileNetworkList data = TileNetworkList.withContents(0, toUse);
            Mekanism.packetHandler.sendToServer(new PacketTileEntity(tile, data));
            injectionRateField.setText("");
        }
    }
}