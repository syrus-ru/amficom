/*-
* $Id: HardSeverityMessagePerpective.java,v 1.2 2005/11/28 14:47:05 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class HardSeverityMessagePerpective extends MessagePerpective {
	
	@Override
	protected Severity getSeverity() {
		return Severity.SEVERITY_HARD;
	}	
}

