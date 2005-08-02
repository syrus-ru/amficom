/*-
 * $Id: NetBeanFactory.java,v 1.8 2005/08/02 14:42:06 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;



/**
 * @version $Revision: 1.8 $, $Date: 2005/08/02 14:42:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NetBeanFactory extends AbstractBeanFactory {
	
	private static NetBeanFactory instance;
	
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
		AbstractBean bean = new NonStorableBean();
		bean.setName("Net" + (++super.count));
		bean.setCodeName("Net");
		bean.setValidator(this.getValidator());
		
		return bean;
	}

	private final Validator getValidator() {
		if (this.validator == null) {
			 this.validator = new Validator() {
					
					public boolean isValid(	AbstractBean sourceBean,
											AbstractBean targetBean) {
						
						return sourceBean != null && 
							targetBean != null && 
							sourceBean.getCodeName().equals("Net") &&
							targetBean.getCodeName().equals("Domain");
					}
				};
		}
		return this.validator;
	}
}
