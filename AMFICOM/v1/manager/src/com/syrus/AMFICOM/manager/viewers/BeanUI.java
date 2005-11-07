/*-
* $Id: BeanUI.java,v 1.2 2005/11/07 15:24:19 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.AbstractBeanFactory;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface BeanUI<T extends AbstractBean> {

	JPanel getPropertyPanel(final T bean);
	
	JPopupMenu getPopupMenu(final T bean, 
	                        final Object cell);
	
	Icon getImage(final T bean);
	
	Icon  getIcon(final AbstractBeanFactory<T> factory);
	
	void disposePropertyPanel();

}

