/*-
 * $Id: RTUBeanFactory.java,v 1.7 2005/08/02 14:42:06 bob Exp $
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

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/02 14:42:06 $
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
	public AbstractBean createBean() 
	throws CreateObjectException, IllegalObjectEntityException {
		RTUBean bean = new RTUBean();
		bean.setCodeName("RTU");
		bean.setValidator(this.getValidator());
		
		KIS kis = KIS.createInstance(LoginManager.getUserId(),
			Identifier.VOID_IDENTIFIER,
			"",
			"",
			"",
			(short)0,
			Identifier.VOID_IDENTIFIER,
			Identifier.VOID_IDENTIFIER);
		StorableObjectPool.putStorableObject(kis);
		bean.setId(kis.getId());	
		
		bean.table = super.getTable(bean, 
			RTUBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_MCM_ID,
				KEY_HOSTNAME,
				KEY_PORT});
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
						sourceBean.getCodeName().equals("RTU") &&
						targetBean.getCodeName().equals("Net");
				}
			};
		}
		return this.validator;
	}
	
}
