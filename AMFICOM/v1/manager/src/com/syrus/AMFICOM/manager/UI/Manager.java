/*-
* $Id: Manager.java,v 1.27 2005/12/14 15:52:41 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.util.Date;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resources.ResourceHandler;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2005/12/14 15:52:41 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Manager extends AbstractApplication {
	private static final String APPLICATION_NAME = "administration";

	
	public Manager() {
		super(APPLICATION_NAME);

	}
	
	@Override
	protected void init() {
		final ExtensionLauncher extensionLauncher = ExtensionLauncher.getInstance();
		final ClassLoader classLoader = Manager.class.getClassLoader();
		extensionLauncher.addExtensions(classLoader.getResource("xml/manager.xml"));
		
		extensionLauncher.addExtensions(classLoader.getResource("xml/resources.xml"));
		
		extensionLauncher.getExtensionHandler(ResourceHandler.class.getName());
		
		
		super.aContext.setApplicationModel(new ManagerModel(super.aContext));

		
		final boolean xmlSession = ApplicationProperties.getBoolean(XMLSESSION_KEY, false);
		if (xmlSession) {
			assert Log.debugMessage("XML session" , Log.DEBUGLEVEL10);
			try {
				this.initUser();
				this.loadDomainMembers();
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}		
		}
		
		final ManagerMainFrame managerMainFrame = new ManagerMainFrame(super.aContext);
		
		final ImageIcon imageIcon = (ImageIcon)UIManager.getIcon("com.syrus.AMFICOM.icon.administrate");
		super.startMainFrame(managerMainFrame, 
			imageIcon.getImage());
		if (xmlSession) {
			managerMainFrame.loggedIn();
		}
	}

	
	private void loadDomainMembers() throws ApplicationException {
		// XXX see bug # 312
		final Set<Domain> domains = 
			StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.DOMAIN_CODE), true);
		
		final Set<Identifier> domainIds = Identifier.createIdentifiers(domains);
		
		StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(domainIds, ObjectEntities.KIS_CODE), true);
		
		StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(domainIds, ObjectEntities.MCM_CODE), true);
		StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(domainIds, ObjectEntities.SERVER_CODE), true);
		StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(domainIds, ObjectEntities.PERMATTR_CODE), true);
		
	}
	
	private void initUser() throws ApplicationException {
		final Set<SystemUser> systemUserWithLoginSys = 
			StorableObjectPool.getStorableObjectsByCondition(
				new TypicalCondition("sys", 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.SYSTEMUSER_CODE, 
				SystemUserWrapper.COLUMN_LOGIN), true);

		assert !systemUserWithLoginSys.isEmpty() : "There is no sys user";
		
		LoginManager.setUserId(systemUserWithLoginSys.iterator().next().getId());

		final Domain domain = StorableObjectPool.getStorableObject(new Identifier("Domain_0"), true);
		LoginManager.setDomainId(domain.getId());
		
		LoginManager.setIdlSessionKey(new IdlSessionKey(new Date().toString()));		
	}
	
	public static void main(String[] args) {
		Launcher.launchApplicationClass(Manager.class);
	}
	
}
