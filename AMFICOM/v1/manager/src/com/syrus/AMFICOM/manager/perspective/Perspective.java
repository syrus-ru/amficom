/*-
* $Id: Perspective.java,v 1.1 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import java.util.Set;

import javax.swing.JToolBar;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.viewers.BeanUI;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:35 $
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
	
	void addEntities(final JToolBar entityToolBar) throws ApplicationException;
	
	void createNecessaryItems() throws ApplicationException;
	
	boolean isDeletable(final AbstractBean abstractBean);
	
	AbstractItemPopupMenu getPopupMenu(final String codename);
	
	AbstractBean createBean(final String codename) throws ApplicationException;
	
	BeanUI getBeanUI(final String codename) throws ApplicationException;
	
	Validator getValidator();
	
	boolean isSupported(final AbstractBean bean);
	
	void setPerspectiveData(final PerspectiveData perspectiveData);
	
	Set<AbstractBean> getLayoutBeans();
	
	void setLayoutBeans(final Set<AbstractBean> layoutBeans);
	
	void addLayoutBean(final AbstractBean bean);
	
	void removeLayoutBean(final AbstractBean bean);
	
	Perspective getPerspective(final String layoutName);
	
	Set<Perspective> getPerspectives();
}

