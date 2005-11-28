/*-
 * $Id: RTUBeanFactory.java,v 1.2 2005/11/28 14:47:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.perspective.DomainPerpective;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.measurement.KIS;

/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class RTUBeanFactory extends IdentifiableBeanFactory<RTUBean> {
	
	public RTUBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.RemoteTestUnit", 
			"Manager.Entity.RemoteTestUnit.acronym");
		super.graphText = graphText;
	}
	
	@Override
	public RTUBean createBean(Perspective perspective) 
	throws ApplicationException {
		
		final DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		final KIS kis = KIS.createInstance(LoginManager.getUserId(),
			domainPerpective.getDomainId(),
			"",
			"",
			"",
			(short)0,
			Identifier.VOID_IDENTIFIER,
			Identifier.VOID_IDENTIFIER);
		return this.createBean(kis.getId());
	}
	
	@Override
	public RTUBean createBean(Identifier identifier) 
	throws ApplicationException {
		final RTUBean bean = new RTUBean();
		++super.count;
		bean.setManagerMainFrame(super.graphText);
		bean.setId(identifier.getIdentifierString());
		bean.setIdentifier(identifier);		

		return bean;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.KIS;
	}
}
