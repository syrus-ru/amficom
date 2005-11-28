/*-
* $Id: Perspective.java,v 1.2 2005/11/28 14:47:05 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.UI.ActionMutableTreeNode;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Perspective {

	boolean isValid();
	
	String getCodename();
	
	String getName();
	
	void setManagerMainFrame(final ManagerMainFrame managerMainFrame);
	
	void perspectiveApplied() throws ApplicationException;
	
	void createNecessaryItems() throws ApplicationException;
	
	AbstractItemPopupMenu getPopupMenu(final String codename);
	
	AbstractBean createBean(final String codename) throws ApplicationException;
	
	BeanUI getBeanUI(final String codename) throws ApplicationException;
	
	Validator getValidator();
	
	boolean isSupported(final AbstractBean bean);
	
	void setPerspectiveData(final PerspectiveData perspectiveData);
	
	List<AbstractBean> getLayoutBeans();
	
	void setLayoutBeans(final List<AbstractBean> layoutBeans);
	
	void addLayoutBean(final AbstractBean bean);
	
	void removeLayoutBean(final AbstractBean bean);
	
	Perspective getPerspective(final String layoutName);
	
	Set<Perspective> getPerspectives();
	
	List<ActionMutableTreeNode> getActionNodes() throws ApplicationException;
	
	List<AbstractAction> getActions() throws ApplicationException;
	
	Perspective getSubPerspective(final AbstractBean bean);
	
	Perspective getSuperPerspective();
	
	LayoutItem getParentLayoutItem();
	
	void addPropertyChangeListener(final PropertyChangeListener listener);
	
	void removePropertyChangeListener(final PropertyChangeListener listener);
	
	void firePropertyChangeEvent(final PropertyChangeEvent event);
}

