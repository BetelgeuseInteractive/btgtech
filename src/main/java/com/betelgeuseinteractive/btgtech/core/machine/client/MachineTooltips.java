package com.betelgeuseinteractive.btgtech.core.machine.client;

import com.betelgeuseinteractive.btgtech.core.machine.MachineTranslationKeys;
import com.betelgeuseinteractive.btgtech.core.machine.logic.QuantityFormat;
import com.betelgeuseinteractive.btgtech.core.machine.logic.SiAmount;
import com.betelgeuseinteractive.btgtech.core.machine.logic.SiPrefix;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * Tooltip helpers for machine screens (gui.md, section 5): one short line, details while Shift is held, and amounts
 * with SI prefixes.
 */
public final class MachineTooltips {
    private MachineTooltips() {}

    /** Returns an energy amount with an SI prefix, for example {@code 12.3 kFE}. */
    public static Component energy(long amount) {
        SiAmount scaled = QuantityFormat.si(amount);
        return Component.translatable(MachineTranslationKeys.energy(scaled.prefix()), scaled.number());
    }

    /** Returns an exact energy amount, for example {@code 12,345 FE}. */
    public static Component exactEnergy(long amount) {
        return Component.translatable(MachineTranslationKeys.energy(SiPrefix.NONE), QuantityFormat.exact(amount));
    }

    /**
     * Returns the short line, followed by the details in gray while Shift is held, or by a hint to hold Shift otherwise.
     */
    public static List<Component> withDetails(Component summary, List<Component> details) {
        List<Component> lines = new ArrayList<>(1 + details.size());
        lines.add(summary);
        if (Minecraft.getInstance().hasShiftDown()) {
            for (Component detail : details) {
                lines.add(detail.copy().withStyle(ChatFormatting.GRAY));
            }
        } else {
            lines.add(Component.translatable(MachineTranslationKeys.HOLD_SHIFT).withStyle(ChatFormatting.DARK_GRAY));
        }
        return lines;
    }
}
