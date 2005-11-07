/*-
 * $Id: ServerBeanFactory.java,v 1.13 2005/11/07 15:24:19 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

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



/**
 * @version $Revision: 1.13 $, $Date: 2005/11/07 15:24:19 $
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
		bean.setGraphText(super.graphText);
		bean.setIdentifier(identifier);
		bean.setId(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
//		bean.table = super.getTable(bean, 
//			ServerBeanWrapper.getInstance(),
//			new String[] { KEY_NAME, 
//				KEY_DESCRIPTION, 
//				KEY_HOSTNAME});
//		bean.addPropertyChangeListener(this.listener);
//		bean.setPropertyPanel(this.panel);
		
		final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
		final Dispatcher dispatcher = managerModel.getDispatcher();
		dispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.SERVER, null, bean));
		
		return bean;
	}

	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getId().startsWith(ObjectEntities.SERVER) &&
						targetBean.getId().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.SERVER;
	}
}
