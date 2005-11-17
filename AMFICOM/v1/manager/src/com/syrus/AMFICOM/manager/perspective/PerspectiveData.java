/*-
* $Id: PerspectiveData.java,v 1.1 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.viewers.BeanUI;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PerspectiveData {
	private final Map<String, AbstractBeanFactory> factory;
	private final Map<String, BeanUI> beanUI;
	private final Map<String, AbstractItemPopupMenu> popupMenus;
	private final Map<String, PerspectiveData> subperspectiveMap;
	private final Set<String> undeletable;
	private final Validator validator;
	
	public PerspectiveData(final Map<String, AbstractBeanFactory> factory,
		final Map<String, BeanUI> beanUI,
		final Map<String, AbstractItemPopupMenu> popupMenus,
		Map<String, PerspectiveData> subperspectiveMap,
		final Set<String> undeletable,
		final Validator validator) {
		this.factory = factory;
		this.beanUI = beanUI;
		this.popupMenus = popupMenus;
		this.subperspectiveMap = subperspectiveMap;
		this.undeletable = undeletable;
		this.validator = validator;
	}
	
	public AbstractBeanFactory getBeanFactory(final String factoryId) {
		return this.factory.get(factoryId);
	}
	
	public BeanUI getBeanUI(final String beanUiId) {
		return this.beanUI.get(beanUiId);
	}
	
	public boolean isBeanUISupported(final String beanUiId) {
		return this.beanUI.containsKey(beanUiId);
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
	
	public PerspectiveData getSubperspectiveData(final String codename) {		
		final PerspectiveData perspectiveData = this.subperspectiveMap.get(codename);
		return perspectiveData;
	}
}

