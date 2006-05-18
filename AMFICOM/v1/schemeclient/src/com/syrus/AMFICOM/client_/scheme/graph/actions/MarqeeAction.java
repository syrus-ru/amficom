/*
 * $Id: MarqeeAction.java,v 1.4 2005/08/08 11:58:07 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
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
