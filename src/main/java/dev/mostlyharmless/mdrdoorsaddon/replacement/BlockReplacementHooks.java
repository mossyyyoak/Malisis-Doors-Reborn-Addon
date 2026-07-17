package dev.mostlyharmless.mdrdoorsaddon.replacement;

import dev.mostlyharmless.mdrdoorsaddon.registry.VanillaReplacementRegistry;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockReplacementHooks {
    private BlockReplacementHooks() {
    }

    public static BlockState onSetBlock(BlockState state) {
        return VanillaReplacementRegistry.replaceState(state);
    }
}
