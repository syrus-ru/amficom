/*-
 * $Id: AbstractCommand.java,v 1.3 2005/06/06 14:52:47 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
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
