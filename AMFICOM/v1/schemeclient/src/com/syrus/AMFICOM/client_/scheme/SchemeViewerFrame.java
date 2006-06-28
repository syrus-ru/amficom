/*-
 * $Id: SchemeViewerFrame.java,v 1.10 2006/04/19 12:46:41 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2006/04/19 12:46:41 $
 * @module schemeclient
 */

public class SchemeViewerFrame extends JInternalFrame {
	private static final long serialVersionUID = 7822597656196362953L;
	ApplicationContext aContext;
	UgoTabbedPane pane;

	public SchemeViewerFrame(ApplicationContext aContext, UgoTabbedPane pane) {
		this.pane = pane;

		setName(FRAME_EDITOR_MAIN);
		setTitle(I18N.getString(FRAME_EDITOR_MAIN));
		
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.pane, BorderLayout.CENTER);
		
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
