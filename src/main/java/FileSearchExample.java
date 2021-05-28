import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class FileSearchExample implements FileVisitor<Path> {

    private Path startDir;
    private String fileName;

    FileSearchExample(Path startDir, String fileName) {
        this.startDir = startDir;
        this.fileName = fileName;
    }

    @Override
    public FileVisitResult preVisitDirectory(
            Path dir, BasicFileAttributes attrs) {
        return CONTINUE;
    }


    @Override
    public FileVisitResult visitFile(
            Path file, BasicFileAttributes attrs) {
        if (file.getFileName().toString().equals(this.fileName)) {
            System.out.println("File found:" + file.toString());
            return TERMINATE;
        }
        return CONTINUE;
    }


    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.out.println("Failed to access file: " + file.toString());
        return CONTINUE;
    }


    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        boolean finishedSearch = Files.isSameFile(dir, this.startDir);
        if (finishedSearch) {
            System.out.println("File: " + this.fileName + " not found");
            return TERMINATE;
        }
        return CONTINUE;
    }


    public static void main(String[] args) throws IOException {
        Path startingDir = Paths.get(".");
        String fileToSearch = "FileSearchExample.java";
        FileSearchExample crawler =
                new FileSearchExample(startingDir, fileToSearch);
        Files.walkFileTree(startingDir, crawler);
    }

}
