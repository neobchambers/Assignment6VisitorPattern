package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    @Test
    void fileDispatchWorks() {
        FileNode file = new FileNode("a.txt", 100);

        Visitor visitor = new Visitor() {
            public void visit(FileNode f) {
                assertEquals("a.txt", f.getName());
            }

            public void visit(DirectoryNode d) {
                fail("Wrong dispatch");
            }
        };

        file.accept(visitor);
    }

    @Test
    void directoryDispatchWorks() {
        DirectoryNode directory = new DirectoryNode("docs");

        Visitor visitor = new Visitor() {
            public void visit(FileNode f) {
                fail("Wrong dispatch");
            }

            public void visit(DirectoryNode d) {
                assertEquals("docs", d.getName());
            }
        };

        directory.accept(visitor);
    }

    @Test
    void totalSizeAcrossNestedDirectories() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode sub = new DirectoryNode("sub");

        root.addChild(new FileNode("a.txt", 100));
        root.addChild(sub);
        sub.addChild(new FileNode("b.txt", 200));

        TotalSizeVisitor visitor = new TotalSizeVisitor();
        visitor.process(root);

        assertEquals(300, visitor.getTotalSize());
    }

    @Test
    void searchCombinesBothConditions() {
        DirectoryNode root = new DirectoryNode("root");

        root.addChild(new FileNode("report.txt", 500));
        root.addChild(new FileNode("report.pdf", 2000));
        root.addChild(new FileNode("photo.pdf", 3000));

        SearchVisitor visitor =
                new SearchVisitor(name -> name.endsWith(".pdf"), 1000);

        visitor.process(root);

        assertEquals(List.of("photo.pdf", "report.pdf"), visitor.getResults());
    }

    @Test
    void searchReturnsSortedPaths() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode sub = new DirectoryNode("sub");

        root.addChild(new FileNode("z.txt", 100));
        root.addChild(sub);
        sub.addChild(new FileNode("a.txt", 100));

        SearchVisitor visitor =
                new SearchVisitor(name -> name.endsWith(".txt"), 0);

        visitor.process(root);

        assertEquals(
                List.of("sub/a.txt", "z.txt"),
                visitor.getResults()
        );
    }

    @Test
    void statisticsAreCorrect() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode sub = new DirectoryNode("sub");
        DirectoryNode nested = new DirectoryNode("nested");

        root.addChild(sub);
        sub.addChild(nested);
        nested.addChild(new FileNode("a.txt", 100));

        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.process(root);

        assertEquals(1, visitor.getFileCount());
        assertEquals(3, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaximumDepth());
    }

    @Test
    void emptyDirectoryWorks() {
        DirectoryNode root = new DirectoryNode("root");

        TotalSizeVisitor size = new TotalSizeVisitor();
        size.process(root);

        StatisticsVisitor stats = new StatisticsVisitor();
        stats.process(root);

        assertEquals(0, size.getTotalSize());
        assertEquals(0, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(0, stats.getMaximumDepth());
    }

    @Test
    void repeatedExecutionDoesNotAccumulateResults() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("a.txt", 100));

        TotalSizeVisitor visitor = new TotalSizeVisitor();

        visitor.process(root);
        visitor.process(root);

        assertEquals(100, visitor.getTotalSize());
    }

    @Test
    void negativeSizeIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FileNode("bad.txt", -10)
        );
    }

    @Test
    void duplicateNamesAreRejected() {
        DirectoryNode root = new DirectoryNode("root");

        root.addChild(new FileNode("a.txt", 100));

        assertThrows(
                IllegalArgumentException.class,
                () -> root.addChild(new FileNode("a.txt", 200))
        );
    }

    @Test
    void multipleParentsAreRejected() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");

        FileNode file = new FileNode("a.txt", 100);

        first.addChild(file);

        assertThrows(
                IllegalArgumentException.class,
                () -> second.addChild(file)
        );
    }
}