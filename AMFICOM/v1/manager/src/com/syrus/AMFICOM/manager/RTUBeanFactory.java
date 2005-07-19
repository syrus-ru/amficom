/*-
 * $Id: RTUBeanFactory.java,v 1.4 2005/07/19 09:49:00 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;



/**
 * @version $Revision: 1.4 $, $Date: 2005/07/19 09:49:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class RTUBeanFactory extends AbstractBeanFactory {
	
	private static RTUBeanFactory instance;
	
	private Validator validator;
	
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
	public AbstractBean createBean() {
		AbstractBean bean = new AbstractBean() {};
		bean.setCodeName("RTU");
		bean.setValidator(this.getValidator());
		bean.setName("RTU" + (++this.count));
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
