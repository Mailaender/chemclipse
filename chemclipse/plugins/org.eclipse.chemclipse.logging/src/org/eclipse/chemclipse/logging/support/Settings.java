/*******************************************************************************
 * Copyright (c) 2011, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.logging.support;

import java.io.File;
import java.util.Properties;

import org.eclipse.chemclipse.logging.Activator;
import org.osgi.framework.Version;

public class Settings {

	/*
	 * Application name and version are defined in the product definition
	 * or as a parameter when starting the application.
	 * -Dapplication.name=MyApp
	 * -Dapplication.version=1.1.x
	 */
	public static final String D_APPLICATION_NAME = "application.name";
	public static final String D_APPLICATION_VERSION = "application.version";
	//
	private static final String DEFAULT_APPLICATION_NAME = "Chromatography_Application";
	private static final String DEFAULT_APPLICATION_VERSION = "0.7.x";
	//
	private static final String D_OSGI_INSTANCE_AREA = "osgi.instance.area";
	private static final String D_OSGI_USER_AREA = "osgi.user.area";
	//
	private static File fileSettingsFolder = null; // will be initialized
	private static File fileWorkspaceFolder = null; // will be initialized

	/**
	 * Use only static methods.
	 */
	private Settings() {
	}

	/**
	 * Returns the settings folder, e.g.: "/home/user/.myapp/0.9.0"
	 * 
	 * @return File
	 */
	public static final File getSettingsDirectory() {

		if(isRunInitialization()) {
			initialize();
		}
		//
		return fileSettingsFolder;
	}

	public static final File getWorkspaceDirectory() {

		if(isRunInitialization()) {
			initialize();
		}
		//
		return fileWorkspaceFolder;
	}

	/**
	 * Returns e.g.: 0.9.0
	 * 
	 * @return String
	 */
	public static final String getVersionIdentifier() {

		String applicationVersion;
		//
		Properties properties = System.getProperties();
		Object name = properties.get(D_APPLICATION_VERSION);
		if(name != null && name instanceof String) {
			applicationVersion = (String)name;
		} else {
			applicationVersion = DEFAULT_APPLICATION_VERSION;
			try {
				Version version = Activator.getContext().getBundle().getVersion();
				StringBuilder builder = new StringBuilder();
				builder.append(version.getMajor());
				builder.append(".");
				builder.append(version.getMinor());
				builder.append(".");
				builder.append("x"); // version.getMicro()
				applicationVersion = builder.toString();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		return applicationVersion;
	}

	/**
	 * Use this method to get the default folder, e.g.:
	 * 
	 * folder = MyApp
	 * folder = .myapp
	 * 
	 * user.home/MyApp/0.9.x
	 * user.home/.myapp/0.9.x
	 * 
	 * @param folder
	 * @return String
	 */
	public static final String getDirectory(String folder) {

		/*
		 * Get the settings dir.
		 */
		StringBuilder builder = new StringBuilder();
		builder.append(System.getProperty("user.home"));
		builder.append(File.separator);
		builder.append(folder);
		builder.append(File.separator);
		builder.append(getVersionIdentifier());
		return builder.toString();
	}

	/**
	 * If the application is not valid, null will be returned.
	 * 
	 * @param applicationName
	 * @return String
	 */
	public static final String getApplicationName() {

		String applicationName;
		/*
		 * Get application name from the JRE prefs.
		 */
		Properties properties = System.getProperties();
		Object name = properties.get(D_APPLICATION_NAME);
		if(name != null && name instanceof String) {
			applicationName = (String)name;
		} else {
			applicationName = DEFAULT_APPLICATION_NAME;
		}
		/*
		 * Replace unallowed characters.
		 * White space and others are not allowed.
		 */
		if(!applicationName.equals("")) {
			applicationName = applicationName.replaceAll("-", "");
			applicationName = applicationName.replaceAll("\\.", "");
			applicationName = applicationName.replaceAll(":", "");
			applicationName = applicationName.replaceAll(" ", "");
			applicationName = applicationName.replaceAll("_", "");
		}
		return applicationName;
	}

	/**
	 * Tests if the values have been initialized.
	 * 
	 * @return boolean
	 */
	private static boolean isRunInitialization() {

		if(fileSettingsFolder == null || fileWorkspaceFolder == null) {
			return true;
		}
		return false;
	}

	/**
	 * Initializes the settings.
	 */
	private static void initialize() {

		/*
		 * Initialize the folder values.
		 */
		Properties properties = System.getProperties();
		Object workspaceFolder = properties.get(D_OSGI_INSTANCE_AREA);
		Object settingsFolder = properties.get(D_OSGI_USER_AREA);
		if(isSetDefaults(workspaceFolder, settingsFolder)) {
			/*
			 * Parse the application name.
			 * Set default folder.
			 * This is needed for JUnit tests in headless mode.
			 */
			String applicationName = getApplicationName();
			String applicationWorkspaceFolder = applicationName;
			String applicationSettingsFolder = "." + applicationName.toLowerCase();
			fileWorkspaceFolder = new File(getDirectory(applicationWorkspaceFolder));
			fileSettingsFolder = new File(getDirectory(applicationSettingsFolder));
		} else {
			/*
			 * Replace the file: prefix
			 */
			String applicationWorkspaceFolder = (String)workspaceFolder;
			applicationWorkspaceFolder = applicationWorkspaceFolder.replace("file:", "");
			String applicationSettingsFolder = (String)settingsFolder;
			applicationSettingsFolder = applicationSettingsFolder.replace("file:", "");
			fileWorkspaceFolder = new File(applicationWorkspaceFolder);
			fileSettingsFolder = new File(applicationSettingsFolder);
		}
		/*
		 * Print in the console.
		 */
		System.out.println("-----------------------------------------");
		System.out.println("Product Initializiation: " + Settings.class.getName());
		System.out.println("Workspace Path: " + fileWorkspaceFolder);
		System.out.println("Settings Path: " + fileSettingsFolder);
		System.out.println("-----------------------------------------");
	}

	/**
	 * Set default values?
	 * 
	 * @param workspaceFolder
	 * @param settingsFolder
	 * @return boolean
	 */
	private static boolean isSetDefaults(Object workspaceFolder, Object settingsFolder) {

		/*
		 * Null is not allowed.
		 */
		if(workspaceFolder == null || settingsFolder == null) {
			return true;
		}
		/*
		 * Object must be of instance String
		 */
		if(!(workspaceFolder instanceof String) || !(settingsFolder instanceof String)) {
			return true;
		}
		/*
		 * In case no VM argument has been set, create the defaults.
		 */
		String workspace = (String)workspaceFolder;
		String settings = (String)settingsFolder;
		if(workspace.equals("") || settings.equals("")) {
			return true;
		}
		/*
		 * Check if the product is run from the IDE product editor.
		 * Some plug-ins may fail if the workspace and settings path is too long.
		 * file:/home/.../0.9.x/runtime-....product/
		 */
		String regex = ".*runtime.*product/";
		if(workspace.matches(regex) || settings.matches(regex)) {
			return true;
		}
		/*
		 * Return false if no default shall be calculated.
		 */
		return false;
	}
}