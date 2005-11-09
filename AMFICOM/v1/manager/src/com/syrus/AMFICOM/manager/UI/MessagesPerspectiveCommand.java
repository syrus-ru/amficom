/*-
* $Id: MessagesPerspectiveCommand.java,v 1.1 2005/11/09 15:08:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.manager.MessagesPerpective;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/09 15:08:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessagesPerspectiveCommand extends AbstractCommand {
	final ManagerMainFrame graphText;
	
	public MessagesPerspectiveCommand(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {
		this.graphText.setPerspective(new MessagesPerpective(this.graphText));

	}
	
	
}

