package com.betelgeuseinteractive.btgtech.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Required architecture rules of {@code docs/development/testing.md}, section 2.
 *
 * <p>Rules about packages that do not exist yet allow an empty selection, so they pass until the first class arrives
 * and protect it from then on. External annotations are referenced by name because the integrated mods are not on the
 * test classpath.
 */
@AnalyzeClasses(packagesOf = BTGTech.class, importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
    private static final String ROOT = "com.betelgeuseinteractive.btgtech";

    @ArchTest
    static final ArchRule CLIENT_CODE_IS_ISOLATED = noClasses()
            .that()
            .resideOutsideOfPackages("..client..", ROOT + ".datagen..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..client..")
            .because("client-only code must never be reachable from common code (ADR 0009, 0018, 0075)");

    @ArchTest
    static final ArchRule ONLY_CLIENT_CODE_REFERENCES_DATA_GENERATION = noClasses()
            .that()
            .resideOutsideOfPackages("..client..", ROOT + ".datagen..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage(ROOT + ".datagen..")
            .because("data generation runs only in the client data run and may use client classes (ADR 0075)");

    @ArchTest
    static final ArchRule LOGIC_IS_INDEPENDENT_OF_MINECRAFT = noClasses()
            .that()
            .resideInAPackage("..logic..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("net.minecraft..", "net.neoforged..")
            .because("logic packages hold Minecraft-independent, unit-tested code (ADR 0018)")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule CORE_IS_INDEPENDENT_OF_FEATURES = noClasses()
            .that()
            .resideInAPackage(ROOT + ".core..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    ROOT + ".energy..",
                    ROOT + ".machine..",
                    ROOT + ".storage..",
                    ROOT + ".logistics..",
                    ROOT + ".material..",
                    ROOT + ".tool..")
            .because("core never depends on a feature package (ADR 0023)")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule TOP_LEVEL_PACKAGES_ARE_FREE_OF_CYCLES = slices().matching(ROOT + ".(*)..")
            .should()
            .beFreeOfCycles()
            .because("cyclic dependencies between features are forbidden (ADR 0023)");

    @ArchTest
    static final ArchRule ONLY_JEI_INTEGRATION_REFERENCES_JEI = noClasses()
            .that()
            .resideOutsideOfPackage(ROOT + ".compat.jei..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("mezz.jei..")
            .because("integrations are optional and isolated in compat.<modid> (ADR 0058)");

    @ArchTest
    static final ArchRule ONLY_JADE_INTEGRATION_REFERENCES_JADE = noClasses()
            .that()
            .resideOutsideOfPackage(ROOT + ".compat.jade..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("snownee.jade..")
            .because("integrations are optional and isolated in compat.<modid> (ADR 0058)");

    @ArchTest
    static final ArchRule EVENT_BUS_SUBSCRIBER_IS_NOT_USED = noClasses()
            .should()
            .beAnnotatedWith("net.neoforged.fml.common.EventBusSubscriber")
            .because("event listeners are registered explicitly in bootstrap classes (ADR 0022)");

    @ArchTest
    static final ArchRule PLUGIN_ANNOTATIONS_ONLY_IN_COMPAT = noClasses()
            .that()
            .resideOutsideOfPackage(ROOT + ".compat..")
            .should()
            .beAnnotatedWith("mezz.jei.api.JeiPlugin")
            .orShould()
            .beAnnotatedWith("snownee.jade.api.WailaPlugin")
            .because("annotation-based plugin discovery is allowed only in integrations (ADR 0058)");
}
