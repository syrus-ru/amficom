/*-
 * $Id: RTUBeanFactory.java,v 1.15 2005/09/12 12:06:26 bob Exp $
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

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.measurement.KIS;

/**
 * @version $Revision: 1.15 $, $Date: 2005/09/12 12:06:26 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBeanFactory extends TabledBeanFactory {
	
	private static RTUBeanFactory instance;
	
	private RTUBeanFactory(final ManagerMainFrame graphText) {
		super("Entity.RemoteTestUnit", 
			"Entity.RemoteTestUnit.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/rtu.gif", 
			"com/syrus/AMFICOM/manager/resources/rtu.png");
		super.graphText = graphText;
	}
	
	public static final synchronized RTUBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new RTUBeanFactory(graphText);
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
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(identifier);	
		

		
		bean.table = super.getTable(bean, 
			RTUBeanWrapper.getInstance(bean.graphText.getDispatcher()),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_MCM_ID,
				KEY_HOSTNAME,
				KEY_PORT});
		
		bean.graphText.getDispatcher().addPropertyChangeListener(
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
	
	@Override
	public String getCodename() {
		return ObjectEntities.KIS;
	}
}
