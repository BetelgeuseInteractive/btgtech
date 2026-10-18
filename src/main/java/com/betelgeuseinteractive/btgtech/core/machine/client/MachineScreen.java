package com.betelgeuseinteractive.btgtech.core.machine.client;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.MachineMenu;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Base of every machine screen (ADR 0044, 0045): the 176 × 166 panel of the machine's tier, a frame behind every slot,
 * the energy bar on the left, the redstone mode button at the top right and the title centered.
 *
 * <p>Subclasses place their slots in the menu and draw their own gauges, such as a {@link ProgressArrow}, after calling
 * {@code super.extractBackground}. They keep the area of the redstone mode button free.
 */
public abstract class MachineScreen<M extends MachineMenu> extends AbstractContainerScreen<M> {
    /** Left edge of the energy bar. */
    public static final int ENERGY_BAR_X = 7;
    /** Top edge of the energy bar. */
    public static final int ENERGY_BAR_Y = 16;
    /** Left edge of the redstone mode button; machine slots and gauges stay to its left above row 22. */
    public static final int REDSTONE_BUTTON_X = 152;
    /** Top edge of the redstone mode button. */
    public static final int REDSTONE_BUTTON_Y = 6;

    private static final int LABEL_COLOR = 0xFF404040;
    private static final int TITLE_MARGIN = 8;
    private static final int TITLE_BUTTON_GAP = 2;
    private static final int SLOT_FRAME_SIZE = 18;
    private static final Identifier SLOT_FRAME = Identifier.withDefaultNamespace("container/slot");
    private static final Map<Tier, Identifier> PANELS = new EnumMap<>(Tier.class);

    static {
        for (Tier tier : Tier.values()) {
            PANELS.put(tier, Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "machine/" + tier.id() + "_panel"));
        }
    }

    private final EnergyBar energyBar = new EnergyBar(ENERGY_BAR_X, ENERGY_BAR_Y);
    private RedstoneModeButton redstoneModeButton;

    protected MachineScreen(M menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addRenderableWidget(new RedstoneModeButton(
                leftPos + REDSTONE_BUTTON_X,
                topPos + REDSTONE_BUTTON_Y,
                menu.redstoneMode(),
                button -> cycleRedstoneMode()));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        redstoneModeButton.show(menu.redstoneMode());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED, PANELS.get(menu.tier()), leftPos, topPos, imageWidth, imageHeight);
        for (Slot slot : menu.slots) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    SLOT_FRAME,
                    leftPos + slot.x - 1,
                    topPos + slot.y - 1,
                    SLOT_FRAME_SIZE,
                    SLOT_FRAME_SIZE);
        }
        if (menu.hasEnergy()) {
            energyBar.extract(graphics, leftPos, topPos, menu.energy(), menu.energyCapacity());
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        extractTitle(graphics);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, LABEL_COLOR, false);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        if (menu.hasEnergy() && menu.getCarried().isEmpty() && energyBar.isHovered(leftPos, topPos, mouseX, mouseY)) {
            graphics.setComponentTooltipForNextFrame(
                    font, energyBar.tooltip(menu.energy(), menu.energyCapacity()), mouseX, mouseY);
        }
    }

    // Centered when it fits beside the redstone mode button, moved left when needed, shortened only as a last resort.
    private void extractTitle(GuiGraphicsExtractor graphics) {
        int right = REDSTONE_BUTTON_X - TITLE_BUTTON_GAP;
        int available = right - TITLE_MARGIN;
        int width = font.width(title);
        if (width <= available) {
            int x = Math.max(TITLE_MARGIN, Math.min((imageWidth - width) / 2, right - width));
            graphics.text(font, title, x, titleLabelY, LABEL_COLOR, false);
        } else {
            graphics.text(
                    font,
                    ComponentRenderUtils.clipText(title, font, available),
                    TITLE_MARGIN,
                    titleLabelY,
                    LABEL_COLOR,
                    false);
        }
    }

    private void cycleRedstoneMode() {
        if (minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, MachineMenu.CYCLE_REDSTONE_MODE_BUTTON);
        }
    }
}
