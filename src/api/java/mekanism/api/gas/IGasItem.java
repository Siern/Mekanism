package mekanism.api.gas;

import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;

/**
 * Implement this in your item class if it can store or transfer certain gasses.
 *
 * @author AidanBrady
 */
//TODO: Make gas items be handled through capabilities (at the very least in 1.14, given if we suddenly make it only
// look for the capabilities, and addons only implement the interface they won't return that they have the capability)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IGasItem {

    /**
     * Gets the rate of transfer this item can handle.
     *
     * @return The transfer rate of what the item can handle.
     */
    int getRate(ItemStack itemstack);

    /**
     * Adds a defined amount of a certain gas to an item.
     *
     * @param itemstack - the itemstack to add gas to
     * @param stack     - the type and amount of gas to add
     *
     * @return the gas that was accepted by the item
     */
    int addGas(ItemStack itemstack, GasStack stack);

    /**
     * Removes the defined amount of a certain gas from the item.
     *
     * @param itemstack - the itemstack to remove gas from
     * @param amount    - the amount of gas to remove
     *
     * @return the gas that was removed by the item
     */
    GasStack removeGas(ItemStack itemstack, int amount);

    /**
     * Whether or not this storage tank be given a specific gas.
     *
     * @param itemstack - the itemstack to check
     * @param type      - the type of gas the tank can possibly receive
     *
     * @return if the item be charged
     */
    boolean canReceiveGas(ItemStack itemstack, Gas type);

    /**
     * Whether or not this item can give a gas receiver a certain type of gas.
     *
     * @param itemstack - the itemstack to check
     * @param type      - the type of gas the tank can provide
     *
     * @return if the item can provide gas
     */
    boolean canProvideGas(ItemStack itemstack, Gas type);

    /**
     * Get the gas of a declared type.
     *
     * @param itemstack - ItemStack parameter
     *
     * @return gas stored
     */
    GasStack getGas(ItemStack itemstack);

    /**
     * Set the gas of a declared type to a new amount;
     *
     * @param itemstack - ItemStack parameter
     * @param stack     - the type and amount of gas to add
     */
    void setGas(ItemStack itemstack, GasStack stack);

    /**
     * Gets the maximum amount of gas this tile entity can store.
     *
     * @param itemstack - ItemStack parameter
     *
     * @return maximum gas
     */
    int getMaxGas(ItemStack itemstack);

    //TODO: This is mainly here to remember to add things like this when porting IGasItem to a capability
    default int getNeeded(ItemStack itemStack) {
        return Math.max(0, getMaxGas(itemStack) - getGas(itemStack).getAmount());
    }
}