/*-
 * $Id: RTUBeanFactory.java,v 1.17 2005/10/18 15:10:38 bob Exp $
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
 * @version $Revision: 1.17 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBeanFactory extends IdentifiableBeanFactory<RTUBean> {
	
	private static RTUBeanFactory instance;
	
	private RTUBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.RemoteTestUnit", 
			"Manager.Entity.RemoteTestUnit.acronym");
		super.graphText = graphText;
	}
	
	public static final synchronized RTUBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new RTUBeanFactory(graphText);
		}		
		return instance;
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
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(identifier);		

		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ObjectEntities.KIS) &&
						targetBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME);
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
