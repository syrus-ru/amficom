/*
 * $Id: ExitCommand.java,v 1.2 2005/08/02 13:03:22 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Window;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ExitCommand extends AbstractCommand {

	private Window	window; // Окно, из которого вызвана команда

	public ExitCommand(Window window) {
		this.window = window;
	}

	public void execute() {
		System.out.println("exit window " + this.window.getName());
		Environment.disposeWindow(this.window); // Реально удаление окна
		// производит
		// только класс окружения Environment
	}

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
