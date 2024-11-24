package org.nasdanika.launcher.demo.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

public class TestStats {
	
	enum Metric {
		
		REPO,
		MODULE,
		FILE,
		LINE_OF_CODE,
		SITE,
		HTML_PAGE
		
	}
		
	private final String[] GIT_REPOS = { "core", "html", "cli", "demo-cli", "nasdanika.github.io", "retrieval-augmented-generation" };	
	private final String[] GIT_MODEL_REPOS = { 
		"echarts",
		"ecore",
		"ncore",
		"drawio",
		"exec",
		"graph",
		"git",
		"gitLab",
		"html",
		"bootstrap",
		"html-app",
		"excel",
		"party",
		"architecture",
		"family",
		"bank",
		"pdf",
		"coverage",
		"source-engineering",
		"java",
		"maven",
		"enterprise",
		"function-flow",
		"nature",
		"rules"
	};
	
	/**
	 * Computes code stats - modules, source files, lines of code.
	 */
	@Test
	public void testStats() {		
		Map<Metric, int[]> measurements = new TreeMap<>();
		BiConsumer<Metric, Integer> measurementConsumer = (metric, measurement) -> measurements.computeIfAbsent(metric, m -> new int[] { 0 })[0] += measurement;
		for (String gitRepo: GIT_REPOS) {
			repoStats(new File("../../git/" + gitRepo), measurementConsumer);
		}
		for (String gitModelRepo: GIT_MODEL_REPOS) {
			repoStats(new File("../../git-models/" + gitModelRepo), measurementConsumer);
		}
		
		
		measurements.entrySet().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()[0]));
	}
	
	private void repoStats(File repo, BiConsumer<Metric, Integer> measurementConsumer) {
		if (!repo.isDirectory()) {
			fail("Not a directory:" + repo.getAbsolutePath());
		}
		measurementConsumer.accept(Metric.REPO, 1);
		walk(null, (file, path) -> repoFileStats(file, path, measurementConsumer), repo.listFiles());
	}
	
	protected void repoFileStats(File file, String path, BiConsumer<Metric, Integer> measurementConsumer) {
		if (path.equals("src/main/java/module-info.java") || path.endsWith("/src/main/java/module-info.java")) {
			measurementConsumer.accept(Metric.MODULE, 1);
		}		
		if ((path.startsWith("src/") || path.contains("/src/")) && file.isFile() && file.getName().endsWith(".java")) {
			measurementConsumer.accept(Metric.FILE, 1);
			try {
				measurementConsumer.accept(Metric.LINE_OF_CODE, Files.readLines(file, StandardCharsets.UTF_8).size());
			} catch (Exception e) {
				fail("Exception: " + e, e);
			}
		}
		if (path.equals("docs") && file.isDirectory()) {
			measurementConsumer.accept(Metric.SITE, 1);			
		}
		if (path.startsWith("docs/") && file.isFile() && path.endsWith(".html")) {
			measurementConsumer.accept(Metric.HTML_PAGE, 1);			
		}
	}
	
	/**
	 * Walks the directory passing files and their paths to the listener.
	 * @param source
	 * @param target
	 * @param cleanTarget
	 * @param cleanPredicate
	 * @param listener
	 * @throws IOException
	 */
	public static void walk(String path, BiConsumer<File,String> listener, File... files) {
		if (files != null) {
			for (File file: files) {
				String filePath = path == null ? file.getName() : path + "/" + file.getName();
				listener.accept(file, filePath);
				if (file.isDirectory()) {
					walk(filePath, listener, file.listFiles());
				}
			}
		}
	}
	

}
