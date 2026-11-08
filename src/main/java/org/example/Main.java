package org.example;

public class Main {
    public static void main(String[] args) {
        DirectoryNode root = new DirectoryNode("root");

        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode images = new DirectoryNode("images");
        DirectoryNode projects = new DirectoryNode("projects");

        root.addChild(documents);
        root.addChild(images);

        documents.addChild(new FileNode("report.pdf", 5000));
        documents.addChild(new FileNode("notes.txt", 2000));

        images.addChild(new FileNode("photo.jpg", 8000));

        images.addChild(projects);
        projects.addChild(new FileNode("design.png", 12000));

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        sizeVisitor.process(root);

        System.out.println("Total size: " + sizeVisitor.getTotalSize());

        SearchVisitor searchVisitor =
                new SearchVisitor(name -> name.endsWith(".png"), 1000);

        searchVisitor.process(root);

        System.out.println("Search results: " + searchVisitor.getResults());

        StatisticsVisitor statisticsVisitor = new StatisticsVisitor();
        statisticsVisitor.process(root);

        System.out.println("Files: " + statisticsVisitor.getFileCount());
        System.out.println("Directories: " + statisticsVisitor.getDirectoryCount());
        System.out.println("Maximum depth: " + statisticsVisitor.getMaximumDepth());
    }
}