package com.thatblondeguy.drawernames;

import com.buuz135.functionalstorage.block.tile.ItemControllableDrawerTile;
import com.buuz135.functionalstorage.inventory.BigInventoryHandler;
import com.buuz135.functionalstorage.inventory.CompactingInventoryHandler;
import com.buuz135.functionalstorage.util.CompactingUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Adds one line naming what an item drawer holds. Functional Storage's own Jade provider (priority 4999)
 * draws only icons and counts for item drawers, so this runs just after it.
 */
public enum DrawerNameProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("drawernames", "contents_names");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        try {
            if (!(accessor.getBlockEntity() instanceof ItemControllableDrawerTile<?> tile)) {
                return;
            }
            IItemHandler storage = tile.getStorage();
            Set<String> names = new LinkedHashSet<>();
            if (storage instanceof CompactingInventoryHandler compacting) {
                for (CompactingUtil.Result result : compacting.getResultList()) {
                    addName(names, result.getResult());
                }
            } else if (storage instanceof BigInventoryHandler big) {
                for (BigInventoryHandler.BigStack stack : big.getStoredStacks()) {
                    addName(names, stack.getStack());
                }
            }
            if (!names.isEmpty()) {
                tooltip.add(Component.literal(String.join(" / ", names)));
            }
        } catch (RuntimeException ignored) {
            // A tooltip must never take the client down; skip the line if the drawer reports odd data.
        }
    }

    private static void addName(Set<String> names, ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            names.add(stack.getHoverName().getString());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public int getDefaultPriority() {
        return 5000;
    }
}
