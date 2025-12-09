package ru.megantcs.enhancer.platform.loader.preprocessors;

import ru.megantcs.enhancer.platform.loader.TextPipeline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IncludeProcessor implements TextPipeline.Processor {
    @Override
    public String process(String input) {
        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\n");

        for (String line : lines) {
            if (line.trim().startsWith("#include ")) {
                String filename = line.trim().substring(9).replace("\"", "").trim();
                result.append("-- # include '").append(filename).append("'\n");
                try {
                    var includeLines = Files.readAllLines(Path.of(filename));
                    for(var il : includeLines)
                        result.append(il).append('\n');

                } catch (IOException e) {
                    result.append("-- # error include '").append(filename).append("' exception: ").append(e).append('\n');
                }
            } else {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }
}
