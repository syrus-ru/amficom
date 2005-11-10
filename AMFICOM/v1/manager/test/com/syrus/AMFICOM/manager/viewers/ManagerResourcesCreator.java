/*-
* $Id: ManagerResourcesCreator.java,v 1.3 2005/11/10 13:59:37 bob Exp $
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
import com.syrus.AMFICOM.manager.MessageBeanFactory;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.PermissionBeanFactory;
import com.syrus.AMFICOM.manager.RTUBeanFactory;
import com.syrus.AMFICOM.manager.RoleBeanFactory;
import com.syrus.AMFICOM.manager.RolePermissionBeanFactory;
import com.syrus.AMFICOM.manager.ServerBeanFactory;
import com.syrus.AMFICOM.manager.UserBeanFactory;
import com.syrus.AMFICOM.manager.WorkstationBeanFactory;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.amficom.extensions.ExtensionPoint;
import com.syrus.amficom.extensions.RootDocument;
import com.syrus.amficom.extensions.RootDocument.Root;
import com.syrus.amficom.extensions.manager.BeanFactory;
import com.syrus.amficom.extensions.manager.ManagerExtensions;
import com.syrus.amficom.extensions.manager.ManagerResource;
import com.syrus.amficom.extensions.manager.Perspective;
import com.syrus.amficom.extensions.manager.UiHandler;
import com.syrus.amficom.extensions.manager.Validator;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/10 13:59:37 $
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
		
		assertFalse(doc.validate());
		
		final Root extensions = doc.addNewRoot();		
		final ExtensionPoint extensionPoint = extensions.addNewExtension();
		final ManagerExtensions managerExtensions = (ManagerExtensions) extensionPoint.changeType(ManagerExtensions.type);
		managerExtensions.setId(ManagerHandler.class.getName());
		
		// create domains perspective 
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final Perspective perspective = (Perspective) resource.changeType(Perspective.type);
			perspective.setId("domains");
			
			BeanFactory domainFactory = perspective.addNewBeanFactory();
			domainFactory.setId(ObjectEntities.DOMAIN);
			domainFactory.setBeanFactoryClass(DomainBeanFactory.class.getName());
			
			UiHandler domainUIHandler = perspective.addNewUiHandler();
			domainUIHandler.setId(ObjectEntities.DOMAIN);
			domainUIHandler.setUiHandlerClass(DomainBeanUI.class.getName());
			
			BeanFactory networkFactory = perspective.addNewBeanFactory();
			networkFactory.setId(NetBeanFactory.NET_CODENAME);
			networkFactory.setBeanFactoryClass(NetBeanFactory.class.getName());
			
			UiHandler networkUIHandler = perspective.addNewUiHandler();
			networkUIHandler.setId(NetBeanFactory.NET_CODENAME);
			networkUIHandler.setUiHandlerClass(NetBeanUI.class.getName());
			
			Validator domainValidator = perspective.addNewValidator();
			domainValidator.setSource(ObjectEntities.DOMAIN);
			domainValidator.setTarget(ObjectEntities.DOMAIN);
			
			Validator netDomainValidator = perspective.addNewValidator();
			netDomainValidator.setSource(NetBeanFactory.NET_CODENAME);
			netDomainValidator.setTarget(ObjectEntities.DOMAIN);
	    }	
		
		// create domain perspective 
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final Perspective perspective = (Perspective) resource.changeType(Perspective.type);
			perspective.setId(ObjectEntities.DOMAIN);
			
			UiHandler networkUIHandler = perspective.addNewUiHandler();
			networkUIHandler.setId(NetBeanFactory.NET_CODENAME);
			networkUIHandler.setUiHandlerClass(NetBeanUI.class.getName());
			
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
			
			perspective.addUndeletable(NetBeanFactory.NET_CODENAME);
	    }
		
		// create user perspective 
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final Perspective perspective = (Perspective) resource.changeType(Perspective.type);
			perspective.setId(ObjectEntities.SYSTEMUSER);
			
			UiHandler userUIHandler = perspective.addNewUiHandler();
			userUIHandler.setId(ObjectEntities.SYSTEMUSER);
			userUIHandler.setUiHandlerClass(SystemUserBeanUI.class.getName());
			
			BeanFactory permissionFactory = perspective.addNewBeanFactory();
			permissionFactory.setId(ObjectEntities.PERMATTR);
			permissionFactory.setBeanFactoryClass(PermissionBeanFactory.class.getName());
			
			UiHandler permissionUIHandler = perspective.addNewUiHandler();
			permissionUIHandler.setId(ObjectEntities.PERMATTR);
			permissionUIHandler.setUiHandlerClass(UserPermissionBeanUI.class.getName());
			
			for(final Module module : Module.getValueList()){
				final String codename = module.getCodename();
				BeanFactory modulePermissionFactory = perspective.addNewBeanFactory();
				modulePermissionFactory.setId(codename);
				modulePermissionFactory.setBeanFactoryClass(PermissionBeanFactory.class.getName());
				
				UiHandler modulePermissionUIHandler = perspective.addNewUiHandler();
				modulePermissionUIHandler.setId(codename);
				modulePermissionUIHandler.setUiHandlerClass(UserPermissionBeanUI.class.getName());
				
				Validator permissionUserValidator = perspective.addNewValidator();
				permissionUserValidator.setSource(codename);
				permissionUserValidator.setTarget(ObjectEntities.SYSTEMUSER);
			}
			
			Validator permissionUserValidator = perspective.addNewValidator();
			permissionUserValidator.setSource(ObjectEntities.PERMATTR);
			permissionUserValidator.setTarget(ObjectEntities.SYSTEMUSER);
			
			perspective.addUndeletable(ObjectEntities.SYSTEMUSER);
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
			
			perspective.addUndeletable(ObjectEntities.ROLE);
	    }
		
		// create role perspective 
		{			
			final ManagerResource resource = managerExtensions.addNewManagerResource();
			final Perspective perspective = (Perspective) resource.changeType(Perspective.type);
			perspective.setId("messages");
			
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

