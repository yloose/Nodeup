package de.yloose.nodeup.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

	@Autowired
	ConfigurableApplicationContext ctx;
	
	public void update() {
		Thread thread = new Thread(this::performShutdown);
		thread.setContextClassLoader(getClass().getClassLoader());
		thread.start();
	}

	private void performShutdown() {
		try {
			Thread.sleep(500L);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		ctx.close();
	}
	
}
