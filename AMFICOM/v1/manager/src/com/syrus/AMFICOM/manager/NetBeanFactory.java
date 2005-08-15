/*-
 * $Id: NetBeanFactory.java,v 1.10 2005/08/15 14:20:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;



/**
 * @version $Revision: 1.10 $, $Date: 2005/08/15 14:20:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NetBeanFactory extends AbstractBeanFactory {
	
	public static final String NET_CODENAME = "Net";
	
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
	public AbstractBean createBean(Perspective perspective) {
		return this.createBean(NET_CODENAME + super.count);
	}

	@Override
	public AbstractBean createBean(final String codename) {
		++super.count;
		AbstractBean bean = new NonStorableBean();
		bean.setCodeName(codename);
		bean.setValidator(this.getValidator());
		bean.setId(Identifier.VOID_IDENTIFIER);
		return bean;
	}
	
	private final Validator getValidator() {
		if (this.validator == null) {
			 this.validator = new Validator() {
					
					public boolean isValid(	AbstractBean sourceBean,
											AbstractBean targetBean) {
						
						return sourceBean != null && 
							targetBean != null && 
							sourceBean.getCodeName().startsWith(NET_CODENAME) &&
							targetBean.getCodeName().startsWith(ObjectEntities.DOMAIN);
					}
				};
		}
		return this.validator;
	}
}
