/*-
* $Id: Perspective.java,v 1.6 2005/11/08 09:08:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JToolBar;

import com.syrus.AMFICOM.general.ApplicationException;


/**
 * @version $Revision: 1.6 $, $Date: 2005/11/08 09:08:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Perspective {

	boolean isValid();
	
	String getCodename();
	
	String getName();
	
	void perspectiveApplied() throws ApplicationException;
	
	void addEntities(final JToolBar entityToolBar);
	
	void createNecessaryItems() throws ApplicationException;
	
	boolean isDeletable(final AbstractBean abstractBean);
}

