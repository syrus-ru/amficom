/*-
* $Id: SoftSeverityMessagePerpective.java,v 1.1 2005/11/14 10:02:09 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/14 10:02:09 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class SoftSeverityMessagePerpective extends MessagePerpective {

	@Override
	protected Severity getSeverity() {
		return Severity.SEVERITY_SOFT;
	}	
}

