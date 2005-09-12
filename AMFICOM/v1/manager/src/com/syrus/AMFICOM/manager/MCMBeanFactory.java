/*-
 * $Id: MCMBeanFactory.java,v 1.12 2005/09/12 12:06:26 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_NAME;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_SERVER_ID;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_USER_ID;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.PROPERTY_SERVERS_REFRESHED;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.PROPERTY_USERS_REFRESHED;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.12 $, $Date: 2005/09/12 12:06:26 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBeanFactory extends TabledBeanFactory {
	
	private static MCMBeanFactory instance;
	
	private MCMBeanFactory(final ManagerMainFrame graphText) {
		super("Entity.MeasurementContolModule", 
			"Entity.MeasurementContolModule.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/mcm.gif", 
			"com/syrus/AMFICOM/manager/resources/mcm.png");
		super.graphText = graphText;
	}
	
	public static final synchronized  MCMBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new MCMBeanFactory(graphText);
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) 
	throws CreateObjectException, IllegalObjectEntityException {
		
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		String name = LangModelManager.getString("Entity.MeasurementContolModule") + "-" + (++super.count);
		
		MCM mcm = MCM.createInstance(LoginManager.getUserId(), 
			domainPerpective.getDomainId(),
			name,
			"",
			"",
			Identifier.VOID_IDENTIFIER,
			Identifier.VOID_IDENTIFIER);

		return this.createBean(mcm.getId());
	}	

	@Override
	public AbstractBean createBean(Identifier identifier) {
		final MCMBean bean = new MCMBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(identifier);			
		
		Dispatcher dispatcher = super.graphText.getDispatcher();
		
		dispatcher.addPropertyChangeListener(
			PROPERTY_USERS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					bean.table.updateModel();
				}
			});
		
		dispatcher.addPropertyChangeListener(
			PROPERTY_SERVERS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					bean.table.updateModel();
				}
			});

		bean.table = super.getTable(bean, 
			MCMBeanWrapper.getInstance(dispatcher),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID});
		bean.addPropertyChangeListener(this.listener);
		bean.setPropertyPanel(this.panel);
		
		dispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.MCM, null, bean));
		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ObjectEntities.MCM) &&
						targetBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.MCM;
	}
}
