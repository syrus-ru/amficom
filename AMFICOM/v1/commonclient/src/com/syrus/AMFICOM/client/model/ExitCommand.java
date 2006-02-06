/*
 * $Id: ExitCommand.java,v 1.5 2005/11/24 08:04:15 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * 
 * @version $Revision: 1.5 $, $Date: 2005/11/24 08:04:15 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ExitCommand extends AbstractCommand {

	private AbstractMainFrame	window; // Окно, из которого вызвана команда
	private final Dispatcher	dispatcher;

	public ExitCommand(final AbstractMainFrame window,
			final Dispatcher dispatcher) {
		this.window = window;
		this.dispatcher = dispatcher;		
	}

	@Override
	public void execute() {
		final ContextChangeEvent logOutEvent = 
			new ContextChangeEvent(this, ContextChangeEvent.LOGGED_OUT_EVENT);
		this.dispatcher.firePropertyChange(logOutEvent);
		AbstractMainFrame.disposeMainFrame(this.window); // Реально удаление окна
		// производит
		// только класс окружения Environment
	}

	@Override
	public void setParameter(	String field,
								Object value) {
		if (field.equals("window")) {
			setWindow((AbstractMainFrame) value);
		}
	}

	public void setWindow(AbstractMainFrame window) {
		this.window = window;
	}

}
