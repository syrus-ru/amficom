/*-
 * $Id: SchemeViewerFrame.java,v 1.1 2005/04/05 14:10:46 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.*;
import java.beans.PropertyVetoException;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:10:46 $
 * @module schemeclient_v1
 */

public class SchemeViewerFrame extends JInternalFrame implements
		OperationListener {
	ApplicationContext aContext;
	UgoTabbedPane pane;

	public SchemeViewerFrame(ApplicationContext aContext, UgoTabbedPane pane) {
		this.pane = pane;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null)
			this.aContext.getDispatcher().unregister(this, SchemeEvent.TYPE);
		this.aContext = aContext;
		this.aContext.getDispatcher().register(this, SchemeEvent.TYPE);
	}

	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.CLOSE_SCHEME)) {
				closeFrame();
			}
		}
	}

	protected void closeFrame() {
		if (isClosable()) {
			aContext.getDispatcher().unregister(this, SchemeEvent.TYPE);
			doDefaultCloseAction();
		}
	}

	private void jbInit() throws Exception {
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pane, BorderLayout.CENTER);
	}

	public SchemeGraph getGraph() {
		return pane.getGraph();
	}

	public void doDefaultCloseAction() {
		if (isMaximum()) {
			try {
				setMaximum(false);
			} catch (PropertyVetoException ex) {
				// ignore
			}
		}
		super.doDefaultCloseAction();
	}
}
