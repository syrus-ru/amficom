/*-
 * $Id: AbstractCommand.java,v 1.1 2005/05/19 14:06:41 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public abstract class AbstractCommand implements Command {

	public void commitExecute() {
		// nothing

	}

	public void commitUndo() {
		// nothing

	}

	public void execute() {
		// nothing

	}

	public int getResult() {
		return RESULT_OK;
	}

	public Object getSource() {
		return null;
	}

	public void redo() {
		// nothing

	}

	public void setParameter(	String field,
								Object value) {
		// nothing

	}

	public void undo() {
		// nothing

	}

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
