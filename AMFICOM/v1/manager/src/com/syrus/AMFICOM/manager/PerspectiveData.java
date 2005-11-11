/*-
* $Id: PerspectiveData.java,v 1.3 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.viewers.BeanUI;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PerspectiveData {
	private final Map<String, AbstractBeanFactory> factory;
	private final Map<String, BeanUI> beanUI;
	private final Map<String, AbstractItemPopupMenu> popupMenus;
	private final Set<String> undeletable;
	private final Validator validator;
	
	public PerspectiveData(final Map<String, AbstractBeanFactory> factory,
		final Map<String, BeanUI> beanUI,
		final Map<String, AbstractItemPopupMenu> popupMenus,
		final Set<String> undeletable,
		final Validator validator) {
		this.factory = factory;
		this.beanUI = beanUI;
		this.popupMenus = popupMenus;
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
	
	public Validator getValidator() {
		return this.validator;
	}
	
	public AbstractItemPopupMenu getPopupMenu(final String codename) {
		return this.popupMenus.get(codename);
	}
}

