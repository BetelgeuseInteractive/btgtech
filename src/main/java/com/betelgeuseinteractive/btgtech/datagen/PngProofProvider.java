package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;

/**
 * Demonstration for WP-004 (ADR 0048), reverted after the proof: reads a base texture and a grayscale tier mask from
 * the existing resources, tints the masked area and writes the result as a PNG through the data generator cache.
 */
final class PngProofProvider implements DataProvider {
    private static final Identifier BASE = texture("png_proof_base");
    private static final Identifier MASK = texture("png_proof_mask");
    // Demonstration color only; tier accent colors are chosen in WP-005.
    private static final int ACCENT = 0xFFE07B39;

    private final PackOutput output;
    private final ResourceManager resources;

    PngProofProvider(PackOutput output, ResourceManager resources) {
        this.output = output;
        this.resources = resources;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        try (NativeImage base = read(BASE);
                NativeImage mask = read(MASK);
                NativeImage result = new NativeImage(base.getWidth(), base.getHeight(), false)) {
            for (int y = 0; y < base.getHeight(); y++) {
                for (int x = 0; x < base.getWidth(); x++) {
                    int pixel = base.getPixel(x, y);
                    float weight = ARGB.red(mask.getPixel(x, y)) / 255.0F;
                    result.setPixel(x, y, ARGB.srgbLerp(weight, pixel, ARGB.multiply(pixel, ACCENT)));
                }
            }
            Path target = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(BTGTech.MOD_ID)
                    .resolve("textures/block/png_proof_tinted.png");
            byte[] png = encode(result);
            cache.writeIfNeeded(target, png, Hashing.sha1().hashBytes(png));
            return CompletableFuture.completedFuture(null);
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public String getName() {
        return "PNG proof";
    }

    private NativeImage read(Identifier location) throws IOException {
        try (InputStream stream = resources.open(location)) {
            return NativeImage.read(stream);
        }
    }

    // NativeImage writes PNG files only; the bytes are needed for the data generator cache.
    private static byte[] encode(NativeImage image) throws IOException {
        Path file = Files.createTempFile(BTGTech.MOD_ID, ".png");
        try {
            image.writeToFile(file);
            return Files.readAllBytes(file);
        } finally {
            Files.delete(file);
        }
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "textures/block/" + name + ".png");
    }
}
