package de.yloose.nodeup;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import de.yloose.nodeup.lib.NativeLibraryLoader;
import de.yloose.nodeup.util.SystemUtil;

@SpringBootApplication
public class Nodeup {

	private static final Logger LOG = LoggerFactory.getLogger(Nodeup.class);

	public static void main(String[] args) {
		
		try {
			File lib = new File("../native/target/nodeup-native.so");
			System.load(lib.getAbsolutePath());
		} catch (UnsatisfiedLinkError e) {
			LOG.info("No native library found, trying to extract library from jar.");
			String res1 = Nodeup.class.getResource("/de/yloose/nodeup/Nodeup.class").getPath();
			String res2 = res1.substring(5, res1.indexOf(".jar!"));
			System.load(res2.substring(0, res2.lastIndexOf("/")) + "/libpcap.so");
			try {
				NativeLibraryLoader.loadLibraryFromJar("/BOOT-INF/classes/libs/libNodeup.so");
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
