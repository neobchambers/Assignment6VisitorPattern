package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryNode implements Element {
    private final String name;
    private final List<Element> children = new ArrayList<>();
    private DirectoryNode parent;

    public DirectoryNode(String name) {
        if (name == null || name.isBlank() || name.contains("/")) {
            throw new IllegalArgumentException("Invalid directory name");
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public DirectoryNode getParent() {
        return parent;
    }

    @Override
    public void setParent(DirectoryNode parent) {
        this.parent = parent;
    }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(Element child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }

        if (child == this) {
            throw new IllegalArgumentException("Directory cannot contain itself");
        }

        if (child instanceof FileNode file && file.getParent() != null) {
            throw new IllegalArgumentException("File already has a parent");
        }

        if (child instanceof DirectoryNode directory) {
            if (directory.parent != null) {
                throw new IllegalArgumentException("Directory already has a parent");
            }

            DirectoryNode current = this;

            while (current != null) {
                if (current == directory) {
                    throw new IllegalArgumentException("Cycle detected");
                }
                current = current.parent;
            }
        }

        for (Element existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException("Duplicate sibling name");
            }
        }

        children.add(child);

        if (child instanceof FileNode file) {
            file.setParent(this);
        } else if (child instanceof DirectoryNode directory) {
            directory.setParent(this);
        }
    }

    public void removeChild(Element child) {
        if (children.remove(child)) {
            if (child instanceof FileNode file) {
                file.setParent(null);
            } else if (child instanceof DirectoryNode directory) {
                directory.setParent(null);
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}