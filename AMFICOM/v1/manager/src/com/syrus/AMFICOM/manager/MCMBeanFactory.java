/*-
 * $Id: MCMBeanFactory.java,v 1.7 2005/08/11 13:06:12 bob Exp $
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.JGraphText;


/**
 * @version $Revision: 1.7 $, $Date: 2005/08/11 13:06:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBeanFactory extends TabledBeanFactory {
	
	private static MCMBeanFactory instance;
	
	private MCMBeanFactory() {
		super("Entity.MeasurementContolModule", 
			"Entity.MeasurementContolModule.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/mcm.gif", 
			"com/syrus/AMFICOM/manager/resources/mcm.png");
	}
	
	public static final MCMBeanFactory getInstance() {
		if(instance == null) {
			synchronized (MCMBeanFactory.class) {
				if(instance == null) {
					instance = new MCMBeanFactory();
				}
			}
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
		bean.setCodeName("MCM");
		bean.setValidator(this.getValidator());
		bean.setId(identifier);			
		
		JGraphText.entityDispatcher.addPropertyChangeListener(
			"usersRefreshed",
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					bean.table.updateModel();
				}
			});
		
		JGraphText.entityDispatcher.addPropertyChangeListener(
			"serversRefreshed",
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					bean.table.updateModel();
				}
			});

		bean.table = super.getTable(bean, 
			MCMBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID});
		bean.addPropertyChangeListener(this.listener);
		bean.setPropertyPanel(this.panel);
		
		JGraphText.entityDispatcher.firePropertyChange(
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
						sourceBean.getCodeName().equals("MCM") &&
						targetBean.getCodeName().equals("Net");
				}
			};
		}
		return this.validator;
	}
}
