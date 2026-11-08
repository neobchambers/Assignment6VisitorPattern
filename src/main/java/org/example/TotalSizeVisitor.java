package org.example;

public class TotalSizeVisitor extends AbstractTreeVisitor {
    private long totalSize;

    @Override
    protected void onFile(FileNode file) {
        totalSize = Math.addExact(totalSize, file.getSize());
    }

    @Override
    protected void onDirectory(DirectoryNode directory) {
    }

    @Override
    protected void reset() {
        totalSize = 0;
    }

    public long getTotalSize() {
        return totalSize;
    }
}