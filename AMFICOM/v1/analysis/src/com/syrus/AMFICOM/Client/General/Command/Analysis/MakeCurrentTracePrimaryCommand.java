/*-
 * $Id: MakeCurrentTracePrimaryCommand.java,v 1.1 2005/07/21 10:55:53 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.AbstractCommand;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/07/21 10:55:53 $
 * @module
 */
public class MakeCurrentTracePrimaryCommand extends AbstractCommand {
	public MakeCurrentTracePrimaryCommand() {
		// empty
	}

	@Override
	public void execute() {
		String currentTrace = Heap.getCurrentTrace();
		if (! Heap.isTraceSecondary(currentTrace))
			return; // это не первичная рефлектограммма
		Heap.setSecondaryTraceAsPrimary(currentTrace);
	}
}
