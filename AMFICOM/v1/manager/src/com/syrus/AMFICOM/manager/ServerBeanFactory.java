/*-
 * $Id: ServerBeanFactory.java,v 1.11 2005/10/11 15:34:53 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.ServerBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.ServerBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.ServerBeanWrapper.KEY_NAME;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;



/**
 * @version $Revision: 1.11 $, $Date: 2005/10/11 15:34:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBeanFactory extends TabledBeanFactory {
	
	private static ServerBeanFactory instance;
	
	private ServerBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Server", 
			"Manager.Entity.Server", 
			"com/syrus/AMFICOM/manager/resources/icons/server.gif", 
			"com/syrus/AMFICOM/manager/resources/server.png");
		super.graphText = graphText;
	}
	
	public static final synchronized ServerBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new ServerBeanFactory(graphText);
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) 
	throws IllegalObjectEntityException, CreateObjectException {
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
	protected AbstractBean createBean(Identifier identifier) {
		final ServerBean bean = new ServerBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setId(identifier);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.table = super.getTable(bean, 
			ServerBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME});
		bean.addPropertyChangeListener(this.listener);
		bean.setPropertyPanel(this.panel);
		
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
