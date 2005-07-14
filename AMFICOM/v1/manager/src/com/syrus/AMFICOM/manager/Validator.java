/*-
* $Id: Validator.java,v 1.1 2005/07/14 10:14:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 10:14:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Validator {

	boolean isValid(AbstractBean sourceBean, AbstractBean targetBean);
}

