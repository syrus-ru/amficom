/*-
 * $Id: LazyCommand.java,v 1.3 2005/09/08 14:25:57 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import javax.swing.UIDefaults;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:25:57 $
 * @author $Author: bob $
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

	@Override
	public void commitExecute() {
		this.getLazyCommand().commitExecute();
	}

	@Override
	public void commitUndo() {
		this.getLazyCommand().commitUndo();
	}

	@Override
	public void execute() {
		this.getLazyCommand().execute();
	}

	@Override
	public int getResult() {
		return this.getLazyCommand().getResult();
	}

	@Override
	public Object getSource() {
		return this.getLazyCommand().getSource();
	}

	@Override
	public void redo() {
		this.getLazyCommand().redo();
	}

	@Override
	public void setParameter(	final String field,
								final Object value) {
		this.getLazyCommand().setParameter(field, value);
	}

	@Override
	public void undo() {
		this.getLazyCommand().undo();

	}

}
