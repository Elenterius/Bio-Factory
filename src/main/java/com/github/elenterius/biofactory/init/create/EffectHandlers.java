package com.github.elenterius.biofactory.init.create;

import com.github.elenterius.biofactory.init.ModFluids;
import com.github.elenterius.biomancy.statuseffect.StatusEffectHandler;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

public final class EffectHandlers {

	private EffectHandlers(){ }

	static void register() {
		OpenPipeEffectHandler.REGISTRY.register(com.github.elenterius.biomancy.init.ModFluids.ACID.get().getSource(), new AcidEffectHandler());
		OpenPipeEffectHandler.REGISTRY.register(ModFluids.NUTRIENTS_FLUID.get().getSource(), new NutrientsEffectHandler());
	}

	private static class AcidEffectHandler implements OpenPipeEffectHandler {

		@Override
		public void apply(Level level, AABB area, FluidStack fluid) {
			if (level.getGameTime() % 5 != 0)
				return;

			List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class, area, livingEntity -> StatusEffectHandler
					.canApplyAcidEffect(livingEntity, StatusEffectHandler.CONSUME_ONE_NUTRIENT_PER_ARMOR_PIECE));
			for (LivingEntity mob : mobs) {
				StatusEffectHandler.applyCorrosiveEffect(mob, 4);
			}

			BlockPos.betweenClosedStream(area).forEach(pos -> corrodeCopper(level, pos));
		}

		private void corrodeCopper(Level level, BlockPos pos) {
			if (level.random.nextFloat() >= 0.057f) {return;}

			BlockState blockState = level.getBlockState(pos);
			Block block = blockState.getBlock();
			if (block instanceof WeatheringCopper weatheringCopper && WeatheringCopper.getNext(block).isPresent()) {
				weatheringCopper.getNext(blockState).ifPresent(state -> level.setBlockAndUpdate(pos, state));
			}
		}

	}

	private static class NutrientsEffectHandler implements OpenPipeEffectHandler {

		@Override
		public void apply(Level level, AABB area, FluidStack fluid) {
			if (level.getGameTime() % 5 != 0)
				return;

			List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
			for (LivingEntity entity : entities) {
				entity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 7, 0, false, false, false));
			}
		}
	}
}
