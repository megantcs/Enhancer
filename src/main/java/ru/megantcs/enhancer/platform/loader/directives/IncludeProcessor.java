package ru.megantcs.enhancer.platform.loader.preprocessors;

import ru.megantcs.enhancer.platform.loader.LuaPreprocessor;
import ru.megantcs.enhancer.platform.loader.TextPipeline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IncludeProcessor implements TextPipeline.Processor {
    private final LuaPreprocessor preprocessor;
    private final Stack<String> includeStack = new Stack<>();

    public IncludeProcessor(LuaPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    @Override
    public String process(String input) {
        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.startsWith("#include ")) {
                String filename = line.substring(9).replace("\"", "").trim();

                if (includeStack.contains(filename)) {
                    result.append("-- # error: Circular include detected: ").append(filename).append("\n");
                    continue;
                }

                includeStack.push(filename);

                try {
                    String includedContent = Files.readString(Path.of(filename));

                    String processedContent = preprocessor.processCode(includedContent);

                    result.append("-- # include '").append(filename).append("' start\n");
                    result.append(processedContent);
                    result.append("\n-- # include '").append(filename).append("' end\n");

                } catch (IOException e) {
                    result.append("-- # error including '").append(filename).append("': ").append(e.getMessage()).append("\n");
                } finally {
                    includeStack.pop();
                }

            } else {
                result.append(lines[i]).append("\n");
            }
        }

        return result.toString();
    }
}