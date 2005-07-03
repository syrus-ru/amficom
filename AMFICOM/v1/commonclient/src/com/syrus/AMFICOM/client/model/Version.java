
package com.syrus.AMFICOM.client.model;

public class Version {

	public Version() {
		// never occur
		assert false;
	}

	public static String getVersionNumber() {
		return "1.0";
	}

	public static String getVersionText() {
		return "Ограниченная демо-версия";
	}

	public static String getVersionCopyright() {
		return "Syrus Systems 2002-2005";
	}
	
	public static String getPatchVersion() {
		return "clear";
	}
}
