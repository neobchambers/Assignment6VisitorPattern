package org.example;

public abstract class AbstractTreeVisitor implements Visitor {
    protected String currentPath;
    protected int currentDepth;

    public final void process(Element root) {
        if (root == null) {
            throw new IllegalArgumentException("Root cannot be null");
        }

        reset();
        walk(root, root.getName(), 0, true);
    }

    private void walk(Element element, String path, int depth, boolean isRoot) {
        String previousPath = currentPath;
        int previousDepth = currentDepth;

        currentPath = isRoot && element instanceof DirectoryNode
                ? ""
                : path;
        currentDepth = depth;

        element.accept(this);

        currentPath = previousPath;
        currentDepth = previousDepth;
    }

    @Override
    public final void visit(FileNode file) {
        onFile(file);
    }

    @Override
    public final void visit(DirectoryNode directory) {
        onDirectory(directory);

        for (Element child : directory.getChildren()) {
            String childPath = currentPath.isEmpty()
                    ? child.getName()
                    : currentPath + "/" + child.getName();

            walk(child, childPath, currentDepth + 1, false);
        }
    }

    protected abstract void onFile(FileNode file);

    protected abstract void onDirectory(DirectoryNode directory);

    protected abstract void reset();
}