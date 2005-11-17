/*-
* $Id: SoftSeverityMessagePerspectiveCommand.java,v 1.2 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.manager.perspective.SoftSeverityMessagePerpective;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class SoftSeverityMessagePerspectiveCommand extends AbstractCommand {
	final ManagerMainFrame graphText;
	
	public SoftSeverityMessagePerspectiveCommand(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {
		this.graphText.setPerspective(new SoftSeverityMessagePerpective());

	}
	
	
}

