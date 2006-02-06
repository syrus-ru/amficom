/*-
 * $Id: AbstractCommand.java,v 1.7 2005/09/21 13:49:01 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/21 13:49:01 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public abstract class AbstractCommand implements Command {

	protected int result = RESULT_UNSPECIFIED;

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
		return this.result;
	}

	public void setResult(final int result) {
		this.result = result;
	}

	public Object getSource() {
		return null;
	}

	public void redo() {
		// nothing
	}

	public void setParameter(final String field, final Object value) {
		// nothing
	}

	public void undo() {
		// nothing
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
