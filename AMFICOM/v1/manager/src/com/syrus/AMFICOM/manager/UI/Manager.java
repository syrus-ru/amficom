/*-
* $Id: Manager.java,v 1.15 2005/11/11 13:47:08 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Toolkit;
import java.util.Set;

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
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @version $Revision: 1.15 $, $Date: 2005/11/11 13:47:08 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Manager extends AbstractApplication {
	private static final String APPLICATION_NAME = "manager";

	
	public Manager() {
		super(APPLICATION_NAME);

	}
	
	@Override
	protected void init() {
		
		
		final ExtensionLauncher extensionLauncher = ExtensionLauncher.getInstance();
		extensionLauncher.addExtensions("xml/manager.xml");	
		super.aContext.setApplicationModel(new ManagerModel(super.aContext));
		
		try {
			TypicalCondition tc = new TypicalCondition("sys", 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.SYSTEMUSER_CODE, 
				SystemUserWrapper.COLUMN_LOGIN);
			Set<SystemUser> systemUserWithLoginSys = 
				StorableObjectPool.getStorableObjectsByCondition(tc, true);

			assert !systemUserWithLoginSys.isEmpty() : "There is no sys user";
			
			LoginManager.setUserId(systemUserWithLoginSys.iterator().next().getId());
			
			createDeliveryAttributes();
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.startMainFrame(new ManagerMainFrame(super.aContext), 
			Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif"));
	}

	public static void main(String[] args) {
		Launcher.launchApplicationClass(Manager.class);
	}
	
	void createDeliveryAttributes() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();
		final DeliveryAttributes attributes = 
			DeliveryAttributes.createInstance(userId, Severity.SEVERITY_SOFT);
		final Set<Role> roles = StorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ObjectEntities.ROLE_CODE),
			true);
		
		attributes.setRoles(roles);
	}
	
	
}
