package ru.megantcs.enhancer.platform.render.engine.common.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.render.engine.common.exceptions.MissingAnnotationException;
import ru.megantcs.enhancer.platform.render.engine.common.storage.LocalStorage;
import ru.megantcs.enhancer.platform.toolkit.Events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.Events.impl.ActionEvent;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;

import java.util.Objects;

public class Module
{
    private final ModuleInfo moduleInfo;
    protected LocalStorage root;
    protected final Logger logger;

    private boolean enable;

    public Module()
    {
        ModuleData data = this.getClass().getAnnotation(ModuleData.class);
        if(data == null) {
            throw new MissingAnnotationException("require annotation 'ModuleData'");
        }

        moduleInfo = ModuleInfo.fromModuleData(data);
        logger = LoggerFactory.getLogger("module@"+data.name());
        onSetup();
    }

    public Module setLocalStorage(LocalStorage storage) {
        root = Objects.requireNonNull(storage, "storage cannot be null");
        return this;
    }

    public ModuleInfo getModuleInfo() {
        return moduleInfo.copy();
    }

    /** protected events **/
    protected void onSetup() {}
    protected void onInitialize() {}
    protected void onShutdown() {}

    /** manage state module **/
    public Module toggle()
    {
        setEnable(!isEnabled());
        return this;
    }

    public void setEnable(boolean value) {
        enable = value;
        updateModuleState();
    }

    public boolean isEnabled() {
        return enable;
    }

    private void updateModuleState()
    {
        if(enable) enabled();
        else disabled();
    }

    /** other */
    private void enabled() {
        onInitialize();
        ON_ENABLE_EVENT.emit(this);
    }

    private void disabled() {
        onShutdown();
        ON_DISABLE_EVENT.emit(this);
    }

    public final ActionEvent<Module> ON_ENABLE_EVENT = EventFactory.makeActionEventSync();
    public final ActionEvent<Module> ON_DISABLE_EVENT = EventFactory.makeActionEventSync();
}
