package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreBlocks;
import com.betelgeuseinteractive.btgtech.core.CoreCreativeModeTabs;
import com.betelgeuseinteractive.btgtech.core.CoreItems;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.MachineTranslationKeys;
import com.betelgeuseinteractive.btgtech.core.machine.logic.RedstoneMode;
import com.betelgeuseinteractive.btgtech.core.machine.logic.SiPrefix;
import com.betelgeuseinteractive.btgtech.energy.EnergyBlocks;
import com.betelgeuseinteractive.btgtech.energy.EnergyItems;
import com.betelgeuseinteractive.btgtech.material.MaterialBlocks;
import com.betelgeuseinteractive.btgtech.material.MaterialItems;
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
        addMaterials();
        addEnergy();
    }

    private void addCore() {
        add(CoreCreativeModeTabs.BTG_TECH_TITLE, "BTG Tech");
        // Crafting components
        addBlock(CoreBlocks.BASIC_MACHINE_CASING, "Basic Machine Casing");
        addItem(CoreItems.BASIC_CIRCUIT, "Basic Circuit");
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

    private void addMaterials() {
        addBlock(MaterialBlocks.TIN_ORE, "Tin Ore");
        addBlock(MaterialBlocks.DEEPSLATE_TIN_ORE, "Deepslate Tin Ore");
        addItem(MaterialItems.RAW_TIN, "Raw Tin");
        addItem(MaterialItems.TIN_INGOT, "Tin Ingot");
        addItem(MaterialItems.TIN_NUGGET, "Tin Nugget");
        addItem(MaterialItems.TIN_DUST, "Tin Dust");
        addItem(MaterialItems.TIN_PLATE, "Tin Plate");
        addBlock(MaterialBlocks.TIN_BLOCK, "Block of Tin");
        addBlock(MaterialBlocks.RAW_TIN_BLOCK, "Block of Raw Tin");
        addItem(MaterialItems.BRONZE_INGOT, "Bronze Ingot");
        addItem(MaterialItems.BRONZE_NUGGET, "Bronze Nugget");
        addItem(MaterialItems.BRONZE_DUST, "Bronze Dust");
        addItem(MaterialItems.BRONZE_PLATE, "Bronze Plate");
        addBlock(MaterialBlocks.BRONZE_BLOCK, "Block of Bronze");
    }

    private void addEnergy() {
        addBlock(EnergyBlocks.BATTERIES.get(Tier.BASIC), "Basic Battery");
        addBlock(EnergyBlocks.BATTERIES.get(Tier.ADVANCED), "Advanced Battery");
        addBlock(EnergyBlocks.BATTERIES.get(Tier.ELITE), "Elite Battery");
        addBlock(EnergyBlocks.BATTERIES.get(Tier.ULTIMATE), "Ultimate Battery");
        add(EnergyItems.BATTERY_LOSES_ENERGY, "Loses its stored energy when broken");
    }
}
