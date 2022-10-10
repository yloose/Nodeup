package de.yloose.nodeup.updateConfigBuilder;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import org.update4j.Configuration;
import org.update4j.FileMetadata;

public class UpdateConfigBuilder {
	
	private static final Logger LOG = Logger.getLogger(UpdateConfigBuilder.class.getName());

	public static void main(String[] args) {
		
		String projectRoot = System.getProperty("user.dir");
		String configLoc = projectRoot + "/target";
		
		Properties props = new Properties();
		try {
			props.load(UpdateConfigBuilder.class.getResourceAsStream("/application.properties"));
		} catch (IOException e) {
			LOG.info("Failed to read application properties.");
			return;
		}
		String nodeupJar = props.getProperty("nodeup-server.file.path");

		Configuration config = Configuration.builder()
				.baseUri("http://docs.update4j.org/demo/business")
				.basePath("${user.dir}/${app.name}-update")
				.property("app.name", "Nodeup")
				.property("default.launcher.main.class", "org.springframework.boot.loader.JarLauncher")
				.file(FileMetadata.readFrom(nodeupJar).path("Nodeup.jar").uri("/nodeup.jar").classpath())
				.build();

		try (Writer out = Files.newBufferedWriter(Paths.get(configLoc + "/config.xml"))) {
			config.write(out);
		} catch (Exception e) {
			LOG.info("Failed to write out config.txt");
		}
	}

}
