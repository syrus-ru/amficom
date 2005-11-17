/*-
* $Id: RoleBeanFactory.java,v 1.1 2005/11/17 09:00:32 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class RoleBeanFactory extends AbstractBeanFactory<RoleBean> {

	public RoleBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Role", 
			"Manager.Entity.Role.acronym");
		super.graphText = graphText;
	}
	

	@Override
	public RoleBean createBean(final Perspective perspective) 
	throws ApplicationException {
		
		final Role permissionAttributes = 
			Role.createInstance(LoginManager.getUserId(),
				"",
				"");
		
		return this.createBean(permissionAttributes.getId());

	}
	
	@Override
	public RoleBean createBean(final String codename) 
	throws ApplicationException {
		return this.createBean(new Identifier(codename));
	}
	
	protected RoleBean createBean(final Identifier identifier) 
	throws ApplicationException {
		
		final RoleBean bean = new RoleBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setId(identifier.getIdentifierString());
		
		bean.setIdentifier(identifier);
		final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
		Dispatcher dispatcher = managerModel.getDispatcher();
		dispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.PERMATTR, null, bean));
		
		return bean;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.ROLE;
	}
}
