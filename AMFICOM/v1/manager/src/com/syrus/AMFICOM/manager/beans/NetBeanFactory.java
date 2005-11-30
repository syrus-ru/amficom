/*-
 * $Id: NetBeanFactory.java,v 1.3 2005/11/30 13:15:27 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.util.Log;



/**
 * @version $Revision: 1.3 $, $Date: 2005/11/30 13:15:27 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class NetBeanFactory extends AbstractBeanFactory<NonStorableBean> {
	
	public static final String NET_CODENAME = "Net";
	
	public NetBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Net", 
			"Manager.Entity.Net");
		super.graphText = graphText;
	}
	
	@Override
	public NonStorableBean createBean(final Perspective perspective) 
	throws ApplicationException {
//		return this.createBean(NET_CODENAME + super.count);
		throw new UnsupportedOperationException(
			"NetBeanFactory.createBean() is unsupported");
	}

	@Override
	public NonStorableBean createBean(final String codename) 
	throws ApplicationException {
		++super.count;
		assert Log.debugMessage(codename, Log.DEBUGLEVEL03);
		final Identifier domainId = new Identifier(codename.replaceFirst(NET_CODENAME, ""));
		final Domain domain = StorableObjectPool.getStorableObject(domainId, true); 
		final NonStorableBean bean = new NetBean(domain);
		bean.setManagerMainFrame(super.graphText);
		bean.setId(codename);
		bean.setName(this.getName() + '(' + domain.getName() + ')');
		bean.setIdentifier(Identifier.VOID_IDENTIFIER);
		return bean;
	}
	
	@Override
	public String getCodename() {
		return NET_CODENAME + ObjectEntities.DOMAIN;
	}
}
