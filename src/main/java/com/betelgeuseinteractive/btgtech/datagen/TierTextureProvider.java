package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;

/**
 * Tier texture variants (ADR 0048). Each listed base texture is drawn with the Basic tier tones; its tier mask is white
 * where the tier color belongs and black elsewhere. For every tier, the Basic tones under the mask take the tones of
 * that tier (art style guide, section 4).
 */
final class TierTextureProvider implements DataProvider {
    private static final List<BaseTexture> BASE_TEXTURES = List.of(
            // Machine screens
            new BaseTexture("gui/sprites/machine/panel", EnumSet.allOf(Tier.class)),
            // Crafting components; the other tiers follow with their components in WP-301
            new BaseTexture("block/machine_casing", EnumSet.of(Tier.BASIC)),
            new BaseTexture("item/circuit", EnumSet.of(Tier.BASIC)),
            // Energy
            new BaseTexture("block/battery_front", EnumSet.allOf(Tier.class)),
            new BaseTexture("block/battery_front_active", EnumSet.allOf(Tier.class)),
            new BaseTexture("block/battery_side", EnumSet.allOf(Tier.class)),
            new BaseTexture("block/battery_top", EnumSet.allOf(Tier.class)));

    private static final int MASK_OFF = 0x000000;
    private static final int MASK_ON = 0xFFFFFF;

    private final PackOutput output;
    private final ResourceManager resources;

    TierTextureProvider(PackOutput output, ResourceManager resources) {
        this.output = output;
        this.resources = resources;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        try {
            for (BaseTexture baseTexture : BASE_TEXTURES) {
                writeVariants(cache, baseTexture.path(), baseTexture.tiers());
            }
            return CompletableFuture.completedFuture(null);
        } catch (IOException | IllegalArgumentException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public String getName() {
        return "Tier textures";
    }

    private void writeVariants(CachedOutput cache, String baseTexture, Set<Tier> tiers) throws IOException {
        try (NativeImage base = read(baseTexture);
                NativeImage mask = read(baseTexture + "_tier_mask")) {
            if (base.getWidth() != mask.getWidth() || base.getHeight() != mask.getHeight()) {
                throw new IllegalArgumentException("The tier mask of " + baseTexture + " differs in size");
            }
            byte[] metadata = metadata(baseTexture);
            for (Tier tier : tiers) {
                Path target = target(baseTexture, tier);
                try (NativeImage variant = recolor(baseTexture, base, mask, tier)) {
                    byte[] png = encode(variant);
                    cache.writeIfNeeded(target, png, sha1(png));
                }
                if (metadata != null) {
                    cache.writeIfNeeded(
                            target.resolveSibling(target.getFileName() + ".mcmeta"), metadata, sha1(metadata));
                }
            }
        }
    }

    // The hand-made metadata of the base texture (an animation), which every variant needs as well.
    private byte @Nullable [] metadata(String baseTexture) throws IOException {
        Optional<Resource> resource =
                resources.getResource(location(baseTexture).withSuffix(".mcmeta"));
        if (resource.isEmpty()) {
            return null;
        }
        try (InputStream stream = resource.get().open()) {
            return stream.readAllBytes();
        }
    }

    private static NativeImage recolor(String baseTexture, NativeImage base, NativeImage mask, Tier tier) {
        NativeImage variant = new NativeImage(base.getWidth(), base.getHeight(), false);
        for (int y = 0; y < base.getHeight(); y++) {
            for (int x = 0; x < base.getWidth(); x++) {
                int pixel = base.getPixel(x, y);
                int maskColor = mask.getPixel(x, y) & 0xFFFFFF;
                if (maskColor == MASK_ON) {
                    pixel = recolorMasked(baseTexture, x, y, pixel, tier);
                } else if (maskColor != MASK_OFF) {
                    throw new IllegalArgumentException(String.format(
                            Locale.ROOT, "Tier mask of %s is neither black nor white at %d, %d", baseTexture, x, y));
                }
                variant.setPixel(x, y, pixel);
            }
        }
        return variant;
    }

    private static int recolorMasked(String baseTexture, int x, int y, int pixel, Tier tier) {
        try {
            return TierRecolor.recolor(pixel, tier);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    String.format(
                            Locale.ROOT, "%s at %d, %d under the tier mask: %s", baseTexture, x, y, e.getMessage()),
                    e);
        }
    }

    private NativeImage read(String texture) throws IOException {
        try (InputStream stream = resources.open(location(texture))) {
            return NativeImage.read(stream);
        }
    }

    // NativeImage writes PNG files only; the data generator cache needs the bytes.
    private static byte[] encode(NativeImage image) throws IOException {
        Path file = Files.createTempFile(BTGTech.MOD_ID, ".png");
        try {
            image.writeToFile(file);
            return Files.readAllBytes(file);
        } finally {
            Files.delete(file);
        }
    }

    // SHA-1 like the vanilla providers; the cache uses the hash only to detect changed output.
    @SuppressWarnings("deprecation")
    private static HashCode sha1(byte[] bytes) {
        return Hashing.sha1().hashBytes(bytes);
    }

    private Path target(String baseTexture, Tier tier) {
        int nameStart = baseTexture.lastIndexOf('/') + 1;
        String variant = baseTexture.substring(0, nameStart) + tier.id() + "_" + baseTexture.substring(nameStart);
        return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(BTGTech.MOD_ID)
                .resolve("textures")
                .resolve(variant + ".png");
    }

    private static Identifier location(String texture) {
        return Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "textures/" + texture + ".png");
    }

    /**
     * A base texture, as a path below {@code assets/btgtech/textures/} without the extension, and the tiers that have a
     * variant of it.
     */
    private record BaseTexture(String path, Set<Tier> tiers) {}
}
