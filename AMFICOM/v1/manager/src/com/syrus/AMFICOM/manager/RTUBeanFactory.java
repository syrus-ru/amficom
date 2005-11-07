/*-
 * $Id: RTUBeanFactory.java,v 1.18 2005/11/07 15:24:19 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.measurement.KIS;

/**
 * @version $Revision: 1.18 $, $Date: 2005/11/07 15:24:19 $
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
		
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		KIS kis = KIS.createInstance(LoginManager.getUserId(),
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
		bean.setGraphText(super.graphText);
		bean.setId(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setIdentifier(identifier);		

		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getId().startsWith(ObjectEntities.KIS) &&
						targetBean.getId().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.KIS;
	}
}
