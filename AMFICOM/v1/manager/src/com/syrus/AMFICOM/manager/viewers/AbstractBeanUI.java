/*-
* $Id: AbstractBeanUI.java,v 1.2 2005/11/10 13:59:01 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/10 13:59:01 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBeanUI<T extends AbstractBean> implements BeanUI<T> {

	protected Icon icon;
	
	protected Icon image;	

	protected final ManagerMainFrame managerMainFrame;
	
	protected  AbstractBeanUI(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	protected  AbstractBeanUI(final ManagerMainFrame managerMainFrame,
			final String iconUrl,
			final String imageUrl) {
		this(managerMainFrame);
		URL resource = AbstractBeanFactory.class.getClassLoader().getResource(iconUrl);
		if (resource != null) {
			this.icon = new ImageIcon(resource);
		} else {
			assert Log.debugMessage(iconUrl + " not found ",
				Log.DEBUGLEVEL09);
		}
		
		resource = AbstractBeanFactory.class.getClassLoader().getResource(imageUrl);
		if (resource != null) {
			this.image = new ImageIcon(resource);
		} else {
			assert Log.debugMessage(imageUrl + " not found ",
				Log.DEBUGLEVEL09);
		}
	}
	
	
	public JPopupMenu getPopupMenu(	final T bean,
	                               	final Object cell) {
		return null;
	}
	
	public JPanel getPropertyPanel(final T bean) {
		return null;
	}
	
	public void disposePropertyPanel() {
		// nothing		
	}

	
	public Icon getIcon(final AbstractBeanFactory<T> factory) {
		return this.icon;
	}
	
	public Icon getImage(final T bean) {
		return this.image;
	}
	
}

