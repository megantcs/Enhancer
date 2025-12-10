package ru.megantcs.enhancer.platform.loader.preprocessors;

import ru.megantcs.enhancer.platform.loader.TextPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class DefineProcessor implements TextPipeline.Processor {
    private static final Logger LOG = LoggerFactory.getLogger(DefineProcessor.class);
    final Map<String, MacroDefinition> defines;

    public static class MacroDefinition {
        public final String name;
        public final String[] parameters;
        public final String body;
        public final boolean hasValue;

        public MacroDefinition(String name, String[] parameters, String body, boolean hasValue) {
            this.name = name;
            this.parameters = parameters;
            this.body = body;
            this.hasValue = hasValue;
        }

        public boolean hasParameters() {
            return parameters != null && parameters.length > 0;
        }
    }

    public DefineProcessor(Map<String, MacroDefinition> defines) {
        this.defines = defines;
    }

    @Override
    public String process(String input) {


        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.startsWith("#define ")) {
                String content = line.substring(8).trim();

                StringBuilder macroLines = new StringBuilder();
                macroLines.append(content);

                while (i < lines.length - 1 && lines[i].trim().endsWith("\\")) {
                    i++;
                    String nextLine = lines[i].trim();
                    macroLines.setLength(macroLines.length() - 1);
                    macroLines.append(" ").append(nextLine);
                }

                String fullMacro = macroLines.toString().replace("\\\n", " ").replace("\\\r\n", " ");


                MacroDefinition macro = parseMacro(fullMacro);
                if (macro != null) {
                    defines.put(macro.name, macro);

                }
            } else {
                result.append(lines[i]).append("\n");
            }
        }


        return result.toString();
    }

    private MacroDefinition parseMacro(String macro) {
        macro = macro.trim();

        int firstSpace = macro.indexOf(' ');
        if (firstSpace == -1) {
            return new MacroDefinition(macro, null, "", false);
        }

        String namePart;
        String bodyPart;

        if (macro.contains("(") && macro.indexOf('(') < firstSpace) {
            int openParen = macro.indexOf('(');
            int closeParen = findMatchingParenthesis(macro, openParen);

            if (closeParen > openParen) {
                namePart = macro.substring(0, closeParen + 1);
                bodyPart = macro.substring(closeParen + 1).trim();

                String macroName = namePart.substring(0, openParen);
                String paramsStr = namePart.substring(openParen + 1, closeParen);

                String[] parameters = paramsStr.isEmpty() ? new String[0] : paramsStr.split("\\s*,\\s*");
                return new MacroDefinition(macroName, parameters, bodyPart, true);
            }
        }

        namePart = macro.substring(0, firstSpace);
        bodyPart = macro.substring(firstSpace + 1).trim();

        return new MacroDefinition(namePart, null, bodyPart, true);
    }

    private int findMatchingParenthesis(String str, int openPos) {
        int count = 1;
        for (int i = openPos + 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}