/*-
 * $Id: DomainBeanFactory.java,v 1.1 2005/11/17 09:00:33 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.perspective.Perspective;



/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class DomainBeanFactory extends IdentifiableBeanFactory<DomainBean> {
	
	public DomainBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Domain", 
			"Manager.Entity.Domain");
		super.graphText = graphText;
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
		bean.setId(id.getIdentifierString());
		bean.setIdentifier(id);		
		return bean;
	}

	@Override
	public final String getCodename() {
		return ObjectEntities.DOMAIN;
	}
}
