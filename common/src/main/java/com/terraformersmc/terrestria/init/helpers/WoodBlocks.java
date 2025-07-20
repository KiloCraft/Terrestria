package com.terraformersmc.terrestria.init.helpers;

import com.terraformersmc.terraform.leaves.api.block.ColoredParticleLeavesBlock;
import com.terraformersmc.terraform.leaves.api.block.ExtendedLeavesBlock;
import com.terraformersmc.terraform.leaves.api.block.LeafPileBlock;
import com.terraformersmc.terraform.wood.api.block.PillarLogHelper;
import com.terraformersmc.terrestria.Terrestria;
import com.terraformersmc.terrestria.block.TerrestriaOptiLeavesBlock;
import com.terraformersmc.terrestria.init.TerrestriaBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;

import java.util.Optional;

// TODO: Consider reverting to a record with builder object to enable config of things like
//       BlockSetType, WoodType, flammability, and simplify the 'has', 'is, 'uses' args.
public class WoodBlocks {
	private final String name;
	private final Identifier id;
	private final WoodColors colors;
	private final LogSize size;

	public final BlockSetType blockSetType;
	public final WoodType woodType;

	private final boolean tintable;

	public final Block log;
	public final Block wood;
	public final Block leaves;
	public final Block strippedLog;
	public final Block strippedWood;

	private WoodBlocks(String name, WoodColors colors, LogSize size, boolean hasLeafPile, boolean hasQuarterLog, boolean usesExtendedLeaves, boolean isTintable) {
		this.tintable = isTintable;

		this.name = name;
		this.id = Identifier.of(Terrestria.MOD_ID, name);
		this.colors = colors;
		this.size = size;

		this.blockSetType = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(id);
		this.woodType = WoodTypeBuilder.copyOf(WoodType.OAK).register(id, this.blockSetType);

		// register manufactured blocks

		if (usesExtendedLeaves) {
			leaves = TerrestriaRegistry.register(name + "_leaves", TerrestriaOptiLeavesBlock::new, AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(colors.leaves).allowsSpawning(TerrestriaBlocks::canSpawnOnLeaves).suffocates(TerrestriaBlocks::never).blockVision(TerrestriaBlocks::never));
		} else {
			if (tintable) {
				leaves = TerrestriaRegistry.register(name + "_leaves", settings -> new TintedParticleLeavesBlock(0.01f, settings), AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(colors.leaves).allowsSpawning(TerrestriaBlocks::canSpawnOnLeaves).suffocates(TerrestriaBlocks::never).blockVision(TerrestriaBlocks::never));
			} else {
				leaves = TerrestriaRegistry.register(name + "_leaves", settings -> new ColoredParticleLeavesBlock(0.01f, colors.leaves.color, settings), AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(colors.leaves).allowsSpawning(TerrestriaBlocks::canSpawnOnLeaves).suffocates(TerrestriaBlocks::never).blockVision(TerrestriaBlocks::never));
			}
		}


		log = TerrestriaRegistry.register(name + "_log", PillarBlock::new, PillarLogHelper.createSettings(colors.planks, colors.bark));
		strippedLog = TerrestriaRegistry.register("stripped_" + name + "_log", PillarBlock::new, PillarLogHelper.createSettings(colors.planks));

		wood = TerrestriaRegistry.register(name + "_wood", PillarBlock::new, PillarLogHelper.createSettings(colors.bark));
		strippedWood = TerrestriaRegistry.register("stripped_" + name + "_wood", PillarBlock::new, PillarLogHelper.createSettings(colors.planks));
	}

	public static WoodBlocks register(String name, WoodColors colors, LogSize size, boolean hasLeafPile, boolean hasQuarteredLog, boolean usesExtendedLeaves, boolean isTintable) {
		WoodBlocks blocks = new WoodBlocks(name, colors, size, hasLeafPile, hasQuarteredLog, usesExtendedLeaves, isTintable);

		blocks.addFlammables();
		blocks.addStrippables();

		return blocks;
	}

	public static WoodBlocks register(String name, WoodColors colors, LogSize size) {
		return register(name, colors, size, false, false, false, true);
	}

	public static WoodBlocks register(String name, WoodColors colors) {
		return register(name, colors, LogSize.NORMAL);
	}

	private void addFlammables() {
		FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();

		// tree
		flammableRegistry.add(log, 5, 5);
		flammableRegistry.add(strippedLog, 5, 5);
		if (hasWood()) {
			flammableRegistry.add(wood, 5, 5);
			flammableRegistry.add(strippedWood, 5, 5);
		}

		flammableRegistry.add(leaves, 30, 60);
	}

	private void addStrippables() {
		if (log != null && strippedLog != null) {
			StrippableBlockRegistry.register(log, strippedLog);
		}
		if (wood != null && strippedWood != null) {
			StrippableBlockRegistry.register(wood, strippedWood);
		}
	}

	public String getName() {
		return name;
	}

	public Identifier getId() {
		return id;
	}

	public WoodColors getColors() {
		return colors;
	}

	public LogSize getSize() {
		return size;
	}

	public boolean hasQuarterLog() {
		return false;
	}

	public boolean hasLeafPile() {
		return false;
	}

	public boolean hasWood() {
		return (wood != null && strippedWood != null);
	}

	public boolean isTintable() {
		return tintable;
	}

	public enum LogSize {
		NORMAL("normal"),
		SMALL("small");

		private final String name;

		LogSize(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}
