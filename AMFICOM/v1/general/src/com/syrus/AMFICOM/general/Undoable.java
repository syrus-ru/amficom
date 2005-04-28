/*-
* $Id: Undoable.java,v 1.1 2005/04/28 16:02:19 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 16:02:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general_v1
 */
public interface Undoable {

	void undo();
	
	void redo();
}

