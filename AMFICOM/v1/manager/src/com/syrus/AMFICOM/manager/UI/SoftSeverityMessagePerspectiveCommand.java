/*-
* $Id: SoftSeverityMessagePerspectiveCommand.java,v 1.3 2005/11/28 14:47:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.manager.perspective.SoftSeverityMessagePerpective;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/28 14:47:04 $
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

