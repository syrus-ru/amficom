/*
* $Id: ArrangeWindowCommand.java,v 1.3 2005/09/08 14:26:23 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.client.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;

/**
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:26:23 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ArrangeWindowCommand extends AbstractCommand {

	private WindowArranger	arranger;

	public ArrangeWindowCommand(WindowArranger arranger) {
		this.arranger = arranger;
	}

	@Override
	public void execute() {
		this.arranger.arrange();
	}
}
