package com.github.elenterius.biofactory.init;

import com.github.elenterius.biofactory.BioFactoryMod;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BioFactoryMod.MOD_ID);

	private ModBlocks() {}

	private static <T extends Block> RegistryObject<T> register(String name, Function<BlockBehaviour.Properties, T> factory) {
		return BLOCKS.register(name, () -> factory.apply(Properties.of()));
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> factory) {
		return BLOCKS.register(name, factory);
	}

	public static BlockBehaviour.Properties copyProperties(BlockBehaviour behaviour) {
		return BlockBehaviour.Properties.copy(behaviour);
	}

	public static boolean neverValid(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> entityType) {
		return false;
	}

	public static boolean neverValid(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}

}
