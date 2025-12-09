package ru.megantcs.enhancer.platform.toolkit.Fabric;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;

public abstract class ModEntrypoint implements Minecraft, ModInitializer
{
    private final String modificationName;
    protected final Logger LOGGER;

    protected ModEntrypoint(String modName) {
        this.modificationName = modName;
        LOGGER = LoggerFactory.getLogger(modName);
    }

    @Override
    public final void onInitialize()
    {
        bootstrap();
    }

    public abstract void bootstrap();

    public String getModificationName() {
        return modificationName;
    }
}
