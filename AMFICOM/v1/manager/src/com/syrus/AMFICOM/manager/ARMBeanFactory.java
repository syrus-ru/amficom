/*-
 * $Id: ARMBeanFactory.java,v 1.4 2005/07/15 11:59:00 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;



/**
 * @version $Revision: 1.4 $, $Date: 2005/07/15 11:59:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class ARMBeanFactory extends AbstractBeanFactory {
	
	private static ARMBeanFactory instance;
	
	private int count = 0;
	
	private Validator validator;
	
	private ARMBeanFactory() {
		super("Entity.AutomatedWorkplace", 
			"Entity.AutomatedWorkplace.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/arm.gif", 
			"com/syrus/AMFICOM/manager/resources/arm.gif");
	}
	
	public static final ARMBeanFactory getInstance() {
		if(instance == null) {
			synchronized (ARMBeanFactory.class) {
				if(instance == null) {
					instance = new ARMBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {
		AbstractBean bean = new AbstractBean() {};
		
		bean.setValidator(this.getValidator());
		bean.setStorableObject("ARM" + (++this.count));
		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("ARMBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getStorableObject() 
						+ " -> " 
						+ targetBean.getStorableObject());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getStorableObject().startsWith("ARM") &&
						targetBean.getStorableObject().startsWith("Net");
				}
			};
		}
		return this.validator;
	}
}
