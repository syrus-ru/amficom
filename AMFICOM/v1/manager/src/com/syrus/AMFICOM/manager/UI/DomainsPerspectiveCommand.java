/*-
* $Id: DomainsPerspectiveCommand.java,v 1.8 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.8 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainsPerspectiveCommand extends AbstractCommand {
	
	final ManagerMainFrame graphText;
	
	public DomainsPerspectiveCommand(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {

		final Perspective perspective = 
			this.graphText.getManagerHandler().getPerspective("domains");
		
		this.graphText.setPerspective(perspective);	

	}
	
	
}

