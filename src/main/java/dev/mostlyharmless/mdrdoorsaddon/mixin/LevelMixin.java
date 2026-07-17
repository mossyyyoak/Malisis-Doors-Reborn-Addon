package dev.mostlyharmless.mdrdoorsaddon.mixin;

import dev.mostlyharmless.mdrdoorsaddon.replacement.BlockReplacementHooks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Level.class)
public abstract class LevelMixin {
    @ModifyVariable(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("HEAD"),
            argsOnly = true,
            index = 2
    )
    private BlockState mdrdoorsaddon$replaceBlockStateWithMdrFourArg(BlockState state) {
        return BlockReplacementHooks.onSetBlock(state);
    }

    @ModifyVariable(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            at = @At("HEAD"),
            argsOnly = true,
            index = 2
    )
    private BlockState mdrdoorsaddon$replaceBlockStateWithMdrThreeArg(BlockState state) {
        return BlockReplacementHooks.onSetBlock(state);
    }
}
