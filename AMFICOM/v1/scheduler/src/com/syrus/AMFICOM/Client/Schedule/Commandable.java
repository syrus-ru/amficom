/*-
* $Id: Commandable.java,v 1.1 2005/04/12 06:52:36 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.General.Command.Command;


/**
 * @version $Revision: 1.1 $, $Date: 2005/04/12 06:52:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface Commandable {

	Command getCommand();
}

