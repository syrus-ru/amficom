/*-
 * $Id: MCMBeanFactory.java,v 1.14 2005/10/18 15:10:38 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.14 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBeanFactory extends IdentifiableBeanFactory<MCMBean> {
	
	private static MCMBeanFactory instance;
	
	private MCMBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.MeasurementContolModule", 
			"Manager.Entity.MeasurementContolModule.acronym");
		super.graphText = graphText;
	}
	
	public static final synchronized  MCMBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new MCMBeanFactory(graphText);
		}		
		return instance;
	}

	@Override
	public MCMBean createBean(Perspective perspective) 
	throws ApplicationException {
		
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		String name = I18N.getString("Manager.Entity.MeasurementContolModule") + "-" + (++super.count);
		
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
	public MCMBean createBean(final Identifier identifier) throws ApplicationException {
		final MCMBean bean = new MCMBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(identifier);			
		
		Dispatcher dispatcher = super.graphText.getDispatcher();
//		dispatcher.addPropertyChangeListener(
//			PROPERTY_USERS_REFRESHED,
//			new PropertyChangeListener() {
//
//				public void propertyChange(PropertyChangeEvent evt) {
//					((WrapperedPropertyTable)bean.table).updateModel();
//				}
//			});
//		
//		dispatcher.addPropertyChangeListener(
//			PROPERTY_SERVERS_REFRESHED,
//			new PropertyChangeListener() {
//
//				public void propertyChange(PropertyChangeEvent evt) {
//					((WrapperedPropertyTable)bean.table).updateModel();
//				}
//			});
//
//		bean.table = super.getTable(bean, 
//			MCMBeanWrapper.getInstance(dispatcher),
//			new String[] { KEY_NAME, 
//				KEY_DESCRIPTION, 
//				KEY_HOSTNAME,
//				KEY_SERVER_ID,
//				KEY_USER_ID});
//		bean.addPropertyChangeListener(this.listener);
//		bean.setPropertyPanel(this.panel);
		
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
