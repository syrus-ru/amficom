/*-
 * $Id: NetBeanFactory.java,v 1.4 2005/07/15 11:59:00 bob Exp $
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
public class NetBeanFactory extends AbstractBeanFactory {
	
	private static NetBeanFactory instance;
	
	private int count = 0;
	
	private Validator validator;
	
	private NetBeanFactory() {
		super("Entity.Net", 
			"Entity.Net", 
			"com/syrus/AMFICOM/manager/resources/icons/cloud.gif", 
			"com/syrus/AMFICOM/manager/resources/cloud.png");
	}
	
	public static final NetBeanFactory getInstance() {
		if(instance == null) {
			synchronized (NetBeanFactory.class) {
				if(instance == null) {
					instance = new NetBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {		
		AbstractBean bean = new AbstractBean() {};
		bean.setStorableObject("Net" + (++this.count));
		bean.setValidator(this.getValidator());
		
		return bean;
	}

	private final Validator getValidator() {
		if (this.validator == null) {
			 this.validator = new Validator() {
					
					public boolean isValid(	AbstractBean sourceBean,
											AbstractBean targetBean) {
						
						return false;
					}
				};
		}
		return this.validator;
	}
}
