package de.yloose.nodeup.bootstrap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

import org.update4j.Archive;
import org.update4j.Configuration;
import org.update4j.UpdateOptions.ArchiveUpdateOptions;

public class Bootstrap {
	
	public static final String CONFIG_URL = "https://github.com/yloose/Nodeup/releases/download/latest/config.xml";

	public static void main(String[] args) {
		while (true) {
			update();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void update() {
		Configuration config = loadConfig();

		config.update(ArchiveUpdateOptions.archive(FileSystems.getDefault().getPath("nodeup-update.zip")));
		try {
			if (config.requiresUpdate()) {
				Archive.read("node-update.zip").install();
			}
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Handle error
		}

		config.launch();
	}

	public static Configuration loadConfig() {
		Configuration config = null;
		try {
			URL configUrl = new URL(CONFIG_URL);
			Reader in = new InputStreamReader(configUrl.openStream(), StandardCharsets.UTF_8);
			config = Configuration.read(in);
		} catch (IOException e) {
			System.err.println("Could not load remote config.");
			// TODO: Handle error
		}
		return config;
	}
}
