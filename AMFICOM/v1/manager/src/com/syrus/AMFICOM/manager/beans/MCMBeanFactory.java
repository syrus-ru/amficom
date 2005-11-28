/*-
 * $Id: MCMBeanFactory.java,v 1.2 2005/11/28 14:47:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.MCM;
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
public final class MCMBeanFactory extends IdentifiableBeanFactory<MCMBean> {
	
	public MCMBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.MeasurementContolModule", 
			"Manager.Entity.MeasurementContolModule.acronym");
		super.graphText = graphText;
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
		bean.setManagerMainFrame(super.graphText);
		bean.setId(identifier.getIdentifierString());
		bean.setIdentifier(identifier);			
		
		final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
		final Dispatcher dispatcher = managerModel.getDispatcher();
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
	
	@Override
	public String getCodename() {
		return ObjectEntities.MCM;
	}
}
