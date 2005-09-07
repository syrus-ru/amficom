/*-
 * $Id: AbstractCommand.java,v 1.6 2005/09/07 02:37:31 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/07 02:37:31 $
 * @author $Author: arseniy $
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

	protected void setResult(final int result) {
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
