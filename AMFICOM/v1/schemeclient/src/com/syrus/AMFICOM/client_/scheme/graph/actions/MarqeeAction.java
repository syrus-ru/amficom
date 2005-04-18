/*
 * $Id: MarqeeAction.java,v 1.2 2005/04/18 09:55:03 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 09:55:03 $
 * @module schemeclient_v1
 */

public class MarqeeAction extends AbstractAction {
	UgoTabbedPane pane;

	public MarqeeAction(UgoTabbedPane pane) {
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
	}
}
