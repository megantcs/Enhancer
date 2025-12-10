package ru.megantcs.enhancer.platform.loader.preprocessors;

import ru.megantcs.enhancer.platform.loader.TextPipeline;
import ru.megantcs.enhancer.platform.loader.preprocessors.DefineProcessor.MacroDefinition;

import java.util.Map;
import java.util.Stack;

public class ConditionalProcessor implements TextPipeline.Processor {
    final Map<String, MacroDefinition> defines;

    private static class ConditionState {
        final boolean condition;
        final boolean enabled;

        ConditionState(boolean condition, boolean enabled) {
            this.condition = condition;
            this.enabled = enabled;
        }
    }

    public ConditionalProcessor(Map<String, MacroDefinition> defines) {
        this.defines = defines;
    }

    @Override
    public String process(String input) {
        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\n");
        Stack<ConditionState> conditionStack = new Stack<>();
        boolean currentlyEnabled = true;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            String originalLine = lines[i];

            if (line.startsWith("#if ")) {
                String condition = line.substring(4).trim();
                boolean conditionResult = evaluateCondition(condition);
                conditionStack.push(new ConditionState(conditionResult, currentlyEnabled));
                currentlyEnabled = currentlyEnabled && conditionResult;
                continue;
            } else if (line.startsWith("#ifdef ")) {
                String macroName = line.substring(7).trim();
                boolean conditionResult = defines.containsKey(macroName);
                conditionStack.push(new ConditionState(conditionResult, currentlyEnabled));
                currentlyEnabled = currentlyEnabled && conditionResult;
                continue;
            } else if (line.startsWith("#ifndef ")) {
                String macroName = line.substring(8).trim();
                boolean conditionResult = !defines.containsKey(macroName);
                conditionStack.push(new ConditionState(conditionResult, currentlyEnabled));
                currentlyEnabled = currentlyEnabled && conditionResult;
                continue;
            } else if (line.startsWith("#else")) {
                if (!conditionStack.isEmpty()) {
                    ConditionState state = conditionStack.peek();
                    currentlyEnabled = state.enabled && !state.condition;
                }
                continue;
            } else if (line.startsWith("#elif ")) {
                if (!conditionStack.isEmpty()) {
                    ConditionState state = conditionStack.pop();
                    String condition = line.substring(6).trim();
                    boolean conditionResult = evaluateCondition(condition);
                    conditionStack.push(new ConditionState(conditionResult, state.enabled));
                    currentlyEnabled = state.enabled && conditionResult;
                }
                continue;
            } else if (line.startsWith("#endif")) {
                if (!conditionStack.isEmpty()) {
                    conditionStack.pop();
                    if (conditionStack.isEmpty()) {
                        currentlyEnabled = true;
                    } else {
                        ConditionState state = conditionStack.peek();
                        currentlyEnabled = state.enabled && state.condition;
                    }
                }
                continue;
            }

            if (currentlyEnabled) {
                result.append(originalLine).append("\n");
            }
        }

        return result.toString();
    }

    private boolean evaluateCondition(String condition) {
        condition = condition.trim();

        if (condition.startsWith("defined(") && condition.endsWith(")")) {
            String macroName = condition.substring(8, condition.length() - 1).trim();
            return defines.containsKey(macroName);
        }

        if (condition.startsWith("!")) {
            return !evaluateCondition(condition.substring(1));
        }

        if (condition.contains("&&")) {
            String[] parts = condition.split("&&");
            boolean result = true;
            for (String part : parts) {
                result = result && evaluateCondition(part.trim());
            }
            return result;
        }

        if (condition.contains("||")) {
            String[] parts = condition.split("\\|\\|");
            boolean result = false;
            for (String part : parts) {
                result = result || evaluateCondition(part.trim());
            }
            return result;
        }

        return defines.containsKey(condition);
    }
}