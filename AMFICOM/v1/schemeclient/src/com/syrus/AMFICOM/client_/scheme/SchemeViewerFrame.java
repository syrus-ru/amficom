/*-
 * $Id: SchemeViewerFrame.java,v 1.8 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeViewerFrame extends JInternalFrame {
	private static final long serialVersionUID = 7822597656196362953L;
	public static final String	NAME	= "editorFrame";
	ApplicationContext aContext;
	UgoTabbedPane pane;

	public SchemeViewerFrame(ApplicationContext aContext, UgoTabbedPane pane) {
		this.pane = pane;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

//	protected void closeFrame() {
//		if (isClosable()) {
//			aContext.getDispatcher().unregister(this, SchemeEvent.TYPE);
//			doDefaultCloseAction();
//		}
//	}

	private void jbInit() throws Exception {
		setName(NAME);
		
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.pane, BorderLayout.CENTER);
	}

	public SchemeGraph getGraph() {
		return this.pane.getGraph();
	}

	@Override
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
