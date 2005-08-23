/*-
 * $Id: ARMBeanFactory.java,v 1.10 2005/08/23 07:52:33 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/23 07:52:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ARMBeanFactory extends AbstractBeanFactory {
	
	public static final String ARM_CODENAME = "ARM";
	
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
		return this.createBean(ARM_CODENAME + this.count);
	}
	
	@Override
	public AbstractBean createBean(final String codename) {
		++super.count;
		AbstractBean bean = new NonStorableBean();
		bean.setValidator(this.getValidator());
		bean.setCodeName(codename);
		bean.setId(Identifier.VOID_IDENTIFIER);		
		return bean;
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
						sourceBean.getCodeName().startsWith(ARM_CODENAME) &&
						targetBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}	
	
}
