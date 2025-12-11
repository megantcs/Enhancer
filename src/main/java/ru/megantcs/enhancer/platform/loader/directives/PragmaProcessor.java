package ru.megantcs.enhancer.platform.loader.directives;

import ru.megantcs.enhancer.platform.loader.TextPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

public class PragmaProcessor implements TextPipeline.Processor {
    private static final Logger LOG = LoggerFactory.getLogger(PragmaProcessor.class);

    private final Map<String, Function<String, String>> pragmaHandlers;
    private final String pragmaPrefix = "#pragma";

    public PragmaProcessor(Map<String, Function<String, String>> pragmaHandlers) {
        this.pragmaHandlers = pragmaHandlers;
    }

    public void registerPragma(String pragmaName, Function<String, String> handler) {
        pragmaHandlers.put(pragmaName.toLowerCase(), handler);
        LOG.debug("Registered pragma handler: {}", pragmaName);
    }

    public void removePragma(String pragmaName) {
        pragmaHandlers.remove(pragmaName.toLowerCase());
        LOG.debug("Removed pragma handler: {}", pragmaName);
    }

    @Override
    public String process(String input) {
        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith(pragmaPrefix)) {
                processPragmaLine(trimmedLine);
            } else {
                result.append(line).append("\n");
            }
        }

        return result.toString().trim();
    }

    private void processPragmaLine(String line) {
        String content = line.substring(pragmaPrefix.length()).trim();

        int spaceIndex = content.indexOf(' ');
        String pragmaName;
        String pragmaContent;

        if (spaceIndex > 0) {
            pragmaName = content.substring(0, spaceIndex).toLowerCase();
            pragmaContent = content.substring(spaceIndex + 1).trim();
        } else {
            pragmaName = content.toLowerCase();
            pragmaContent = "";
        }

        Function<String, String> handler = pragmaHandlers.get(pragmaName);
        if (handler != null) {
            try {
                handler.apply(pragmaContent);
            } catch (Exception e) {
                LOG.error("Error executing pragma {}: {}", pragmaName, pragmaContent, e);
            }
        } else {
            LOG.warn("Unknown pragma: {}", pragmaName);
        }
    }

    public void registerHandler(String pragmaName, Runnable handler) {
        registerPragma(pragmaName, content -> {
            handler.run();
            return "";
        });
    }
}