/*-
* $Id: ResourcesCreator.java,v 1.2 2005/12/07 15:43:50 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.resources.Handler;
import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.AMFICOM.extensions.resources.Resource;
import com.syrus.AMFICOM.extensions.resources.Resources;
import com.syrus.AMFICOM.extensions.resources.Size;
import com.syrus.AMFICOM.resources.ImageResourceHandler;
import com.syrus.AMFICOM.resources.ResourceHandler;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/07 15:43:50 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ResourcesCreator extends TestCase {

	public void testCreateUIResourceDocument() throws Exception {
		final boolean enableOutput = true;
		final String xmlFilePath = "xml/resources.xml";
		
		
    	final XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();

		// Build a new document
    	
    	final RootDocument doc = RootDocument.Factory.newInstance();    	
		if (enableOutput) {
			System.out.println("Empty document:\n" + doc.xmlText(opt) + "\nValid:" + doc.validate() + "\n");
		}

		assertFalse(doc.validate());
		
		System.out.println("1");
		
		Root extensions = doc.addNewRoot();		
		ExtensionPoint extensionPoint = extensions.addNewExtension();
		final XmlObject changeType = extensionPoint.changeType(Resources.type);
		System.out.println(changeType.getClass().getName());
		System.out.println("expect: " + Resources.class.getName());
		Resources resources = (Resources) changeType;
		resources.setId(ResourceHandler.class.getName());
		
		{
			Resource resource = resources.addNewResource();
			Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(Image.type.getName().getLocalPart());
			handler.setHandlerClass(ImageResourceHandler.class.getName());
		}
		
		
		// RTU
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.rtu");
			image.setFilename("com/syrus/AMFICOM/manager/resources/rtu.png");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.rtu");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/rtu.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}

		// Domain
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.domain");
			image.setFilename("com/syrus/AMFICOM/manager/resources/domain2.png");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.domain");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/domain.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// MCM
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.mcm");
			image.setFilename("com/syrus/AMFICOM/manager/resources/mcm.png");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.mcm");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/mcm.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// User
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.user");
			image.setFilename("com/syrus/AMFICOM/manager/resources/user.gif");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.user");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/user.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// Server
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.server");
			image.setFilename("com/syrus/AMFICOM/manager/resources/server.png");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.server");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/server.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// Workstation
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.workstation");
			image.setFilename("com/syrus/AMFICOM/manager/resources/arm.gif");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.workstation");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/arm.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// Network
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.network");
			image.setFilename("com/syrus/AMFICOM/manager/resources/cloud.png");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.network");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/cloud.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		// Warning message
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.warning");
			image.setFilename("com/syrus/AMFICOM/manager/resources/enwarning.gif");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.warning");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/enwarning.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// Error message
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.error");
			image.setFilename("com/syrus/AMFICOM/manager/resources/enerror.gif");
		}

		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.icons.error");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/enerror.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
		}
		
		// Permissions 
		{
			for (final Module module : Module.getValueList()) {
				if (module.isEnable()) {
					{
						Resource resource = resources.addNewResource();
						Image image = (Image) resource.changeType(Image.type);
						image.setId("com.syrus.AMFICOM.manager.resources." + module.getCodename());
						image.setFilename("com/syrus/AMFICOM/manager/resources/" + module.getCodename() +".gif");
					}
					{
						Resource resource = resources.addNewResource();
						Image image = (Image) resource.changeType(Image.type);
						image.setId("com.syrus.AMFICOM.manager.resources.icons." + module.getCodename());
						image.setFilename("com/syrus/AMFICOM/manager/resources/icons/" + module.getCodename() +".gif");
						Size size = Size.Factory.newInstance();
						size.setWidth(16);
						size.setHeight(16);
						image.setSize(size);
					}
				}
			}
		}
		
		// other
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.action.enter");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/enter.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.action.connecton");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/connecton.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.action.connectoff");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/connectoff.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.action.undo");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/undo.gif");
		}		
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.manager.resources.action.redo");
			image.setFilename("com/syrus/AMFICOM/manager/resources/icons/redo.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("org.jgraph.example.resources.action.paste");
			image.setFilename("org/jgraph/example/resources/paste.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("org.jgraph.example.resources.action.cut");
			image.setFilename("org/jgraph/example/resources/cut.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("org.jgraph.example.resources.action.delete");
			image.setFilename("org/jgraph/example/resources/delete.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("org.jgraph.example.resources.action.zoom");
			image.setFilename("org/jgraph/example/resources/zoom.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("org.jgraph.example.resources.action.zoomin");
			image.setFilename("org/jgraph/example/resources/zoomin.gif");
		}
		
		{
			Resource resource = resources.addNewResource();
			Image image = (Image) resource.changeType(Image.type);
			image.setId("org.jgraph.example.resources.action.zoomout");
			image.setFilename("org/jgraph/example/resources/zoomout.gif");
		}
		
		// Document contains two concrete resources and is valid
		if (enableOutput) {
			System.out.println("Final document:\n" + doc.xmlText(opt));
			System.out.println("Valid = " + doc.validate());			
		}

		assertTrue(doc.validate());
		
		if (xmlFilePath != null && doc.validate()) {
			try {
				final FileWriter fileWriter = new FileWriter(xmlFilePath);
				fileWriter.write(doc.xmlText(opt));
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
}

