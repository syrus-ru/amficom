/*-
 * $Id: AbstractCommand.java,v 1.4 2005/08/02 13:03:22 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 13:03:22 $
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

	protected void setResult(int result) {
		this.result = result;
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
