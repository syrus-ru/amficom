/*-
* $Id: Perspective.java,v 1.3 2005/09/12 11:10:16 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/12 11:10:16 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Perspective {

	boolean isValid();
	
	String getPerspectiveName();
	
	void perspectiveApplied();
	
}

