/*-
 * $Id: ServerBeanFactory.java,v 1.12 2005/10/18 15:10:38 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;



/**
 * @version $Revision: 1.12 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBeanFactory extends IdentifiableBeanFactory<ServerBean> {
	
	private static ServerBeanFactory instance;
	
	private ServerBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Server", 
			"Manager.Entity.Server");
		super.graphText = graphText;
	}
	
	public static final synchronized ServerBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new ServerBeanFactory(graphText);
		}		
		return instance;
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
		bean.setId(identifier);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
//		bean.table = super.getTable(bean, 
//			ServerBeanWrapper.getInstance(),
//			new String[] { KEY_NAME, 
//				KEY_DESCRIPTION, 
//				KEY_HOSTNAME});
//		bean.addPropertyChangeListener(this.listener);
//		bean.setPropertyPanel(this.panel);
		
		super.graphText.getDispatcher().firePropertyChange(
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
						sourceBean.getCodeName().startsWith(ObjectEntities.SERVER) &&
						targetBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME);
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
