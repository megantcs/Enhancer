package ru.megantcs.enhancer.platform.loader;

import java.util.ArrayList;
import java.util.List;

public class TextPipeline {
    final List<Processor> processors = new ArrayList<>();


    public interface Processor {
        String process(String input);
    }

    public void addProcessor(Processor processor) {
        processors.add(processor);
    }

    public String process(String input) {
        String result = input;
        for (Processor processor : processors) {
            result = processor.process(result);
        }
        return result;
    }

    public void clear() {
        processors.clear();
    }

    public int size() {
        return processors.size();
    }
}