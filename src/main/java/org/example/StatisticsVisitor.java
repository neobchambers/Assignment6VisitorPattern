package org.example;

public class StatisticsVisitor extends AbstractTreeVisitor {
    private int fileCount;
    private int directoryCount;
    private int maximumDepth;

    @Override
    protected void onFile(FileNode file) {
        fileCount++;
        maximumDepth = Math.max(maximumDepth, currentDepth);
    }

    @Override
    protected void onDirectory(DirectoryNode directory) {
        directoryCount++;
        maximumDepth = Math.max(maximumDepth, currentDepth);
    }

    @Override
    protected void reset() {
        fileCount = 0;
        directoryCount = 0;
        maximumDepth = 0;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaximumDepth() {
        return maximumDepth;
    }
}