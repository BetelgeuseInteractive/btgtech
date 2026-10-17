package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreCreativeModeTabs;
import com.betelgeuseinteractive.btgtech.core.machine.MachineTranslationKeys;
import com.betelgeuseinteractive.btgtech.core.machine.logic.RedstoneMode;
import com.betelgeuseinteractive.btgtech.core.machine.logic.SiPrefix;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** Generates {@code en_us.json} in per-feature sections; the other languages are maintained by hand (ADR 0051). */
final class EnglishLanguageProvider extends LanguageProvider {
    EnglishLanguageProvider(PackOutput output) {
        super(output, BTGTech.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addCore();
    }

    private void addCore() {
        add(CoreCreativeModeTabs.BTG_TECH_TITLE, "BTG Tech");
        // Machine screens
        add(MachineTranslationKeys.energy(SiPrefix.NONE), "%s FE");
        add(MachineTranslationKeys.energy(SiPrefix.KILO), "%s kFE");
        add(MachineTranslationKeys.energy(SiPrefix.MEGA), "%s MFE");
        add(MachineTranslationKeys.energy(SiPrefix.GIGA), "%s GFE");
        add(MachineTranslationKeys.ENERGY_BAR, "%s / %s");
        add(MachineTranslationKeys.redstoneMode(RedstoneMode.IGNORE), "Redstone: ignored");
        add(MachineTranslationKeys.redstoneMode(RedstoneMode.ACTIVE_WITH_SIGNAL), "Redstone: active with signal");
        add(MachineTranslationKeys.redstoneMode(RedstoneMode.ACTIVE_WITHOUT_SIGNAL), "Redstone: active without signal");
        add(MachineTranslationKeys.HOLD_SHIFT, "Hold Shift for details");
    }
}
