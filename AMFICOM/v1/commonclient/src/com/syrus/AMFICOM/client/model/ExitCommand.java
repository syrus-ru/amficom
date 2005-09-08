/*
 * $Id: ExitCommand.java,v 1.3 2005/09/08 14:25:57 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Window;

/**
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:25:57 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ExitCommand extends AbstractCommand {

	private Window	window; // Окно, из которого вызвана команда

	public ExitCommand(Window window) {
		this.window = window;
	}

	@Override
	public void execute() {
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
