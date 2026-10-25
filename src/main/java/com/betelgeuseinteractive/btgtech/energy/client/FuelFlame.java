package com.betelgeuseinteractive.btgtech.energy.client;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.machine.logic.GaugeScale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/** The flame above a generator's fuel slot; it shrinks from the top as the burning fuel is used up. */
final class FuelFlame {
    static final int SIZE = 14;

    private static final Identifier EMPTY = Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/fuel_flame");
    private static final Identifier FILL = Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/fuel_flame_fill");

    private final int x;
    private final int y;

    FuelFlame(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void extract(GuiGraphicsExtractor graphics, int left, int top, int remaining, int total) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EMPTY, left + x, top + y, SIZE, SIZE);
        int lit = GaugeScale.filledLength(remaining, total, SIZE);
        if (lit > 0) {
            int unlit = SIZE - lit;
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED, FILL, SIZE, SIZE, 0, unlit, left + x, top + y + unlit, SIZE, lit);
        }
    }
}
