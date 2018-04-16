package com.spillman.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

@Slf4j
public class ApplicationStarter {

	private static Tomcat tomcat;

	public static void main(String[] args) throws Exception {
		String webappDirLocation = "src/main/webapp/";
		tomcat = new Tomcat();
		tomcat.setPort(8080);
		StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		File additionWebInfClasses = new File("target/classes");
		WebResourceRoot resources = new StandardRoot(context);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
			additionWebInfClasses.getAbsolutePath(), "/"));
		context.setResources(resources);

		tomcat.start();
		tomcat.getServer().await();
	}

	public static void shutdown() {
		if (tomcat == null || tomcat.getServer() == null) {
			return;
		}
		if (tomcat.getServer().getState() != LifecycleState.DESTROYED) {
			if (tomcat.getServer().getState() != LifecycleState.STOPPED) {
				try {
					tomcat.stop();
				}
				catch (LifecycleException ex) {
					log.error("Stop tomcat error.", ex);
				}
			}
			try {
				tomcat.destroy();
			}
			catch (LifecycleException ex) {
				log.error("Destroy tomcat error.", ex);
			}
		}
	}

}
