/*-
* $Id: Perspective.java,v 1.4 2005/10/11 15:34:53 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JToolBar;


/**
 * @version $Revision: 1.4 $, $Date: 2005/10/11 15:34:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Perspective {

	boolean isValid();
	
	String getCodename();
	
	String getName();
	
	void perspectiveApplied();
	
	void addEntities(final JToolBar entityToolBar);
	
}

