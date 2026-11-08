package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SearchVisitor extends AbstractTreeVisitor {
    private final Predicate<String> filenameCriterion;
    private final long minimumSize;
    private final List<String> results = new ArrayList<>();

    public SearchVisitor(Predicate<String> filenameCriterion, long minimumSize) {
        if (filenameCriterion == null) {
            throw new IllegalArgumentException("Criterion cannot be null");
        }

        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative");
        }

        this.filenameCriterion = filenameCriterion;
        this.minimumSize = minimumSize;
    }

    @Override
    protected void onFile(FileNode file) {
        if (filenameCriterion.test(file.getName())
                && file.getSize() >= minimumSize) {
            results.add(currentPath);
        }
    }

    @Override
    protected void onDirectory(DirectoryNode directory) {
    }

    @Override
    protected void reset() {
        results.clear();
    }

    public List<String> getResults() {
        List<String> sorted = new ArrayList<>(results);
        Collections.sort(sorted);
        return Collections.unmodifiableList(sorted);
    }
}