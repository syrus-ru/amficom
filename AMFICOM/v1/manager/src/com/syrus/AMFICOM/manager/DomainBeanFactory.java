/*-
 * $Id: DomainBeanFactory.java,v 1.7 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.Identifier;



/**
 * @version $Revision: 1.7 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBeanFactory extends TabledBeanFactory {
	
	private static DomainBeanFactory instance;
	
	private DomainBeanFactory() {
		super("Entity.Domain", 
			"Entity.Domain", 
			"com/syrus/AMFICOM/manager/resources/icons/cloud.gif", 
			"com/syrus/AMFICOM/manager/resources/cloud.png");
	}
	
	public static final DomainBeanFactory getInstance() {
		if(instance == null) {
			synchronized (DomainBeanFactory.class) {
				if(instance == null) {
					instance = new DomainBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {
		DomainBean bean = new DomainBean();
		
		bean.setCodeName("Domain");
		bean.setValidator(this.getValidator());
		bean.setName("Domain_" + (++this.count));		
		bean.setId(new Identifier(bean.getName()));		
		bean.table = super.getTable(bean, 
			DomainBeanWrapper.getInstance(),
			new String[] {
				DomainBeanWrapper.KEY_NAME,
				DomainBeanWrapper.KEY_DESCRIPTION}
			);
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
						(sourceBean.getCodeName().equals("Domain") ||
						 sourceBean.getCodeName().equals("Net")) &&
						targetBean.getCodeName().equals("Domain");
				}
			};
		}
		return this.validator;
	}
}
