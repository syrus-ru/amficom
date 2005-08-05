/*
 * $Id: PortToolAction.java,v 1.3 2005/08/05 12:39:59 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
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