/*-
* $Id: AbstractBeanUI.java,v 1.5 2005/12/07 14:08:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;


/**
 * @version $Revision: 1.5 $, $Date: 2005/12/07 14:08:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBeanUI<T extends AbstractBean> implements BeanUI<T> {

	protected Icon icon;
	
	protected Icon image;
	
	private String iconResourceKey;
	private String imageResourceKey;

	protected final ManagerMainFrame managerMainFrame;
	
	protected  AbstractBeanUI(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	protected  AbstractBeanUI(final ManagerMainFrame managerMainFrame,
			final String resourceKeySuffix) {
		this(managerMainFrame);
		if (resourceKeySuffix != null) {
			this.iconResourceKey = "com.syrus.AMFICOM.manager.resources.icons." + resourceKeySuffix;
			this.imageResourceKey = "com.syrus.AMFICOM.manager.resources." + resourceKeySuffix;
		}
	}
	
	public JPanel getPropertyPanel(final T bean) {
		return null;
	}
	
	public void disposePropertyPanel() {
		// nothing		
	}

	
	public Icon getIcon(final AbstractBeanFactory<T> factory) {
		return this.iconResourceKey != null ? UIManager.getIcon(this.iconResourceKey) : null;
	}
	
	public Icon getImage(final T bean) {
		return this.imageResourceKey != null ? UIManager.getIcon(this.imageResourceKey) : null;
	}
	
}

