/*
 * $Id: PortToolAction.java,v 1.2 2005/05/26 07:40:51 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class PortToolAction extends AbstractAction {
	public PortToolAction() {
		super(Constants.PORT);
	}

	public void actionPerformed(ActionEvent e) {
		// no real action
	}
}