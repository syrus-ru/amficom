/*-
* $Id: CommonClientResourcesCreator.java,v 1.3 2005/12/20 09:15:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.resources.Dimension;
import com.syrus.AMFICOM.extensions.resources.Font;
import com.syrus.AMFICOM.extensions.resources.Handler;
import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.AMFICOM.extensions.resources.Insets;
import com.syrus.AMFICOM.extensions.resources.Resource;
import com.syrus.AMFICOM.extensions.resources.Resources;
import com.syrus.AMFICOM.extensions.resources.Size;

import junit.framework.TestCase;


/**
 * @version $Revision: 1.3 $, $Date: 2005/12/20 09:15:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class CommonClientResourcesCreator extends TestCase {

	public void testCreation() throws Exception {
		final String xmlFilePath = "resources/xml/ccresource.xml";
		this.buildDocument(true, xmlFilePath);
	}

    public XmlObject buildDocument(boolean enableOutput,
    		final String xmlFilePath) {
    	final XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();

		// Build a new document
    	
    	final RootDocument doc = RootDocument.Factory.newInstance();    	
		if (enableOutput) {
			System.out.println("Empty document:\n" + doc.xmlText(opt) + "\nValid:" + doc.validate() + "\n");
		}
		Root extensions = doc.addNewRoot();		
		ExtensionPoint extensionPoint = extensions.addNewExtension();
		Resources resources = (Resources) extensionPoint.changeType(Resources.type);
		resources.setId(ResourceHandler.class.getName());
		
		{			
			final Resource resource = resources.addNewResource();
			final Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(Font.type.getName().getLocalPart());
			handler.setHandlerClass(FontResourceHandler.class.getName());
	    }
		
		{
			Resource resource = resources.addNewResource();
			Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(Image.type.getName().getLocalPart());
			handler.setHandlerClass(ImageResourceHandler.class.getName());
		}
		
		{
			Resource resource = resources.addNewResource();
			Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(Insets.type.getName().getLocalPart());
			handler.setHandlerClass(InsetsResourceHandler.class.getName());
		}
		
		{
			Resource resource = resources.addNewResource();
			Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(Dimension.type.getName().getLocalPart());
			handler.setHandlerClass(DimensionResourceHandler.class.getName());
		}
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_OPEN_SESSION);
			image.setFilename("images/open_session.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_CLOSE_SESSION);
			image.setFilename("images/close_session.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_DOMAIN_SELECTION);
			image.setFilename("images/domains.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_GENERAL);
			image.setFilename("images/general.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_FURTHER);
			image.setFilename("images/further.gif");
	    }		
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_DELETE);
			image.setFilename("images/delete.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_INTRODUCE);
			image.setFilename("images/introduce.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_OPEN_FILE);
			image.setFilename("images/openfile.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_ADD_FILE);
			image.setFilename("images/addfile.gif");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_REMOVE_FILE);
			image.setFilename("images/removefile.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_MINI_PATHMODE);
			image.setFilename("images/pathmode.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(15);
			size.setHeight(15);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_MINI_FOLDER);
			image.setFilename("images/folder.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(15);
			size.setHeight(15);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_MINI_PORT);
			image.setFilename("images/port.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(15);
			size.setHeight(15);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_MINI_TESTING);
			image.setFilename("images/testing.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(15);
			size.setHeight(15);
			image.setSize(size);
	    }
		
//		{			
//			final Resource resource = resources.addNewResource();
//			final Image image = (Image) resource.changeType(Image.type);
//			image.setId(ResourceKeys.ICON_MINI_MEASUREMENT_SETUP);
//			image.setFilename("images/testsetup.gif");
//			Size size = Size.Factory.newInstance();
//			size.setWidth(15);
//			size.setHeight(15);
//			image.setSize(size);
//	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_MINI_RESULT);
			image.setFilename("images/result.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(15);
			size.setHeight(15);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_REFRESH);
			image.setFilename("images/refresh.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_SYNCHRONIZE);
			image.setFilename("images/synchronize.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_ADD);
			image.setFilename("images/newprop.gif");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_COMMIT);
			image.setFilename("images/commit.gif");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_ADD);
			image.setFilename("images/newprop.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_ZOOM_IN);
			image.setFilename("images/zoom_in.gif");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_ZOOM_OUT);
			image.setFilename("images/zoom_out.gif");
	    }

		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_ZOOM_NONE);
			image.setFilename("images/zoom_actual.gif");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.ICON_TIME_DATE);
			image.setFilename("images/timedate.gif");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(ResourceKeys.IMAGE_LOGIN_LOGO);
			image.setFilename("images/main/logo2.jpg");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Insets insets = (Insets) resource.changeType(Insets.type);
			insets.setId(ResourceKeys.INSETS_NULL);
			insets.setTop(0);
			insets.setLeft(0);
			insets.setBottom(0);
			insets.setRight(0);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Insets insets = (Insets) resource.changeType(Insets.type);
			insets.setId(ResourceKeys.INSETS_ICONED_BUTTON);
			insets.setTop(1);
			insets.setLeft(1);
			insets.setBottom(1);
			insets.setRight(1);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Dimension dimension = (Dimension) resource.changeType(Dimension.type);
			dimension.setId(ResourceKeys.SIZE_BUTTON);
			Size size = Size.Factory.newInstance();
			size.setWidth(24);
			size.setHeight(24);
			dimension.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Dimension dimension = (Dimension) resource.changeType(Dimension.type);
			dimension.setId(ResourceKeys.SIZE_NULL);
			Size size = Size.Factory.newInstance();
			size.setWidth(0);
			size.setHeight(0);
			dimension.setSize(size);
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId("com.syrus.AMFICOM.icon.administrate");
			image.setFilename("images/main/administrate_mini.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }

		// Document contains two concrete resources and is valid
		if (enableOutput) {
			System.out.println("Final document:\n" + doc.xmlText(opt));
			System.out.println("Valid = " + doc.validate());
		}

		if (xmlFilePath != null) {
			try {
				final FileWriter fileWriter = new FileWriter(xmlFilePath);
				fileWriter.write(doc.xmlText(opt));
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}
}

