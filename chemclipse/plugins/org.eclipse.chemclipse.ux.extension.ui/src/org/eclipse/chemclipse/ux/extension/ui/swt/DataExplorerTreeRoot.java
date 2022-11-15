/*******************************************************************************
 * Copyright (c) 2019, 2022 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.swt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.settings.ApplicationSettings;
import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;
import org.eclipse.chemclipse.support.settings.UserManagement;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferenceConstants;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferenceSupplier;
import org.eclipse.core.resources.ResourcesPlugin;

/*
 * https://docs.oracle.com/javase/tutorial/essential/io/fileAttr.html
 */
public enum DataExplorerTreeRoot {

	NONE(""), //
	DRIVES("Drives"), //
	HOME("Home"), //
	WORKSPACE("Workspace"), //
	USER_LOCATION("User Location");

	private static final Logger logger = Logger.getLogger(DataExplorerTreeRoot.class);
	//
	private String label;

	private DataExplorerTreeRoot(String label) {

		this.label = label;
	}

	@Override
	public String toString() {

		return this != NONE ? label : super.toString();
	}

	public String getPreferenceKeyDefaultPath() {

		switch(this) {
			case DRIVES:
				return PreferenceConstants.P_SELECTED_DRIVE_PATH;
			case HOME:
				return PreferenceConstants.P_SELECTED_HOME_PATH;
			case WORKSPACE:
				return PreferenceConstants.P_SELECTED_WORKSPACE_PATH;
			case USER_LOCATION:
				return PreferenceConstants.P_SELECTED_USER_LOCATION_PATH;
			case NONE:
			default:
				return "selected" + name() + "Path";
		}
	}

	public File[] getRootContent() {

		File[] roots;
		switch(this) {
			case DRIVES:
				roots = filterRoots(File.listRoots());
				break;
			case HOME:
				roots = new File[]{new File(UserManagement.getUserHome())};
				break;
			case WORKSPACE:
				File root = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
				if(root.exists()) {
					roots = root.listFiles();
				} else {
					/*
					 * Fallback solution
					 */
					roots = ApplicationSettings.getWorkspaceDirectory().listFiles();
				}
				break;
			case USER_LOCATION:
				roots = new File[]{getUserLocation()};
				break;
			case NONE:
			default:
				roots = new File[]{};
				break;
		}
		//
		return roots;
	}

	public static DataExplorerTreeRoot[] getDefaultRoots() {

		return new DataExplorerTreeRoot[]{DRIVES, HOME, WORKSPACE, USER_LOCATION};
	}

	private static File getUserLocation() {

		String userLocationPath = PreferenceSupplier.getUserLocationPath();
		File userLocation = new File(userLocationPath);
		if(!userLocation.exists()) {
			userLocation = new File(UserManagement.getUserHome());
		}
		return userLocation;
	}

	private static File[] filterRoots(File[] roots) {

		List<File> rootFiles = new ArrayList<>();
		for(File root : roots) {
			if((!PreferenceSupplier.showNetworkShares()) && isWindowsNetworkDriveRoot(root))
				continue;
			rootFiles.add(root);
		}
		return rootFiles.toArray(new File[rootFiles.size()]);
	}

	private static boolean isWindowsNetworkDriveRoot(File file) {

		if(!OperatingSystemUtils.isWindows()) {
			return false;
		}
		String path = file.getPath();
		if(path.length() > 3) {
			return false;
		}
		String driveLetter = path.substring(0, 2);
		List<String> cmd = Arrays.asList("cmd", "/c", "net", "use", driveLetter);
		try {
			Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
			process.getOutputStream().close();
			int exitCode = process.waitFor();
			return exitCode == 0;
		} catch(Exception e) {
			logger.error("Failed to detect network drive status for " + driveLetter, e);
		}
		return false;
	}
}