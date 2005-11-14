/*-
* $Id: HardSeverityMessagePerspectiveCommand.java,v 1.1 2005/11/14 10:02:10 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.manager.HardSeverityMessagePerpective;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/14 10:02:10 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class HardSeverityMessagePerspectiveCommand extends AbstractCommand {
	final ManagerMainFrame graphText;
	
	public HardSeverityMessagePerspectiveCommand(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {
		assert Log.debugMessage(Log.DEBUGLEVEL03);
		this.graphText.setPerspective(new HardSeverityMessagePerpective());

	}
	
	
}

