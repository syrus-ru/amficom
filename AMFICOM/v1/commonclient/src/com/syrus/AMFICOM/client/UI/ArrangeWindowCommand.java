/*
* $Id: ArrangeWindowCommand.java,v 1.1 2005/05/19 14:06:41 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.client.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;

/**
 * 
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
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
