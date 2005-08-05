/*
 * $Id: MarqeeAction.java,v 1.3 2005/08/05 12:39:59 stas Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class MarqeeAction extends AbstractAction {
	private static final long serialVersionUID = 5778054304518007916L;
	UgoTabbedPane pane;

	public MarqeeAction(UgoTabbedPane pane) {
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
	}
}
