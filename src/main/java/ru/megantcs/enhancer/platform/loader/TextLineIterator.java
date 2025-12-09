package ru.megantcs.enhancer.platform.loader;

public class TextLineIterator implements Iterator<String>
{
    final String content;
    final String[] lines;

    private int i = -1;
    private String cur = "";

    public TextLineIterator(String content) {
        this.content = content;
        this.lines = content.split("\n");
    }

    @Override
    public boolean next() {
        if (i + 1 >= lines.length) {
            return false;
        }

        i++;
        cur = lines[i];
        return true;
    }

    @Override
    public boolean previous() {
        if (i <= 0) {
            return false;
        }

        i--;
        cur = lines[i];
        return true;
    }

    @Override
    public String peek() {
        return cur;
    }

    @Override
    public boolean hasNext() {
        return i + 1 < lines.length;
    }

    @Override
    public boolean hasPrevious() {
        return i > 0;
    }

    @Override
    public boolean goTo(int index) {
        if (index < 0 || index >= lines.length) {
            return false;
        }

        i = index;
        cur = lines[i];
        return true;
    }

    public void reset() {
        i = -1;
        cur = "";
    }

    public int index() {
        return i;
    }
}
