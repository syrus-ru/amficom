/*-
 * $Id: LazyCommand.java,v 1.2 2005/08/02 13:03:22 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import javax.swing.UIDefaults;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class LazyCommand extends AbstractCommand {

	private UIDefaults	defaults;
	private Object		commandKey;

	public LazyCommand(UIDefaults defaults, Object commandKey) {
		this.defaults = defaults;
		this.commandKey = commandKey;
	}

	private Command getLazyCommand() {
		return (Command) this.defaults.get(this.commandKey);
	}

	public void commitExecute() {
		this.getLazyCommand().commitExecute();
	}

	public void commitUndo() {
		this.getLazyCommand().commitUndo();
	}

	public void execute() {
		this.getLazyCommand().execute();
	}

	public int getResult() {
		return this.getLazyCommand().getResult();
	}

	public Object getSource() {
		return this.getLazyCommand().getSource();
	}

	public void redo() {
		this.getLazyCommand().redo();
	}

	public void setParameter(	String field,
								Object value) {
		this.getLazyCommand().setParameter(field, value);
	}

	public void undo() {
		this.getLazyCommand().undo();

	}

}
