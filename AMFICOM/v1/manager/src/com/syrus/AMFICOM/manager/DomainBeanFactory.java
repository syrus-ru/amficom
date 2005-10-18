/*-
 * $Id: DomainBeanFactory.java,v 1.18 2005/10/18 15:10:39 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;



/**
 * @version $Revision: 1.18 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBeanFactory extends IdentifiableBeanFactory<DomainBean> {
	
	private static DomainBeanFactory instance;
	
	private DomainBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Domain", 
			"Manager.Entity.Domain");
		super.graphText = graphText;
	}
	
	public static final synchronized DomainBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new DomainBeanFactory(graphText);
		}		
		return instance;
	}

	@Override
	public DomainBean createBean(Perspective perspective) 
	throws ApplicationException {		
		Domain domain = Domain.createInstance(LoginManager.getUserId(), 
				Identifier.VOID_IDENTIFIER, "", "");
		return this.createBean(domain.getId());
	}
	
	@Override
	protected DomainBean createBean(final Identifier id) 
	throws ApplicationException {		
		final DomainBean bean = new DomainBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(id.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(id);		
		return bean;
	}

	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	final AbstractBean sourceBean,
				                       	final AbstractBean targetBean) {
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
	
	@Override
	public final String getCodename() {
		return ObjectEntities.DOMAIN;
	}
}
