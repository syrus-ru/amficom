/*-
 * $Id: ServerBeanFactory.java,v 1.6 2005/08/10 14:02:25 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.ServerBeanWrapper.*;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.JGraphText;



/**
 * @version $Revision: 1.6 $, $Date: 2005/08/10 14:02:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBeanFactory extends TabledBeanFactory {
	
	private static ServerBeanFactory instance;
	
	private ServerBeanFactory() {
		super("Entity.Server", 
			"Entity.Server", 
			"com/syrus/AMFICOM/manager/resources/icons/server.gif", 
			"com/syrus/AMFICOM/manager/resources/server.png");
	}
	
	public static final ServerBeanFactory getInstance() {
		if(instance == null) {
			synchronized (ServerBeanFactory.class) {
				if(instance == null) {
					instance = new ServerBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) 
	throws IllegalObjectEntityException, CreateObjectException {
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		String name = LangModelManager.getString("Entity.Server") + "-" + (++super.count);
		
		Server server = Server.createInstance(LoginManager.getUserId(),
			domainPerpective.getDomainId(),
			name,
			"",
			"");
		
		return this.createBean(server.getId());
	}
	
	@Override
	public AbstractBean createBean(Identifier identifier) {
		ServerBean bean = new ServerBean();
		bean.setId(identifier);
		bean.setCodeName("Server");
		bean.setValidator(this.getValidator());
		bean.table = super.getTable(bean, 
			ServerBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME});
		bean.addPropertyChangeListener(this.listener);
		bean.setPropertyPanel(this.panel);
		
		JGraphText.entityDispatcher.firePropertyChange(
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
						sourceBean.getCodeName().equals("Server") &&
						targetBean.getCodeName().equals("Net");
				}
			};
		}
		return this.validator;
	}
}
