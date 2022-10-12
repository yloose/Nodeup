package de.yloose.nodeup;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import de.yloose.nodeup.lib.NativeLibraryLoader;

@SpringBootApplication
public class Nodeup {

	private static final Logger LOG = LoggerFactory.getLogger(Nodeup.class);

	public static void main(String[] args) {

		try {
			File lib = new File("../native/target/nodeup-native.so");
			System.load(lib.getAbsolutePath());
		} catch (UnsatisfiedLinkError e) {
			LOG.info("No native library found, trying to extract library from jar.");
			try {
				NativeLibraryLoader.loadLibraryFromJar("/BOOT-INF/classes/libs/libWeatherstation.so");
			} catch (IOException e2) {
				LOG.error("Cannot load native library.", e2);
			}
		}

		ConfigurableApplicationContext ctx = SpringApplication.run(Nodeup.class, args);

		while (ctx.isActive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
