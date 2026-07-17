package dev.mostlyharmless.mdrdoorsaddon.event;

import dev.mostlyharmless.mdrdoorsaddon.MdrDoorsAddonMod;
import dev.mostlyharmless.mdrdoorsaddon.registry.VanillaReplacementRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = MdrDoorsAddonMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CreativeTabHandler {
    private CreativeTabHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        swapVanillaDoorItemsInTab(event.getEntries());
    }

    private static void swapVanillaDoorItemsInTab(
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries
    ) {
        List<Map.Entry<ItemStack, CreativeModeTab.TabVisibility>> orderedEntries = new ArrayList<>();
        for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : entries) {
            orderedEntries.add(entry);
        }

        for (int index = orderedEntries.size() - 1; index >= 0; index--) {
            ItemStack vanillaStack = orderedEntries.get(index).getKey();
            Item vanillaItem = vanillaStack.getItem();

            if (!VanillaReplacementRegistry.isVanillaReplacementItem(vanillaItem)) {
                continue;
            }

            Item replacementItem = VanillaReplacementRegistry.getReplacementItem(vanillaItem);
            if (replacementItem == null) {
                continue;
            }

            CreativeModeTab.TabVisibility visibility = orderedEntries.get(index).getValue();
            ItemStack anchor = index > 0 ? orderedEntries.get(index - 1).getKey() : null;
            ItemStack replacementStack = new ItemStack(replacementItem);

            entries.remove(vanillaStack);

            if (anchor != null && entries.contains(anchor)) {
                entries.putAfter(anchor, replacementStack, visibility);
            } else {
                entries.put(replacementStack, visibility);
            }
        }
    }
}
