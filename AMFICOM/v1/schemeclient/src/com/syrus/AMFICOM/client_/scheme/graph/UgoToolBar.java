/*-
 * $Id: UgoToolBar.java,v 1.2 2005/04/18 09:55:03 stas Exp $
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

import com.syrus.AMFICOM.client_.scheme.graph.actions.MarqeeAction;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 09:55:03 $
 * @module schemeclient_v1
 */

public class UgoToolBar extends JToolBar {
	public final Dimension btn_size = new Dimension(24, 24);
	protected UgoTabbedPane pane;
	protected int position = 0;
	protected Map commands = new HashMap();
	private static String[] buttons = new String[] { 
		Constants.marqueeTool 
	};

	protected UgoToolBar(UgoTabbedPane pane) {
		this.pane = pane;
	}
	
	public void createToolBar() {
		commands.putAll(createGraphButtons());

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].equals(Constants.separator))
				insert(new JToolBar.Separator());
			else
				insert((JComponent)commands.get(buttons[i]));
		}
	}

	protected Map createGraphButtons() {
		Map bttns = new HashMap();

		SchemeMarqueeHandler mh = pane.getMarqueeHandler();

		bttns.put(Constants.marqueeTool, createToolButton(mh.s, btn_size, null,
				null, Constants.ICON_MARQUEE, new MarqeeAction(pane), true));
		ButtonGroup group = new ButtonGroup();
		for (Iterator it = bttns.values().iterator(); it.hasNext();)
			group.add((AbstractButton) it.next());
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
		add(c);//, new XYConstraints(position, 0, -1, -1));
		position += c.getPreferredSize().width;
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
