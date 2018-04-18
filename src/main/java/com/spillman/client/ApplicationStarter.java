package com.spillman.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class ApplicationStarter {

	private static Tomcat tomcat;

	public static void main(String[] args) throws Exception {
		File root = getRootFolder();
		System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
		tomcat = new Tomcat();
		Path tempPath = Files.createTempDirectory("tomcat-base-dir");
		tomcat.setBaseDir(tempPath.toString());
		tomcat.setPort(8080);
		File webContentFolder = new File(root.getAbsolutePath(), "src/main/webapp/");
		if (!webContentFolder.exists()) {
			webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
		}
		StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
		ctx.setParentClassLoader(ApplicationStarter.class.getClassLoader());
		File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "target/classes");
		WebResourceRoot resources = new StandardRoot(ctx);
		WebResourceSet resourceSet;
		resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClassesFolder.getAbsolutePath(), "/");
		resources.addPreResources(resourceSet);
		ctx.setResources(resources);
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

	private static File getRootFolder() {
		try {
			File root;
			String runningJarPath = ApplicationStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()
				.replaceAll("\\\\", "/");
			int lastIndexOf = runningJarPath.lastIndexOf("/target/");
			if (lastIndexOf < 0) {
				root = new File("");
			}
			else {
				root = new File(runningJarPath.substring(0, lastIndexOf));
			}
			return root;
		}
		catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

}
