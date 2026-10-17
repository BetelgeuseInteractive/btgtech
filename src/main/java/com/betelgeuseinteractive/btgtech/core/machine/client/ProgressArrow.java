package com.betelgeuseinteractive.btgtech.core.machine.client;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.machine.logic.GaugeScale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/** The arrow between a machine's inputs and outputs; it fills from left to right as the operation proceeds. */
public final class ProgressArrow {
    /** Width of the arrow. */
    public static final int WIDTH = 22;
    /** Height of the arrow. */
    public static final int HEIGHT = 15;

    private static final Identifier EMPTY = Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/progress_arrow");
    private static final Identifier FILL =
            Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/progress_arrow_fill");

    private final int x;
    private final int y;

    /** Creates an arrow at the given position relative to the screen's top left corner. */
    public ProgressArrow(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Draws the arrow for a screen whose top left corner is at {@code left, top}. */
    public void extract(GuiGraphicsExtractor graphics, int left, int top, int progress, int duration) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EMPTY, left + x, top + y, WIDTH, HEIGHT);
        int filled = GaugeScale.filledLength(progress, duration, WIDTH);
        if (filled > 0) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED, FILL, WIDTH, HEIGHT, 0, 0, left + x, top + y, filled, HEIGHT);
        }
    }
}
