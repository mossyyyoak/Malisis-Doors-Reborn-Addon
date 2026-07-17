package dev.mostlyharmless.mdrdoorsaddon.replacement;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Copies shared block state properties from a vanilla block state onto a Malisis Doors Reborn block.
 * MDR vanilla-style doors, trapdoors, and fence gates extend the same vanilla block classes.
 */
public final class BlockStateConverter {
    private BlockStateConverter() {
    }

    public static BlockState convert(BlockState source, net.minecraft.world.level.block.Block targetBlock) {
        BlockState result = targetBlock.defaultBlockState();

        for (Property<?> property : source.getProperties()) {
            if (result.hasProperty(property)) {
                result = copyProperty(result, source, property);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState copyProperty(
            BlockState target,
            BlockState source,
            Property<?> property
    ) {
        Property<T> typed = (Property<T>) property;
        return target.setValue(typed, source.getValue(typed));
    }
}
