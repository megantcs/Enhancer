package ru.megantcs.enhancer.platform.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.directives.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LuaPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger("Lua Preprocessor");
    private final TextPipeline pipeline = new TextPipeline();
    private final Map<String, DefineProcessor.MacroDefinition> defines = new HashMap<>();
    private final Map<String, Function<String, String>> pragmaHandlers = new HashMap<>();
    private int index = 0;
    private int filesIndex = 0;

    public LuaPreprocessor() {
        pipeline.addProcessor(new IncludeProcessor(this));
        pipeline.addProcessor(new DefineProcessor(defines, this));
        pipeline.addProcessor(new ConditionalProcessor(defines, this));
        pipeline.addProcessor(new ReplaceProcessor(defines));
        pipeline.addProcessor(new TernaryConverter());
        pipeline.addProcessor(new PragmaProcessor(pragmaHandlers));
    }

    public void addFileIndex(int i) {
        filesIndex += i;
    }

    public int getFilesIndex() {
        return filesIndex;
    }

    public void addIndex(int i) {
        index += i;
    }

    public void setIndex(int i) {
        index = i;
    }

    public int getIndex() {
        return index;
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
        defines.put(name, new DefineProcessor.MacroDefinition(name, null, value, true, 0));
    }

    public void addMacro(String name, String[] parameters, String body) {
        defines.put(name, new DefineProcessor.MacroDefinition(name, parameters, body, true,0));
    }

    public void addFlag(String name) {
        defines.put(name, new DefineProcessor.MacroDefinition(name, null, "", false, 0));
    }

    public void removeDefine(String name) {
        defines.remove(name);
    }

    public boolean isDefined(String name) {
        return defines.containsKey(name);
    }
}