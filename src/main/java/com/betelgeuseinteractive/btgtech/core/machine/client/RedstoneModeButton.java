package com.betelgeuseinteractive.btgtech.core.machine.client;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.machine.MachineTranslationKeys;
import com.betelgeuseinteractive.btgtech.core.machine.logic.RedstoneMode;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * The vanilla button that cycles a machine's redstone mode (ADR 0035, 0045). It shows the current mode as an icon,
 * names it in its tooltip and narration, and follows the mode the server reports.
 */
public final class RedstoneModeButton extends Button {
    /** Width and height of the button. */
    public static final int SIZE = 16;

    private static final int ICON_SIZE = 10;
    private static final Map<RedstoneMode, Identifier> ICONS = new EnumMap<>(RedstoneMode.class);

    static {
        for (RedstoneMode mode : RedstoneMode.values()) {
            ICONS.put(mode, Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/redstone_mode_" + mode.id()));
        }
    }

    private RedstoneMode mode;

    /** Creates the button at the given screen position, showing the given mode. */
    public RedstoneModeButton(int x, int y, RedstoneMode mode, OnPress onPress) {
        super(x, y, SIZE, SIZE, name(mode), onPress, DEFAULT_NARRATION);
        this.mode = mode;
        setTooltip(Tooltip.create(name(mode)));
    }

    /** Shows the given mode; call it when the server reports a mode, for example after a click. */
    public void show(RedstoneMode shown) {
        if (shown != mode) {
            mode = shown;
            setMessage(name(shown));
            setTooltip(Tooltip.create(name(shown)));
        }
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        extractDefaultSprite(graphics);
        int inset = (SIZE - ICON_SIZE) / 2;
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED, ICONS.get(mode), getX() + inset, getY() + inset, ICON_SIZE, ICON_SIZE);
    }

    private static Component name(RedstoneMode mode) {
        return Component.translatable(MachineTranslationKeys.redstoneMode(mode));
    }
}
