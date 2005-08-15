/*-
 * $Id: RTUBeanFactory.java,v 1.11 2005/08/15 14:20:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_MCM_ID;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_NAME;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_PORT;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.PROPERTY_MCMS_REFRESHED;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.11 $, $Date: 2005/08/15 14:20:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBeanFactory extends TabledBeanFactory {
	
	private static RTUBeanFactory instance;
	
	private RTUBeanFactory() {
		super("Entity.RemoteTestUnit", 
			"Entity.RemoteTestUnit.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/rtu.gif", 
			"com/syrus/AMFICOM/manager/resources/rtu.png");
	}
	
	public static final RTUBeanFactory getInstance() {
		if(instance == null) {
			synchronized (RTUBeanFactory.class) {
				if(instance == null) {
					instance = new RTUBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) 
	throws CreateObjectException, IllegalObjectEntityException {
		
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		KIS kis = KIS.createInstance(LoginManager.getUserId(),
			domainPerpective.getDomainId(),
			"",
			"",
			"",
			(short)0,
			Identifier.VOID_IDENTIFIER,
			Identifier.VOID_IDENTIFIER);
		return this.createBean(kis.getId());
	}
	
	@Override
	public AbstractBean createBean(Identifier identifier) {
		final RTUBean bean = new RTUBean();
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(identifier);	
		

		
		bean.table = super.getTable(bean, 
			RTUBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_MCM_ID,
				KEY_HOSTNAME,
				KEY_PORT});
		
		JGraphText.entityDispatcher.addPropertyChangeListener(
			PROPERTY_MCMS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					bean.table.updateModel();
				}
			});
		
		bean.addPropertyChangeListener(this.listener);
		bean.setPropertyPanel(this.panel);
		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ObjectEntities.KIS) &&
						targetBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}
	
}
