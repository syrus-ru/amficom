/*-
* $Id: ManagerResourcesCreator.java,v 1.2 2005/11/28 14:47:18 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.manager.BeanFactory;
import com.syrus.AMFICOM.extensions.manager.ManagerExtensions;
import com.syrus.AMFICOM.extensions.manager.ManagerResource;
import com.syrus.AMFICOM.extensions.manager.Perspective;
import com.syrus.AMFICOM.extensions.manager.PopupMenu;
import com.syrus.AMFICOM.extensions.manager.UiHandler;
import com.syrus.AMFICOM.extensions.manager.Validator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.SystemUserPermissionPopupMenu;
import com.syrus.AMFICOM.manager.beans.DomainBeanFactory;
import com.syrus.AMFICOM.manager.beans.MCMBeanFactory;
import com.syrus.AMFICOM.manager.beans.MessageBeanFactory;
import com.syrus.AMFICOM.manager.beans.NetBeanFactory;
import com.syrus.AMFICOM.manager.beans.PermissionBeanFactory;
import com.syrus.AMFICOM.manager.beans.RTUBeanFactory;
import com.syrus.AMFICOM.manager.beans.RoleBeanFactory;
import com.syrus.AMFICOM.manager.beans.RolePermissionBeanFactory;
import com.syrus.AMFICOM.manager.beans.ServerBeanFactory;
import com.syrus.AMFICOM.manager.beans.UserBeanFactory;
import com.syrus.AMFICOM.manager.beans.WorkstationBeanFactory;
import com.syrus.AMFICOM.manager.perspective.DomainsPerspective;
import com.syrus.AMFICOM.manager.perspective.HardSeverityMessagePerpective;
import com.syrus.AMFICOM.manager.perspective.SoftSeverityMessagePerpective;
import com.syrus.AMFICOM.manager.viewers.DomainBeanUI;
import com.syrus.AMFICOM.manager.viewers.KISBeanUI;
import com.syrus.AMFICOM.manager.viewers.MCMBeanUI;
import com.syrus.AMFICOM.manager.viewers.MessageBeanUI;
import com.syrus.AMFICOM.manager.viewers.NetBeanUI;
import com.syrus.AMFICOM.manager.viewers.RoleBeanUI;
import com.syrus.AMFICOM.manager.viewers.RolePermissionBeanUI;
import com.syrus.AMFICOM.manager.viewers.ServerBeanUI;
import com.syrus.AMFICOM.manager.viewers.SystemUserBeanUI;
import com.syrus.AMFICOM.manager.viewers.UserPermissionBeanUI;
import com.syrus.AMFICOM.manager.viewers.WorkstationBeanUI;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:18 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ManagerResourcesCreator extends TestCase {

	public void testCreateUIResourceDocument() throws Exception {
		final boolean enableOutput = true;
		final String xmlFilePath = "xml/manager.xml";
		
		
    	final XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();

		// Build a new document
    	
    	final RootDocument doc = RootDocument.Factory.newInstance();    	
		if (enableOutput) {
			System.out.println("Empty document:\n" + doc.xmlText(opt) + "\nValid:" + doc.validate() + "\n");
		}

		assertFalse(doc.validate());
		
		final Root extensions = doc.addNewRoot();		
		final ExtensionPoint extensionPoint = extensions.addNewExtension();
		final ManagerExtensions managerExtensions = (ManagerExtensions) extensionPoint.changeType(ManagerExtensions.type);
		managerExtensions.setId(ManagerHandler.class.getName());
		
		// create domains perspective 
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final Perspective domainsPerspective = (Perspective) resource.changeType(Perspective.type);
			{
			domainsPerspective.setId("domains");
			domainsPerspective.setHandler(DomainsPerspective.class.getName());
			
			BeanFactory domainFactory = domainsPerspective.addNewBeanFactory();
			domainFactory.setId(ObjectEntities.DOMAIN);
			domainFactory.setBeanFactoryClass(DomainBeanFactory.class.getName());
			
			UiHandler domainUIHandler = domainsPerspective.addNewUiHandler();
			domainUIHandler.setId(ObjectEntities.DOMAIN);
			domainUIHandler.setUiHandlerClass(DomainBeanUI.class.getName());
			
			BeanFactory networkFactory = domainsPerspective.addNewBeanFactory();
			networkFactory.setId(NetBeanFactory.NET_CODENAME);
			networkFactory.setBeanFactoryClass(NetBeanFactory.class.getName());
			
			UiHandler networkUIHandler = domainsPerspective.addNewUiHandler();
			networkUIHandler.setId(NetBeanFactory.NET_CODENAME);
			networkUIHandler.setUiHandlerClass(NetBeanUI.class.getName());
			
			Validator domainValidator = domainsPerspective.addNewValidator();
			domainValidator.setSource(ObjectEntities.DOMAIN);
			domainValidator.setTarget(ObjectEntities.DOMAIN);
			
			Validator netDomainValidator = domainsPerspective.addNewValidator();
			netDomainValidator.setSource(NetBeanFactory.NET_CODENAME);
			netDomainValidator.setTarget(ObjectEntities.DOMAIN);
			
			}
			
//			 create domain perspective 
			{			
				final Perspective perspective = domainsPerspective.addNewPerspective();
				{
					perspective.setId(ObjectEntities.DOMAIN);
					
					UiHandler networkUIHandler = perspective.addNewUiHandler();
					networkUIHandler.setId(NetBeanFactory.NET_CODENAME);
					networkUIHandler.setUiHandlerClass(NetBeanUI.class.getName());
					
					BeanFactory networkFactory = perspective.addNewBeanFactory();
					networkFactory.setId(NetBeanFactory.NET_CODENAME);
					networkFactory.setBeanFactoryClass(NetBeanFactory.class.getName());
					
					BeanFactory rtuFactory = perspective.addNewBeanFactory();
					rtuFactory.setId(ObjectEntities.KIS);
					rtuFactory.setBeanFactoryClass(RTUBeanFactory.class.getName());
					
					UiHandler rtuUIHandler = perspective.addNewUiHandler();
					rtuUIHandler.setId(ObjectEntities.KIS);
					rtuUIHandler.setUiHandlerClass(KISBeanUI.class.getName());
					
					BeanFactory serverFactory = perspective.addNewBeanFactory();
					serverFactory.setId(ObjectEntities.SERVER);
					serverFactory.setBeanFactoryClass(ServerBeanFactory.class.getName());
					
					UiHandler serverUIHandler = perspective.addNewUiHandler();
					serverUIHandler.setId(ObjectEntities.SERVER);
					serverUIHandler.setUiHandlerClass(ServerBeanUI.class.getName());
					
					BeanFactory mcmFactory = perspective.addNewBeanFactory();
					mcmFactory.setId(ObjectEntities.MCM);
					mcmFactory.setBeanFactoryClass(MCMBeanFactory.class.getName());
					
					UiHandler mcmUIHandler = perspective.addNewUiHandler();
					mcmUIHandler.setId(ObjectEntities.MCM);
					mcmUIHandler.setUiHandlerClass(MCMBeanUI.class.getName());
					
					BeanFactory workstationFactory = perspective.addNewBeanFactory();
					workstationFactory.setId(WorkstationBeanFactory.WORKSTATION_CODENAME);
					workstationFactory.setBeanFactoryClass(WorkstationBeanFactory.class.getName());
					
					UiHandler workstationUIHandler = perspective.addNewUiHandler();
					workstationUIHandler.setId(WorkstationBeanFactory.WORKSTATION_CODENAME);
					workstationUIHandler.setUiHandlerClass(WorkstationBeanUI.class.getName());
					
					BeanFactory userFactory = perspective.addNewBeanFactory();
					userFactory.setId(ObjectEntities.SYSTEMUSER);
					userFactory.setBeanFactoryClass(UserBeanFactory.class.getName());
					
					UiHandler userUIHandler = perspective.addNewUiHandler();
					userUIHandler.setId(ObjectEntities.SYSTEMUSER);
					userUIHandler.setUiHandlerClass(SystemUserBeanUI.class.getName());
					
					Validator mcmNetValidator = perspective.addNewValidator();
					mcmNetValidator.setSource(ObjectEntities.MCM);
					mcmNetValidator.setTarget(NetBeanFactory.NET_CODENAME);
					
					Validator rtuNetValidator = perspective.addNewValidator();
					rtuNetValidator.setSource( ObjectEntities.KIS);
					rtuNetValidator.setTarget(NetBeanFactory.NET_CODENAME);
					
					Validator serverNetValidator = perspective.addNewValidator();
					serverNetValidator.setSource(ObjectEntities.SERVER);
					serverNetValidator.setTarget(NetBeanFactory.NET_CODENAME);
					
					Validator workstationNetValidator = perspective.addNewValidator();
					workstationNetValidator.setSource(WorkstationBeanFactory.WORKSTATION_CODENAME);
					workstationNetValidator.setTarget(NetBeanFactory.NET_CODENAME);
					
					Validator userWorkstationValidator = perspective.addNewValidator();
					userWorkstationValidator.setSource(ObjectEntities.SYSTEMUSER);
					userWorkstationValidator.setTarget(WorkstationBeanFactory.WORKSTATION_CODENAME);
					
				}

				Perspective userPerspective = perspective.addNewPerspective();
//				 create user perspective 
				{			
//					final ManagerResource resource = managerExtensions.addNewManagerResource();
//					final Perspective userPerspective = (Perspective) resource.changeType(Perspective.type);
					userPerspective.setId(ObjectEntities.SYSTEMUSER);
					
					BeanFactory userFactory = userPerspective.addNewBeanFactory();
					userFactory.setId(ObjectEntities.SYSTEMUSER);
					userFactory.setBeanFactoryClass(UserBeanFactory.class.getName());
					
					UiHandler userUIHandler2 = userPerspective.addNewUiHandler();
					userUIHandler2.setId(ObjectEntities.SYSTEMUSER);
					userUIHandler2.setUiHandlerClass(SystemUserBeanUI.class.getName());
					
					BeanFactory permissionFactory = userPerspective.addNewBeanFactory();
					permissionFactory.setId(ObjectEntities.PERMATTR);
					permissionFactory.setBeanFactoryClass(PermissionBeanFactory.class.getName());
					
					UiHandler permissionUIHandler = userPerspective.addNewUiHandler();
					permissionUIHandler.setId(ObjectEntities.PERMATTR);
					permissionUIHandler.setUiHandlerClass(UserPermissionBeanUI.class.getName());
					
					for(final Module module : Module.getValueList()){
						final String codename = module.getCodename();
						BeanFactory modulePermissionFactory = userPerspective.addNewBeanFactory();
						modulePermissionFactory.setId(codename);
						modulePermissionFactory.setBeanFactoryClass(PermissionBeanFactory.class.getName());
						
						UiHandler modulePermissionUIHandler = userPerspective.addNewUiHandler();
						modulePermissionUIHandler.setId(codename);
						modulePermissionUIHandler.setUiHandlerClass(UserPermissionBeanUI.class.getName());
						
						Validator permissionUserValidator = userPerspective.addNewValidator();
						permissionUserValidator.setSource(codename);
						permissionUserValidator.setTarget(ObjectEntities.SYSTEMUSER);
					}
					
					Validator permissionUserValidator = userPerspective.addNewValidator();
					permissionUserValidator.setSource(ObjectEntities.PERMATTR);
					permissionUserValidator.setTarget(ObjectEntities.SYSTEMUSER);
					
					PopupMenu userMenu = userPerspective.addNewPopupMenu();
					userMenu.setId(ObjectEntities.SYSTEMUSER);
					userMenu.setPopupMenuHandler(SystemUserPermissionPopupMenu.class.getName());
			    }	

		    }
	    }	
		
		
		
		

		// create role perspective 
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final Perspective perspective = (Perspective) resource.changeType(Perspective.type);
			perspective.setId(ObjectEntities.ROLE);
			
			BeanFactory roleFactory = perspective.addNewBeanFactory();
			roleFactory.setId(ObjectEntities.ROLE);
			roleFactory.setBeanFactoryClass(RoleBeanFactory.class.getName());
			
			
			UiHandler roleUIHandler = perspective.addNewUiHandler();
			roleUIHandler.setId(ObjectEntities.ROLE);
			roleUIHandler.setUiHandlerClass(RoleBeanUI.class.getName());
			
			BeanFactory permissionFactory = perspective.addNewBeanFactory();
			permissionFactory.setId(ObjectEntities.PERMATTR);
			permissionFactory.setBeanFactoryClass(RolePermissionBeanFactory.class.getName());
			
			UiHandler permissionUIHandler = perspective.addNewUiHandler();
			permissionUIHandler.setId(ObjectEntities.PERMATTR);
			permissionUIHandler.setUiHandlerClass(RolePermissionBeanUI.class.getName());
			
			for(final Module module : Module.getValueList()){
				final String codename = module.getCodename();
				BeanFactory modulePermissionFactory = perspective.addNewBeanFactory();
				modulePermissionFactory.setId(codename);
				modulePermissionFactory.setBeanFactoryClass(RolePermissionBeanFactory.class.getName());
				
				UiHandler modulePermissionUIHandler = perspective.addNewUiHandler();
				modulePermissionUIHandler.setId(codename);
				modulePermissionUIHandler.setUiHandlerClass(RolePermissionBeanUI.class.getName());
				
				Validator permissionRoleValidator = perspective.addNewValidator();
				permissionRoleValidator.setSource(codename);
				permissionRoleValidator.setTarget(ObjectEntities.ROLE);
			}
			
			Validator permissionRoleValidator = perspective.addNewValidator();
			permissionRoleValidator.setSource(ObjectEntities.PERMATTR);
			permissionRoleValidator.setTarget(ObjectEntities.ROLE);
	    }
		
		// create severity message perspective 
		{			
			final Severity[] severities = new Severity[] {Severity.SEVERITY_SOFT, Severity.SEVERITY_HARD};
			final Class[] classes = new Class[] {SoftSeverityMessagePerpective.class, HardSeverityMessagePerpective.class};
			
			for (int i = 0; i < severities.length; i++) {
				final Severity severity = severities[i];
				final ManagerResource resource = managerExtensions.addNewManagerResource();
				final Perspective perspective = (Perspective) resource.changeType(Perspective.type);
				perspective.setId(severity.name().replaceAll("_", ""));
				perspective.setHandler(classes[i].getName());
				
				BeanFactory roleFactory = perspective.addNewBeanFactory();
				roleFactory.setId(ObjectEntities.ROLE);
				roleFactory.setBeanFactoryClass(RoleBeanFactory.class.getName());
				
				UiHandler roleUIHandler = perspective.addNewUiHandler();
				roleUIHandler.setId(ObjectEntities.ROLE);
				roleUIHandler.setUiHandlerClass(RoleBeanUI.class.getName());
				
				BeanFactory severityMessageFactory = perspective.addNewBeanFactory();
				severityMessageFactory.setId(ObjectEntities.DELIVERYATTRIBUTES);
				severityMessageFactory.setBeanFactoryClass(MessageBeanFactory.class.getName());
				
				UiHandler severityMessageUIHandler = perspective.addNewUiHandler();
				severityMessageUIHandler.setId(ObjectEntities.DELIVERYATTRIBUTES);
				severityMessageUIHandler.setUiHandlerClass(MessageBeanUI.class.getName());
				
				
				Validator messageValidator = perspective.addNewValidator();
				messageValidator.setSource(ObjectEntities.ROLE);
				messageValidator.setTarget(ObjectEntities.DELIVERYATTRIBUTES);
			}
			
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

