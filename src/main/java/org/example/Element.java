package org.example;

public interface Element {
    String getName();
    DirectoryNode getParent();
    void setParent(DirectoryNode parent);
    void accept(Visitor visitor);
}