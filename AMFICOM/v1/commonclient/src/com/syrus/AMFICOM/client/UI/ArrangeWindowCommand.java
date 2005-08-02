/*
* $Id: ArrangeWindowCommand.java,v 1.2 2005/08/02 13:03:21 arseniy Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.client.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:21 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ArrangeWindowCommand extends AbstractCommand {

	private WindowArranger	arranger;

	public ArrangeWindowCommand(WindowArranger arranger) {
		this.arranger = arranger;
	}

	public void execute() {
		this.arranger.arrange();
	}
}
