/*
 * $Id: PortToolAction.java,v 1.4 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.Constants;

/**
 * used only for pressed port button identification
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class PortToolAction extends AbstractAction {
	private static final long serialVersionUID = 6240450396040267156L;

	public PortToolAction() {
		super(Constants.PORT);
	}

	public void actionPerformed(ActionEvent e) {
		// no real action
	}
}