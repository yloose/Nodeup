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
	
	public static final String CONFIG_PATH = "https://github.com/yloose/Nodeup/releases/download/latest/";

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
	
	public static String getArch() {
		return System.getProperty("os.arch");
	}
	

	public static Configuration loadConfig() {
		String fileName = "";
		
		try {
			fileName = "config-" + getPlatform() + ".xml";
		} catch (UnsupportedArchitectureException e) {
			System.err.println("Failed to get system architecture: " + e.getMessage());
			System.exit(-1);
		}
		
		Configuration config = null;
		try {
			URL configUrl = new URL(CONFIG_PATH + fileName);
			Reader in = new InputStreamReader(configUrl.openStream(), StandardCharsets.UTF_8);
			config = Configuration.read(in);
		} catch (IOException e) {
			System.err.println("Could not load remote config.");
			// TODO: Handle error
		}
		return config;
	}
	
	public static String getPlatform() throws UnsupportedArchitectureException {
		String arch = System.getProperty("os.arch");
		switch (arch) {
		
		case "x86_64":
			return "amd64";
		case "amd64":
			return "amd64";
			
		case "arm":
			return "armv7";
			
		case "arm64":
			return "aarch64";
		case "aarch64":
			return "aarch64";
		case "armv8":
			return "aarch64";
		
		default:
			throw new UnsupportedArchitectureException("Current platform: " + arch + " is not supported.");
		}
	}
}
