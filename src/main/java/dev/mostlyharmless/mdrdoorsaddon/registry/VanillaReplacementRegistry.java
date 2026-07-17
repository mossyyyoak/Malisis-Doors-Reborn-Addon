package dev.mostlyharmless.mdrdoorsaddon.registry;

import com.mojang.logging.LogUtils;
import dev.mostlyharmless.mdrdoorsaddon.MdrDoorsAddonMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps vanilla door, trapdoor, and fence gate blocks to their Malisis Doors Reborn equivalents.
 * MDR registers vanilla-style blocks under the same path in the {@code malisisdoorsreborn} namespace.
 */
public final class VanillaReplacementRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String MDR_NAMESPACE = "malisisdoorsreborn";

    private static final Block[] VANILLA_DOORS = {
            Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR,
            Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.MANGROVE_DOOR, Blocks.CHERRY_DOOR,
            Blocks.BAMBOO_DOOR, Blocks.CRIMSON_DOOR, Blocks.WARPED_DOOR, Blocks.IRON_DOOR
    };

    private static final Block[] VANILLA_TRAPDOORS = {
            Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR,
            Blocks.ACACIA_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
            Blocks.BAMBOO_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR, Blocks.WARPED_TRAPDOOR, Blocks.IRON_TRAPDOOR
    };

    private static final Block[] VANILLA_FENCE_GATES = {
            Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE,
            Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.CHERRY_FENCE_GATE,
            Blocks.BAMBOO_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE
    };

    private static final Map<Block, Block> BLOCK_REPLACEMENTS = new HashMap<>();
    private static final Set<Block> VANILLA_BLOCKS = new HashSet<>();
    private static final Set<Item> VANILLA_ITEMS = new HashSet<>();

    private static boolean initialized;

    private VanillaReplacementRegistry() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        registerGroup(VANILLA_DOORS);
        registerGroup(VANILLA_TRAPDOORS);
        registerGroup(VANILLA_FENCE_GATES);

        initialized = true;

        if (BLOCK_REPLACEMENTS.isEmpty()) {
            LOGGER.error("[{}] No Malisis Doors Reborn replacement blocks were found. Is malisisdoorsreborn loaded?",
                    MdrDoorsAddonMod.MOD_ID);
        }
    }

    private static void registerGroup(Block[] vanillaBlocks) {
        for (Block vanillaBlock : vanillaBlocks) {
            ResourceLocation vanillaId = BuiltInRegistries.BLOCK.getKey(vanillaBlock);
            ResourceLocation mdrId = ResourceLocation.fromNamespaceAndPath(MDR_NAMESPACE, vanillaId.getPath());

            var mdrBlockOptional = BuiltInRegistries.BLOCK.getOptional(mdrId);
            if (mdrBlockOptional.isEmpty()) {
                LOGGER.warn("[{}] Missing Malisis Doors Reborn block for vanilla block {}",
                        MdrDoorsAddonMod.MOD_ID, vanillaId);
                continue;
            }

            Block mdrBlock = mdrBlockOptional.get();
            if (!isSupportedReplacement(mdrBlock)) {
                LOGGER.warn("[{}] Malisis Doors Reborn block {} is not a door, trapdoor, or fence gate",
                        MdrDoorsAddonMod.MOD_ID, mdrId);
                continue;
            }

            BLOCK_REPLACEMENTS.put(vanillaBlock, mdrBlock);
            VANILLA_BLOCKS.add(vanillaBlock);

            Item vanillaItem = vanillaBlock.asItem();
            if (vanillaItem instanceof BlockItem) {
                VANILLA_ITEMS.add(vanillaItem);
            }
        }
    }

    private static boolean isSupportedReplacement(Block block) {
        return block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock;
    }

    public static int mappingCount() {
        return BLOCK_REPLACEMENTS.size();
    }

    public static BlockState replaceState(BlockState state) {
        if (!initialized) {
            initialize();
        }

        Block replacement = BLOCK_REPLACEMENTS.get(state.getBlock());
        if (replacement == null) {
            return state;
        }

        return dev.mostlyharmless.mdrdoorsaddon.replacement.BlockStateConverter.convert(state, replacement);
    }

    public static boolean isVanillaReplacementBlock(Block block) {
        if (!initialized) {
            initialize();
        }
        return VANILLA_BLOCKS.contains(block);
    }

    public static Item getReplacementItem(Item vanillaItem) {
        if (!initialized) {
            initialize();
        }

        if (!(vanillaItem instanceof BlockItem blockItem)) {
            return null;
        }

        Block replacement = BLOCK_REPLACEMENTS.get(blockItem.getBlock());
        if (replacement == null) {
            return null;
        }

        Item replacementItem = replacement.asItem();
        return replacementItem == net.minecraft.world.item.Items.AIR ? null : replacementItem;
    }

    public static boolean isVanillaReplacementItem(Item item) {
        if (!initialized) {
            initialize();
        }
        return VANILLA_ITEMS.contains(item);
    }

    public static Set<Block> vanillaBlocks() {
        if (!initialized) {
            initialize();
        }
        return Collections.unmodifiableSet(VANILLA_BLOCKS);
    }
}
