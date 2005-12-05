/*-
* $Id: PerspectiveData.java,v 1.3 2005/12/05 14:41:22 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/12/05 14:41:22 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PerspectiveData {
	private final Map<String, AbstractBeanFactory> factory;
	private final Map<String, BeanUI> beanUI;
	private final Map<String, AbstractItemPopupMenu> popupMenus;
	private final Map<String, PerspectiveData> subperspectiveMap;
	private final Validator validator;
	private final Set<String> undeletable;
	private final Set<String> cuttable;
	
	
	public PerspectiveData(final Map<String, AbstractBeanFactory> factory,
		final Map<String, BeanUI> beanUI,
		final Map<String, AbstractItemPopupMenu> popupMenus,
		Map<String, PerspectiveData> subperspectiveMap,
		final Validator validator,
		final Set<String> undeletable,
		final Set<String> cuttable) {
		this.factory = factory;
		this.beanUI = beanUI;
		this.popupMenus = popupMenus;
		this.subperspectiveMap = subperspectiveMap;
		this.validator = validator;
		this.undeletable = undeletable;
		this.cuttable = cuttable;
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

	public Validator getValidator() {
		return this.validator;
	}
	
	public AbstractItemPopupMenu getPopupMenu(final String codename) {
		return this.popupMenus.get(codename);
	}
	
	public boolean isUndeletable(final String codename) {
		return this.undeletable.contains(codename);
	}

	public boolean isCuttable(final String codename) {
		return this.cuttable.contains(codename);
	}

	public PerspectiveData getSubperspectiveData(final String codename) {		
		final PerspectiveData perspectiveData = this.subperspectiveMap.get(codename);
		return perspectiveData;
	}
}

