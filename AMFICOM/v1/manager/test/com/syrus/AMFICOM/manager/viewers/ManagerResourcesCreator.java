/*-
* $Id: ManagerResourcesCreator.java,v 1.1 2005/11/07 15:21:58 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.DomainBeanFactory;
import com.syrus.AMFICOM.manager.MCMBeanFactory;
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.PermissionBeanFactory;
import com.syrus.AMFICOM.manager.RTUBeanFactory;
import com.syrus.AMFICOM.manager.RoleBeanFactory;
import com.syrus.AMFICOM.manager.RolePermissionBeanFactory;
import com.syrus.AMFICOM.manager.ServerBeanFactory;
import com.syrus.AMFICOM.manager.UserBeanFactory;
import com.syrus.AMFICOM.manager.WorkstationBeanFactory;
import com.syrus.amficom.extensions.ExtensionPoint;
import com.syrus.amficom.extensions.RootDocument;
import com.syrus.amficom.extensions.RootDocument.Root;
import com.syrus.amficom.extensions.manager.BeanFactory;
import com.syrus.amficom.extensions.manager.ManagerExtensions;
import com.syrus.amficom.extensions.manager.ManagerResource;
import com.syrus.amficom.extensions.manager.UiHandler;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/07 15:21:58 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerResourcesCreator extends TestCase {

	public void testCreateUIResourceDocument() throws Exception {
		final boolean enableOutput = true;
		final String xmlFilePath = "xml/manager.xml";
		
		
    	final XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();

		// Build a new document
    	
    	final RootDocument doc = RootDocument.Factory.newInstance();    	
		if (enableOutput) {
			System.out.println("Empty document:\n" + doc.xmlText(opt) + "\nValid:" + doc.validate() + "\n");
		}
		final Root extensions = doc.addNewRoot();		
		final ExtensionPoint extensionPoint = extensions.addNewExtension();
		final ManagerExtensions managerExtensions = (ManagerExtensions) extensionPoint.changeType(ManagerExtensions.type);
		managerExtensions.setId(ManagerHandler.class.getName());
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler uiHandler = (UiHandler) resource.changeType(UiHandler.type);
			uiHandler.setId(ObjectEntities.DOMAIN);
			uiHandler.setUiHandlerClass(DomainBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory uiHandler = (BeanFactory) resource.changeType(BeanFactory.type);
			uiHandler.setId(ObjectEntities.DOMAIN);
			uiHandler.setBeanFactoryClass(DomainBeanFactory.class.getName());
	    }

		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(ObjectEntities.SYSTEMUSER);
			handler.setUiHandlerClass(SystemUserBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.SYSTEMUSER);
			handler.setBeanFactoryClass(UserBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(ObjectEntities.SERVER);
			handler.setUiHandlerClass(ServerBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.SERVER);
			handler.setBeanFactoryClass(ServerBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(ObjectEntities.KIS);
			handler.setUiHandlerClass(KISBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.KIS);
			handler.setBeanFactoryClass(RTUBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(ObjectEntities.MCM);
			handler.setUiHandlerClass(MCMBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.MCM);
			handler.setBeanFactoryClass(MCMBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(ObjectEntities.ROLE);
			handler.setUiHandlerClass(RoleBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.ROLE);
			handler.setBeanFactoryClass(RoleBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.PERMATTR + ObjectEntities.SYSTEMUSER);
			handler.setBeanFactoryClass(PermissionBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(ObjectEntities.PERMATTR + ObjectEntities.ROLE);
			handler.setBeanFactoryClass(RolePermissionBeanFactory.class.getName());
	    }
		
		{
			for(final Module module : Module.getValueList()){
				final ManagerResource resource = managerExtensions.addNewManagerResource();
				final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
				handler.setId(module.getCodename() + ObjectEntities.SYSTEMUSER);
				handler.setUiHandlerClass(UserPermissionBeanUI.class.getName());
			}
			
			for(final Module module : Module.getValueList()){
				final ManagerResource resource = managerExtensions.addNewManagerResource();
				final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
				handler.setId(module.getCodename() + ObjectEntities.ROLE);
				handler.setUiHandlerClass(RolePermissionBeanUI.class.getName());
			}
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(NetBeanFactory.NET_CODENAME);
			handler.setUiHandlerClass(NetBeanUI.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(NetBeanFactory.NET_CODENAME);
			handler.setBeanFactoryClass(NetBeanFactory.class.getName());
	    }
		
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final UiHandler handler = (UiHandler) resource.changeType(UiHandler.type);
			handler.setId(WorkstationBeanFactory.WORKSTATION_CODENAME);
			handler.setUiHandlerClass(WorkstationBeanUI.class.getName());
	    }

		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final BeanFactory handler = (BeanFactory) resource.changeType(BeanFactory.type);
			handler.setId(WorkstationBeanFactory.WORKSTATION_CODENAME);
			handler.setBeanFactoryClass(WorkstationBeanFactory.class.getName());
	    }
		
		
		// Document contains two concrete resources and is valid
		if (enableOutput) {
			System.out.println("Final document:\n" + doc.xmlText(opt));
			System.out.println("Valid = " + doc.validate());
		}

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

