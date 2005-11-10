/*-
* $Id: PerspectiveData.java,v 1.2 2005/11/10 13:59:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.manager.viewers.BeanUI;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/10 13:59:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PerspectiveData {
	private final Map<String, AbstractBeanFactory> factory;
	private final Map<String, BeanUI> beanUI;
	private final Set<String> undeletable;
	private final Validator validator;
	
	public PerspectiveData(final Map<String, AbstractBeanFactory> factory,
		final Map<String, BeanUI> beanUI,
		final Set<String> undeletable,
		final Validator validator) {
		this.factory = factory;
		this.beanUI = beanUI;
		this.undeletable = undeletable;
		this.validator = validator;
	}
	
	public AbstractBeanFactory getBeanFactory(final String factoryId) {
		return this.factory.get(factoryId);
	}
	
	public BeanUI getBeanUI(final String beanUiId) {
		return this.beanUI.get(beanUiId);
	}
	
	public boolean isUndeletable(final String codename) {
		return this.undeletable.contains(codename);
	}
	
	public final Validator getValidator() {
		return this.validator;
	}
	
	
}

