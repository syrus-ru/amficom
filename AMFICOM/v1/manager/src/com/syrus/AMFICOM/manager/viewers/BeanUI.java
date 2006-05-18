/*-
* $Id: BeanUI.java,v 1.4 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface BeanUI<T extends AbstractBean> {

	JPanel getPropertyPanel(final T bean);
	
//	JPopupMenu getPopupMenu(final T bean, 
//	                        final Object cell);
	
	Icon getImage(final T bean);
	
	Icon  getIcon(final AbstractBeanFactory<T> factory);
	
	void disposePropertyPanel();

}

