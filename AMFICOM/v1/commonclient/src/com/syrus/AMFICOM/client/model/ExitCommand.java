/*
 * $Id: ExitCommand.java,v 1.4 2005/10/25 15:33:34 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Window;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * 
 * @version $Revision: 1.4 $, $Date: 2005/10/25 15:33:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ExitCommand extends AbstractCommand {

	private Window	window; // Окно, из которого вызвана команда
	private final Dispatcher	dispatcher;

	public ExitCommand(final Window window,
			final Dispatcher dispatcher) {
		this.window = window;
		this.dispatcher = dispatcher;		
	}

	@Override
	public void execute() {
		final ContextChangeEvent logOutEvent = 
			new ContextChangeEvent(this, ContextChangeEvent.LOGGED_OUT_EVENT);
		this.dispatcher.firePropertyChange(logOutEvent);
		System.out.println("exit window " + this.window.getName());
		Environment.disposeWindow(this.window); // Реально удаление окна
		// производит
		// только класс окружения Environment
	}

	@Override
	public void setParameter(	String field,
								Object value) {
		if (field.equals("window")) {
			setWindow((Window) value);
		}
	}

	public void setWindow(Window window) {
		this.window = window;
	}

}
