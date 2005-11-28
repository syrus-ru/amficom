/*-
* $Id: Manager.java,v 1.18 2005/11/28 14:47:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Toolkit;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.event.DeliveryAttributes;
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
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2005/11/28 14:47:04 $
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
		extensionLauncher.addExtensions("xml/manager.xml");	
		super.aContext.setApplicationModel(new ManagerModel(super.aContext));

		
		final boolean xmlSession = ApplicationProperties.getBoolean(XMLSESSION_KEY, false);
		if (xmlSession) {
			assert Log.debugMessage("XML session" , Log.DEBUGLEVEL10);
			try {
				this.initUser();
				this.createDeliveryAttributes();
				this.loadDomainMembers();
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}		
		}
		
		final ManagerMainFrame managerMainFrame = new ManagerMainFrame(super.aContext);
		super.startMainFrame(managerMainFrame, 
			Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif"));
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
	}
	
	public static void main(String[] args) {
		Launcher.launchApplicationClass(Manager.class);
	}
	
	void createDeliveryAttributes() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();
		final DeliveryAttributes attributes = 
			DeliveryAttributes.getInstance(userId, Severity.SEVERITY_SOFT);
		final Set<Role> roles = StorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ObjectEntities.ROLE_CODE),
			true);
		
		attributes.setRoles(roles);
		
		
		assert Log.debugMessage("Created " + attributes , Log.DEBUGLEVEL03);
	}
	
	
}
