package com.betelgeuseinteractive.btgtech.energy;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

/** The item of a battery; its tooltip warns that a broken battery loses its stored energy (ADR 0052). */
public final class BatteryItem extends BlockItem {
    /** Translation key of the warning line. */
    public static final String LOSES_ENERGY = "tooltip.btgtech.battery_loses_energy";

    BatteryItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(
            ItemStack itemStack,
            TooltipContext context,
            TooltipDisplay display,
            Consumer<Component> builder,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable(LOSES_ENERGY).withStyle(ChatFormatting.GRAY));
    }
}
