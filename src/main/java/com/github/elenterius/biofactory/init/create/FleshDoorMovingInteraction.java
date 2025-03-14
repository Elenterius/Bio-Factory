package com.github.elenterius.biofactory.init.create;

import com.github.elenterius.biomancy.block.FleshDoorBlock;
import com.github.elenterius.biomancy.init.ModSoundEvents;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

class FleshDoorMovingInteraction extends SimpleBlockMovingInteraction {

	@Override
	protected BlockState handle(@Nullable Player player, Contraption contraption, BlockPos pos, BlockState blockState) {
		if (!(blockState.getBlock() instanceof FleshDoorBlock)) return blockState;

		BlockPos otherPos = blockState.getValue(FleshDoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
		StructureTemplate.StructureBlockInfo info = contraption.getBlocks().get(otherPos);
		if (info.state().hasProperty(FleshDoorBlock.OPEN)) {
			BlockState newState = info.state().cycle(FleshDoorBlock.OPEN);
			setContraptionBlockData(contraption.entity, otherPos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
		}

		blockState = blockState.cycle(FleshDoorBlock.OPEN);

		if (player != null) {
			boolean isDoorOpen = Boolean.TRUE.equals(blockState.getValue(FleshDoorBlock.OPEN));
			SoundEvent soundEvent = isDoorOpen ? ModSoundEvents.FLESH_DOOR_OPEN.get() : ModSoundEvents.FLESH_DOOR_CLOSE.get();
			float pitch = player.level().random.nextFloat() * 0.1f + 0.9f;
			playSound(player, soundEvent, pitch);
		}

		return blockState;
	}

	@Override
	protected boolean updateColliders() {
		return true;
	}

}
