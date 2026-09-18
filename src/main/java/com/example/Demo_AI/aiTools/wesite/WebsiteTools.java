package com.example.Demo_AI.aiTools.wesite;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class WebsiteTools {

    private final Path workspace =
            Path.of("generated-sites").toAbsolutePath().normalize();

    // Track kaunsa top-level project folder is request me bana
    private volatile String lastProjectRoot;

    public WebsiteTools() {
        try {
            Files.createDirectories(workspace);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create website workspace", e);
        }
    }

    @Tool(description = "Creates a new directory inside" +
            " the website workspace.")
    public String createDirectory(
            @ToolParam(description = "Relative directory " +
                    "path, for example brewlab") String path) {

        try {
            trackProjectRoot(path);
            Path directory = safePath(path);
            Files.createDirectories(directory);
            return "Directory created successfully: " + path;
        } catch (IOException e) {
            return "Failed to create directory: " + e.getMessage();
        }
    }

    @Tool(description = """
            Creates or overwrites a text file inside the website workspace.
            Use this to create HTML, CSS and JavaScript files.
            """)
    public String writeFile(
            @ToolParam(description = "Relative file path, for example " +
                    "brewlab/index.html") String path,
            @ToolParam(description = "Complete content that should be " +
                    "written into the file") String content) {

        try {
            trackProjectRoot(path);
            Path file = safePath(path);
            Files.createDirectories(file.getParent());
            Files.writeString(file, content, StandardCharsets.UTF_8);
            return "File written successfully: " + path;
        } catch (IOException e) {
            return "Failed to write file: " + e.getMessage();
        }
    }

    @Tool(
            description = "Reads the contents of an existing file from" +
                    " the website workspace.")
    public String readFile(
            @ToolParam(description = "Relative file path") String path) {

        try {
            return Files.readString(safePath(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Failed to read file: " + e.getMessage();
        }
    }

    @Tool(description = "Lists all files and directories inside" +
            " a website project.")
    public String listFiles(
            @ToolParam(description = "Relative directory path, " +
                    "for example brewlab") String path) {

        try {
            Path directory = safePath(path);

            if (!Files.exists(directory)) {
                return "Directory does not exist: " + path;
            }

            try (var files = Files.walk(directory)) {
                return files
                        .filter(file -> !file.equals(directory))
                        .map(workspace::relativize)
                        .map(Path::toString)
                        .collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            return "Failed to list files: " + e.getMessage();
        }
    }

    // ---- Naye helper methods (tools nahi hain, controller/service ke liye) ----

    public String getLastProjectRoot() {
        return lastProjectRoot;
    }

    /**
     * Diye gaye project folder ko in-memory ZIP me convert karta hai.
     */
    public byte[] zipProject(String projectRoot) throws IOException {
        Path projectPath = safePath(projectRoot);

        if (!Files.exists(projectPath)) {
            throw new IllegalArgumentException("Project directory not found: " + projectRoot);
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(byteStream);
             Stream<Path> paths = Files.walk(projectPath)) {

            for (Path file : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                String entryName = projectPath.relativize(file).toString().replace("\\", "/");
                zos.putNextEntry(new ZipEntry(entryName));
                Files.copy(file, zos);
                zos.closeEntry();
            }
        }
        return byteStream.toByteArray();
    }

    private void trackProjectRoot(String relativePath) {
        String normalized = relativePath.replace("\\", "/");
        String[] parts = normalized.split("/", 2);
        if (parts.length > 0 && !parts[0].isBlank()) {
            this.lastProjectRoot = parts[0];
        }
    }

    private Path safePath(String path) {
        Path resolved = workspace.resolve(path).normalize();

        if (!resolved.startsWith(workspace)) {
            throw new IllegalArgumentException("Access outside generated-sites " +
                    "is not allowed");
        }

        return resolved;
    }
}