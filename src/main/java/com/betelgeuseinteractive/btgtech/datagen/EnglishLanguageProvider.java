package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreCreativeModeTabs;
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
    }
}
