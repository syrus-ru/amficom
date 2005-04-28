/*-
 * $Id: UgoToolBar.java,v 1.3 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import com.syrus.AMFICOM.client_.scheme.graph.actions.MarqeeAction;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class UgoToolBar extends JToolBar {
	public final Dimension btn_size = new Dimension(24, 24);
	protected UgoTabbedPane pane;
	protected int position = 0;
	protected Map commands = new HashMap();
	private static LayoutManager vertical = new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, false, false);
	private static LayoutManager horizontal = new FlowLayout(FlowLayout.LEFT, 0, 0);
	private static String[] buttons = new String[] { 
		Constants.marqueeTool 
	};

	protected UgoToolBar(UgoTabbedPane pane) {
		this.pane = pane;
	}
	
	public void setOrientation(int o) {
		super.setOrientation(o);
		if (o == VERTICAL) {
			setLayout(vertical);
		} 
		else if (o == HORIZONTAL) {
			setLayout(horizontal);
		}
	}
	
	public void createToolBar() {
		commands.putAll(createGraphButtons());

		String[] butt = getButtons();
		
		for (int i = 0; i < butt.length; i++) {
			if (butt[i].equals(Constants.separator))
				insert(new JToolBar.Separator());
			else
				insert((JComponent)commands.get(butt[i]));
		}
	}
	
	protected String[] getButtons() {
		return buttons;
	}

	protected Map createGraphButtons() {
		Map bttns = new HashMap();
		SchemeMarqueeHandler mh = pane.getMarqueeHandler();
		bttns.put(Constants.marqueeTool, createToolButton(mh.s, btn_size, null,
				null, Constants.ICON_MARQUEE, new MarqeeAction(pane), true));
		mh.s.doClick();
		return bttns;
	}

	protected AbstractButton createToolButton(AbstractButton b, Dimension preferred_size,
			String text, String tooltip, Icon icon, Action action, boolean isEnabled) {
		if (preferred_size != null)
			b.setPreferredSize(preferred_size);
		if (text != null)
			b.setText(text);
		if (tooltip != null)
			b.setToolTipText(tooltip);
		if (icon != null)
			b.setIcon(icon);
		if (action != null) {
			b.addActionListener(new EventRedirector(action));
			b.setActionCommand(Action.NAME);
		}
		b.setEnabled(isEnabled);
		b.setFocusable(false);
		return b;
	}

	protected void insert(Component c) {
		add(c);
	}
	
	/* This will change the source of the actionevent to graph. */
	protected class EventRedirector implements ActionListener {
		protected Action action;

		public EventRedirector(Action a) {
			this.action = a;
		}

		public void actionPerformed(ActionEvent e) {
			JComponent source = pane.getGraph();
			if (source != null)
				e = new ActionEvent(source, e.getID(), e.getActionCommand(), e.getModifiers());
			action.actionPerformed(e);
		}
	}
}
