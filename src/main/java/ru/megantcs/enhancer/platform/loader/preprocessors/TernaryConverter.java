package ru.megantcs.enhancer.platform.loader.preprocessors;

import ru.megantcs.enhancer.platform.loader.TextPipeline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TernaryConverter implements TextPipeline.Processor {

    @Override
    public String process(String input) {
        String pattern = "([^?]+)\\?\\s*([^:]+)\\s*:\\s*([^;\\n]+)";
        Pattern compiled = Pattern.compile(pattern);
        Matcher matcher = compiled.matcher(input);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String trueExpr = matcher.group(2).trim();
            String falseExpr = matcher.group(3).trim();

            String replacement = condition + " and " + trueExpr + " or " + falseExpr;
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}