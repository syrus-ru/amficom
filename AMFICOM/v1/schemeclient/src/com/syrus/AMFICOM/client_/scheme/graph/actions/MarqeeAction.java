/*
 * $Id: MarqeeAction.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class MarqeeAction extends AbstractAction {
	SchemeGraph graph;

	public MarqeeAction(SchemeGraph graph) {
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e) {
	}
}
