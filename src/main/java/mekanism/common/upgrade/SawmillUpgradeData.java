package mekanism.common.upgrade;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mekanism.api.inventory.slot.IInventorySlot;
import mekanism.common.base.IRedstoneControl.RedstoneControl;
import mekanism.common.base.ITileComponent;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;

public class SawmillUpgradeData extends MachineUpgradeData {

    //Precision Sawmill Constructor
    public SawmillUpgradeData(boolean redstone, RedstoneControl controlType, double electricityStored, int operatingTicks, EnergyInventorySlot energySlot,
          InputInventorySlot inputSlot, OutputInventorySlot outputSlot, OutputInventorySlot secondaryOutputSlot, List<ITileComponent> components) {
        this(redstone, controlType, electricityStored, new int[]{operatingTicks},
              energySlot, Collections.singletonList(inputSlot), Arrays.asList(outputSlot, secondaryOutputSlot), false, components);
    }

    //Sawing Factory Constructor
    public SawmillUpgradeData(boolean redstone, RedstoneControl controlType, double electricityStored, int[] progress, EnergyInventorySlot energySlot,
          List<IInventorySlot> inputSlots, List<IInventorySlot> outputSlots, boolean sorting, List<ITileComponent> components) {
        super(redstone, controlType, electricityStored, progress, energySlot, inputSlots, outputSlots, sorting, components);
    }
}