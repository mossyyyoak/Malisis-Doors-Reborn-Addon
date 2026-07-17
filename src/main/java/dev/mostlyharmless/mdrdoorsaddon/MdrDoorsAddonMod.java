package dev.mostlyharmless.mdrdoorsaddon;

import com.mojang.logging.LogUtils;
import dev.mostlyharmless.mdrdoorsaddon.event.CreativeTabHandler;
import dev.mostlyharmless.mdrdoorsaddon.registry.VanillaReplacementRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MdrDoorsAddonMod.MOD_ID)
public class MdrDoorsAddonMod {
    public static final String MOD_ID = "mdrdoorsaddon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MdrDoorsAddonMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(CreativeTabHandler::onBuildCreativeTabContents);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            VanillaReplacementRegistry.initialize();
            LOGGER.info("Malisis Doors Reborn vanilla replacement mappings loaded: {} block pairs",
                    VanillaReplacementRegistry.mappingCount());
        });
    }
}
