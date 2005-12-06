/*-
* $Id: Validator.java,v 1.2 2005/12/06 15:14:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import com.syrus.AMFICOM.manager.beans.AbstractBean;


/**
 * @version $Revision: 1.2 $, $Date: 2005/12/06 15:14:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Validator {

	boolean isValid(final String source, final String target);
}

