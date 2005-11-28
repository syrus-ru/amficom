/*-
 * $Id: ServerBeanFactory.java,v 1.2 2005/11/28 14:47:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.AMFICOM.manager.perspective.DomainPerpective;
import com.syrus.AMFICOM.manager.perspective.Perspective;



/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ServerBeanFactory extends IdentifiableBeanFactory<ServerBean> {

	public ServerBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Server", 
			"Manager.Entity.Server");
		super.graphText = graphText;
	}
	
	@Override
	public ServerBean createBean(Perspective perspective) 
	throws ApplicationException {
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		String name = I18N.getString("Manager.Entity.Server") + "-" + (++super.count);
		
		Server server = Server.createInstance(LoginManager.getUserId(),
			domainPerpective.getDomainId(),
			name,
			"",
			"");
		
		return this.createBean(server.getId());
	}
	
	@Override
	protected ServerBean createBean(Identifier identifier) 
	throws ApplicationException {
		final ServerBean bean = new ServerBean();
		++super.count;
		bean.setManagerMainFrame(super.graphText);
		bean.setIdentifier(identifier);
		bean.setId(identifier.getIdentifierString());

		final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
		final Dispatcher dispatcher = managerModel.getDispatcher();
		dispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.SERVER, null, bean));
		
		return bean;
	}

	@Override
	public String getCodename() {
		return ObjectEntities.SERVER;
	}
}
