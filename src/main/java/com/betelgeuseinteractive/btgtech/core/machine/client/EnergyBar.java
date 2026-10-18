package com.betelgeuseinteractive.btgtech.core.machine.client;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.machine.MachineTranslationKeys;
import com.betelgeuseinteractive.btgtech.core.machine.logic.GaugeScale;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** The energy bar of a machine screen: stored energy fills it from the bottom (gui.md, section 2). */
public final class EnergyBar {
    /** Width of the bar including its frame. */
    public static final int WIDTH = 14;
    /** Height of the bar including its frame. */
    public static final int HEIGHT = 54;

    private static final int FILL_WIDTH = WIDTH - 2;
    private static final int FILL_HEIGHT = HEIGHT - 2;
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/energy_bar");
    private static final Identifier FILL = Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/energy_bar_fill");

    private final int x;
    private final int y;

    /** Creates an energy bar at the given position relative to the screen's top left corner. */
    public EnergyBar(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Draws the bar for a screen whose top left corner is at {@code left, top}. */
    public void extract(GuiGraphicsExtractor graphics, int left, int top, int energy, int capacity) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, left + x, top + y, WIDTH, HEIGHT);
        int filled = GaugeScale.filledLength(energy, capacity, FILL_HEIGHT);
        if (filled > 0) {
            int unfilled = FILL_HEIGHT - filled;
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    FILL,
                    FILL_WIDTH,
                    FILL_HEIGHT,
                    0,
                    unfilled,
                    left + x + 1,
                    top + y + 1 + unfilled,
                    FILL_WIDTH,
                    filled);
        }
    }

    /** Returns whether the mouse is over the bar of a screen whose top left corner is at {@code left, top}. */
    public boolean isHovered(int left, int top, double mouseX, double mouseY) {
        double localX = mouseX - left - x;
        double localY = mouseY - top - y;
        return localX >= 0 && localX < WIDTH && localY >= 0 && localY < HEIGHT;
    }

    /** Returns the tooltip: stored and capacity with SI prefixes, the exact values while Shift is held. */
    public List<Component> tooltip(int energy, int capacity) {
        return MachineTooltips.withDetails(
                Component.translatable(
                        MachineTranslationKeys.ENERGY_BAR,
                        MachineTooltips.energy(energy),
                        MachineTooltips.energy(capacity)),
                List.of(Component.translatable(
                        MachineTranslationKeys.ENERGY_BAR,
                        MachineTooltips.exactEnergy(energy),
                        MachineTooltips.exactEnergy(capacity))));
    }
}
