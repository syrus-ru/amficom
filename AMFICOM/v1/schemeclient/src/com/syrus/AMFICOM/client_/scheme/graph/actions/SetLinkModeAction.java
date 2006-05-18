/*
 * $Id: SetLinkModeAction.java,v 1.8 2005/10/17 14:59:15 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/10/17 14:59:15 $
 * @module schemeclient
 */

public class SetLinkModeAction extends AbstractAction {
	private static final long serialVersionUID = 728678567741292211L;

	SchemeTabbedPane pane;

	public SetLinkModeAction(SchemeTabbedPane pane) {
		super(Constants.LINK_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
//		if (this.pane.getCurrentPanel() != null) { 
//			SchemeResource res = this.pane.getCurrentPanel().getSchemeResource();
//		}
		SchemeResource.setSchemePath(null, false);
//		SchemeResource.setCashedPathMemberIds(null);
//		SchemeResource.setCashedPathStart(null);
//		SchemeResource.setCashedPathEnd(null);
		SchemeGraph.setMode(Constants.LINK_MODE);
	}
}