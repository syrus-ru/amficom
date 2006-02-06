/*-
* $Id: CommonClientResourcesLoaderTest.java,v 1.3 2005/12/20 09:15:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.net.URL;

import javax.swing.UIManager;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;


/**
 * @version $Revision: 1.3 $, $Date: 2005/12/20 09:15:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class CommonClientResourcesLoaderTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CommonClientResourcesLoaderTest.class);
	}

	public void testLoadResources() {
		final ExtensionLauncher extensionLauncher = 
			ExtensionLauncher.getInstance();
		
		final String resourceName = "xml/ccresource.xml";

		final ClassLoader classLoader = CommonClientResourcesLoaderTest.class.getClassLoader();
		final URL url = classLoader.getResource(resourceName);
		
		extensionLauncher.addExtensions(url);		
		extensionLauncher.getExtensionHandler(ResourceHandler.class.getName());
		
		final String[] strings = new String[] {
				ResourceKeys.ICON_OPEN_SESSION,
                ResourceKeys.ICON_CLOSE_SESSION,
                ResourceKeys.ICON_DOMAIN_SELECTION,
                ResourceKeys.ICON_GENERAL,
                ResourceKeys.ICON_FURTHER,
                ResourceKeys.ICON_DELETE,
                ResourceKeys.ICON_INTRODUCE,
                ResourceKeys.ICON_OPEN_FILE,
                ResourceKeys.ICON_ADD_FILE,
                ResourceKeys.ICON_REMOVE_FILE,
                ResourceKeys.ICON_MINI_PATHMODE,
                ResourceKeys.ICON_MINI_FOLDER,
                ResourceKeys.ICON_MINI_PORT,
                ResourceKeys.ICON_MINI_TESTING,
                ResourceKeys.ICON_MINI_RESULT,
                ResourceKeys.ICON_REFRESH,
                ResourceKeys.ICON_SYNCHRONIZE,
                ResourceKeys.ICON_ADD,
                ResourceKeys.ICON_COMMIT,
                ResourceKeys.ICON_ADD,
                ResourceKeys.ICON_ZOOM_OUT,
                ResourceKeys.ICON_ZOOM_IN,
                ResourceKeys.ICON_ZOOM_NONE,
                ResourceKeys.ICON_TIME_DATE,
                ResourceKeys.IMAGE_LOGIN_LOGO,
                ResourceKeys.INSETS_NULL,
                ResourceKeys.INSETS_ICONED_BUTTON,
                ResourceKeys.SIZE_BUTTON,
                ResourceKeys.SIZE_NULL,
                "com.syrus.AMFICOM.icon.administrate",
				
		};
		
		for (final String string : strings) {
			assertNotNull(UIManager.get(string));
		}		
	}
}

