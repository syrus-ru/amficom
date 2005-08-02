/*-
 * $Id: MCMBeanFactory.java,v 1.5 2005/08/02 14:42:06 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.MCMBeanWrapper.*;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;


/**
 * @version $Revision: 1.5 $, $Date: 2005/08/02 14:42:06 $
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
	public AbstractBean createBean() 
	throws CreateObjectException, IllegalObjectEntityException {
		MCMBean bean = new MCMBean();
		bean.setCodeName("MCM");
		bean.setValidator(this.getValidator());
		MCM mcm = MCM.createInstance(LoginManager.getUserId(), 
			Identifier.VOID_IDENTIFIER,
			"",
			"",
			"",
			Identifier.VOID_IDENTIFIER,
			Identifier.VOID_IDENTIFIER);
		StorableObjectPool.putStorableObject(mcm);
		bean.setId(mcm.getId());					

		bean.table = super.getTable(bean, 
			MCMBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID});
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
						sourceBean.getCodeName().equals("MCM") &&
						targetBean.getCodeName().equals("Net");
				}
			};
		}
		return this.validator;
	}
}
