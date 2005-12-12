/*-
* $Id: ManagerResourcesTest.java,v 1.4 2005/12/12 13:41:08 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.net.URL;

import javax.swing.UIManager;

import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;
import com.syrus.AMFICOM.resources.ResourceHandler;


/**
 * @version $Revision: 1.4 $, $Date: 2005/12/12 13:41:08 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerResourcesTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ManagerResourcesTest.class);
	}

	public void testLoadResources() {
		final ExtensionLauncher extensionLauncher = 
			ExtensionLauncher.getInstance();
		
		final String resourceName = "xml/resources.xml";

		final ClassLoader classLoader = ManagerResourcesTest.class.getClassLoader();
		final URL url = classLoader.getResource(resourceName);
		
		extensionLauncher.addExtensions(url);		
		extensionLauncher.getExtensionHandler(ResourceHandler.class.getName());
		
		final String[] strings = new String[] {
				"com.syrus.AMFICOM.manager.resources.rtu",
				"com.syrus.AMFICOM.manager.resources.icons.rtu",
				"com.syrus.AMFICOM.manager.resources.domain",
				"com.syrus.AMFICOM.manager.resources.icons.domain",
				"com.syrus.AMFICOM.manager.resources.mcm",
				"com.syrus.AMFICOM.manager.resources.icons.mcm",				
				"com.syrus.AMFICOM.manager.resources.user",
				"com.syrus.AMFICOM.manager.resources.icons.user",
				"com.syrus.AMFICOM.manager.resources.server",
				"com.syrus.AMFICOM.manager.resources.icons.server",
				"com.syrus.AMFICOM.manager.resources.workstation",
				"com.syrus.AMFICOM.manager.resources.icons.workstation",
				"com.syrus.AMFICOM.manager.resources.network",
				"com.syrus.AMFICOM.manager.resources.icons.network",
				"com.syrus.AMFICOM.manager.resources.warning",
				"com.syrus.AMFICOM.manager.resources.icons.warning",
				"com.syrus.AMFICOM.manager.resources.error",
				"com.syrus.AMFICOM.manager.resources.icons.error",
				"com.syrus.AMFICOM.manager.resources.action.enter",
				"com.syrus.AMFICOM.manager.resources.action.connecton",
				"com.syrus.AMFICOM.manager.resources.action.connectoff",
				"com.syrus.AMFICOM.manager.resources.action.undo",
				"com.syrus.AMFICOM.manager.resources.action.redo",
				"org.jgraph.example.resources.action.paste",
				"org.jgraph.example.resources.action.cut",
				"org.jgraph.example.resources.action.delete",
				"org.jgraph.example.resources.action.zoom",
				"org.jgraph.example.resources.action.zoomin",
				"org.jgraph.example.resources.action.zoomout"
				
		};
		
		for (final String string : strings) {
			assertNotNull(UIManager.get(string));
		}

		for (final Module module : Module.getValueList()) {
			if (module.isEnable()) {
				assertNotNull(UIManager.get("com.syrus.AMFICOM.manager.resources." + module.getCodename()));
				assertNotNull(UIManager.get("com.syrus.AMFICOM.manager.resources.icons." + module.getCodename()));				
			}
		}
	}
}

