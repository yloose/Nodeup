package de.yloose.nodeup.updateConfigBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.update4j.Configuration;
import org.update4j.FileMetadata;

public class UpdateConfigBuilder {

	private static final Logger LOG = Logger.getLogger(UpdateConfigBuilder.class.getName());

	public static void main(String[] args) {

		Properties props = new Properties();
		try {
			props.load(UpdateConfigBuilder.class.getResourceAsStream("/application.properties"));
		} catch (IOException e) {
			LOG.info("Failed to read application properties.");
			return;
		}
		String nodeupJarDirString = props.getProperty("nodeup-server.folder.path");
		String targetDirString = props.getProperty("target.folder.path");

		File nodeupJarDir = new File(nodeupJarDirString);
		List<File> nodeupJars = Arrays.asList(
				nodeupJarDir.listFiles((d, name) -> name.startsWith("nodeup-server-") && name.endsWith(".jar")));

		for (File jar : nodeupJars) {
			LOG.info("Generating config for " + jar.getName());
			String platform = jar.getName().replace("nodeup-server-", "").replace(".jar", "");
			File libpcap = new File(targetDirString + "libpcap-" + platform + ".so");
			
			Configuration config = Configuration.builder()
					.baseUri("https://github.com/yloose/Nodeup/releases/download/latest")
					.basePath("${user.dir}/${app.name}-update").property("app.name", "Nodeup")
					.property("default.launcher.main.class", "org.springframework.boot.loader.JarLauncher")
					.file(FileMetadata.readFrom(jar.getAbsolutePath()).path("Nodeup.jar").uri(jar.getName()).classpath())
					.file(FileMetadata.readFrom(libpcap.getAbsolutePath()).path("libpcap.so").uri(libpcap.getName()))
					.build();
					
			try (Writer out = Files.newBufferedWriter(Paths.get(targetDirString + "config-" + platform + ".xml"))) {
				config.write(out);
				LOG.info("Successufully wrote out config-" + platform + ".xml");
			} catch (Exception e) {
				LOG.info("Failed to write out config.xml");
			}
		}
	}
}
