package ru.megantcs.enhancer.platform.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.preprocessors.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LuaPreprocessor {
    private static final Logger LOG = LoggerFactory.getLogger("Lua Preprocessor");
    private final TextPipeline pipeline = new TextPipeline();
    private final Map<String, DefineProcessor.MacroDefinition> defines = new HashMap<>();
    private final Map<String, Function<String, String>> pragmaHandlers = new HashMap<>();

    public LuaPreprocessor() {
        pipeline.addProcessor(new DefineProcessor(defines));
        pipeline.addProcessor(new ConditionalProcessor(defines));
        pipeline.addProcessor(new ReplaceProcessor(defines));
        pipeline.addProcessor(new TernaryConverter());
        pipeline.addProcessor(new IncludeProcessor());
        pipeline.addProcessor(new PragmaProcessor(pragmaHandlers));
    }

    public void registerPragmaCommand(String pragmaName, Function<String, String> handler) {
        pragmaHandlers.put(pragmaName, handler);
    }

    public String processCode(String code) {
        return pipeline.process(code);
    }

    public void addProcessor(TextPipeline.Processor processor) {
        pipeline.addProcessor(processor);
    }

    public void addDefine(String name, String value) {
        defines.put(name, new DefineProcessor.MacroDefinition(name, null, value, true));
    }

    public void addMacro(String name, String[] parameters, String body) {
        defines.put(name, new DefineProcessor.MacroDefinition(name, parameters, body, true));
    }

    public void addFlag(String name) {
        defines.put(name, new DefineProcessor.MacroDefinition(name, null, "", false));
    }

    public void removeDefine(String name) {
        defines.remove(name);
    }

    public boolean isDefined(String name) {
        return defines.containsKey(name);
    }
}