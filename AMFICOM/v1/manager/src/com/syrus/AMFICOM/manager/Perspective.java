/*-
* $Id: Perspective.java,v 1.8 2005/11/10 13:59:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JToolBar;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.viewers.BeanUI;


/**
 * @version $Revision: 1.8 $, $Date: 2005/11/10 13:59:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Perspective {

	boolean isValid();
	
	String getCodename();
	
	String getName();
	
	void perspectiveApplied() throws ApplicationException;
	
	void addEntities(final JToolBar entityToolBar) throws ApplicationException;
	
	void createNecessaryItems() throws ApplicationException;
	
	boolean isDeletable(final AbstractBean abstractBean);
	
	AbstractBean createBean(final String codename) throws ApplicationException;
	
	BeanUI getBeanUI(final String codename) throws ApplicationException;
	
	Validator getValidator();
}

