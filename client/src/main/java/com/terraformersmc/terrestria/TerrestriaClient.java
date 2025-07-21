package com.terraformersmc.terrestria;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terrestria.init.TerrestriaBlocks;
import com.terraformersmc.terrestria.tag.TerrestriaBlockTags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

// This class is an entrypoint
@Environment(EnvType.CLIENT)
public class TerrestriaClient implements ClientModInitializer {
	private static final BlockRenderLayer GRASS_BLOCK_LAYER = BlockRenderLayer.CUTOUT_MIPPED;
	private static final BlockRenderLayer PLANT_BLOCK_LAYER = BlockRenderLayer.CUTOUT;
	private static final BlockRenderLayer DOOR_BLOCK_LAYER = BlockRenderLayer.CUTOUT;

	private static final BlockColorProvider FOLIAGE_BLOCK_COLORS =
			(block, world, pos, layer) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.DEFAULT;
	private static final BlockColorProvider GRASS_BLOCK_COLORS =
			(block, world, pos, layer) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5, 1.0);

	@Override
	public void onInitializeClient() {
		// Load the client config if it hasn't been loaded already
		Terrestria.getConfigManager().getClientConfig();
	}

	private void registerEntityRenderers() {
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "redwood"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "hemlock"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "rubber"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "cypress"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "willow"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "japanese_maple"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "rainbow_eucalyptus"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "sakura"));
		TerraformBoatClientHelper.registerModelLayers(Identifier.of(Terrestria.MOD_ID, "yucca_palm"));
	}

	private void addColoredGrass(Block grass) {
		BlockRenderLayerMap.putBlock(grass, GRASS_BLOCK_LAYER);
		ColorProviderRegistry.BLOCK.register(GRASS_BLOCK_COLORS, grass);
	}
}
