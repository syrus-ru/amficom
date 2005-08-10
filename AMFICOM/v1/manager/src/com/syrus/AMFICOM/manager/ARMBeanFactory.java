/*-
 * $Id: ARMBeanFactory.java,v 1.8 2005/08/10 14:02:25 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.Identifier;



/**
 * @version $Revision: 1.8 $, $Date: 2005/08/10 14:02:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ARMBeanFactory extends AbstractBeanFactory {
	
	private static ARMBeanFactory instance;
	
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
	public AbstractBean createBean(Perspective perspective) {
		AbstractBean bean = new NonStorableBean();
		
		bean.setValidator(this.getValidator());
		bean.setCodeName("ARM");
		bean.setName("ARM" + (++this.count));
		
		return bean;
	}
	
	@Override
	public AbstractBean createBean(Identifier identifier) {
		throw new UnsupportedOperationException();
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("ARMBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getName() 
						+ " -> " 
						+ targetBean.getName());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().equals("ARM") &&
						targetBean.getCodeName().equals("Net");
				}
			};
		}
		return this.validator;
	}
}
