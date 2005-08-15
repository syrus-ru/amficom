/*-
 * $Id: DomainBeanFactory.java,v 1.10 2005/08/15 14:20:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;



/**
 * @version $Revision: 1.10 $, $Date: 2005/08/15 14:20:05 $
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
	public AbstractBean createBean(Perspective perspective) throws IllegalObjectEntityException, 
	CreateObjectException {		
		Domain domain = Domain.createInstance(LoginManager.getUserId(), 
				Identifier.VOID_IDENTIFIER, "", "");
		return this.createBean(domain.getId());
	}
	
	@Override
	protected AbstractBean createBean(final Identifier id) {
		DomainBean bean = new DomainBean();
		
		bean.setCodeName(id.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(id);		
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
						(sourceBean.getCodeName().startsWith(ObjectEntities.DOMAIN) ||
						 sourceBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME) &&
						targetBean.getCodeName().startsWith(ObjectEntities.DOMAIN));
				}
			};
		}
		return this.validator;
	}
}
