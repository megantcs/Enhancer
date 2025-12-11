package ru.megantcs.enhancer.platform.loader.directives;

import ru.megantcs.enhancer.platform.loader.LuaUtils;
import ru.megantcs.enhancer.platform.loader.TextPipeline;
import ru.megantcs.enhancer.platform.loader.directives.DefineProcessor.MacroDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceProcessor implements TextPipeline.Processor {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceProcessor.class);

    final Map<String, MacroDefinition> defines;
    final Map<String, String> replaceMap;

    public ReplaceProcessor(Map<String, MacroDefinition> defines) {
        this.defines = defines;
        this.replaceMap = new HashMap<>();
        init();
    }

    private void init() {
        replaceMap.putAll(LuaUtils.fixNamesMap());
    }

    @Override
    public String process(String input)
    {
        String result = input;
        boolean changed;
        int maxIterations = 100;
        int iteration = 0;

        for(var key : replaceMap.keySet())
        {
            result = result.replace(key, replaceMap.get(key));
        }

        do {
            changed = false;
            String previous = result;

            for (Map.Entry<String, MacroDefinition> entry : defines.entrySet()) {
                MacroDefinition macro = entry.getValue();

                if (macro.hasValue && macro.hasParameters()) {
                    String pattern = "\\b" + Pattern.quote(macro.name) + "\\s*\\(([^)]*)\\)";

                    Pattern compiled = Pattern.compile(pattern);
                    Matcher matcher = compiled.matcher(result);

                    StringBuffer sb = new StringBuffer();
                    boolean found = false;
                    while (matcher.find()) {
                        found = true;
                        String argsStr = matcher.group(1);
                        String[] args = parseArguments(argsStr);
                        String replacement = expandMacroWithArgs(macro, args);
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
                        changed = true;
                    }
                    if (found) {
                        matcher.appendTail(sb);
                        result = sb.toString();
                    }
                }
            }

            for (Map.Entry<String, MacroDefinition> entry : defines.entrySet()) {
                MacroDefinition macro = entry.getValue();

                if (macro.hasValue && !macro.hasParameters()) {
                    String pattern = "\\b" + Pattern.quote(macro.name) + "\\b";
                    String replacement = macro.body;

                    String newResult = result.replaceAll(pattern, Matcher.quoteReplacement(replacement));
                    if (!newResult.equals(result)) {
                        result = newResult;
                        changed = true;

                    }
                }
            }

            iteration++;
            if (iteration >= maxIterations) {
                LOG.error("Max iterations reached in replace processor");
                break;
            }
        } while (changed);

        return result;
    }

    private String[] parseArguments(String argsStr) {
        if (argsStr.trim().isEmpty()) {
            return new String[0];
        }

        java.util.List<String> args = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenDepth = 0;

        for (char c : argsStr.toCharArray()) {
            if (c == '(') {
                parenDepth++;
                current.append(c);
            } else if (c == ')') {
                parenDepth--;
                current.append(c);
            } else if (c == ',' && parenDepth == 0) {
                args.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            args.add(current.toString().trim());
        }

        return args.toArray(new String[0]);
    }

    private String expandMacroWithArgs(MacroDefinition macro, String[] args) {
        String result = macro.body;

        if (macro.parameters != null) {


            for (int i = 0; i < Math.min(macro.parameters.length, args.length); i++) {
                String param = macro.parameters[i].trim();
                String arg = args[i];

                result = result.replaceAll("\\b" + Pattern.quote(param) + "\\b", arg);
            }
        }

        return result;
    }
}