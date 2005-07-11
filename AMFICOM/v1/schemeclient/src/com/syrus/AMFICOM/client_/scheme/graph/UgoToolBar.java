/*-
 * $Id: UgoToolBar.java,v 1.6 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import com.syrus.AMFICOM.client_.scheme.graph.actions.MarqeeAction;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class UgoToolBar extends JToolBar {
	public final Dimension btn_size = new Dimension(24, 24);
	protected UgoTabbedPane pane;
	protected int position = 0;
	protected Map commands = new HashMap();
	private static LayoutManager vertical = new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, false, false);
	private static LayoutManager horizontal = new FlowLayout(FlowLayout.LEFT, 0, 0);
	private static String[] buttons = new String[] { Constants.MARQUEE,
			Constants.SEPARATOR, Constants.RECTANGLE, Constants.ELLIPSE,
			Constants.LINE, Constants.TEXT, Constants.HORIZONTAL_GLUE };

	protected UgoToolBar(UgoTabbedPane pane) {
		this.pane = pane;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		createToolBar();
		setFloatable(false);
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
	
	protected void createToolBar() {
		commands.putAll(createGraphButtons());

		String[] butt = getButtons();
		
		for (int i = 0; i < butt.length; i++) {
			if (butt[i].equals(Constants.SEPARATOR))
				insert(new JToolBar.Separator());
			else if (butt[i].equals(Constants.HORIZONTAL_GLUE))
				insert(Box.createHorizontalGlue());
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
		bttns.put(Constants.MARQUEE, createToolButton(mh.s, btn_size, null,
				null, Constants.ICON_MARQUEE, new MarqeeAction(pane), true));
		bttns.put(Constants.RECTANGLE, createToolButton(mh.r, btn_size, null,
				LangModelGraph.getString(Constants.RECTANGLE), Constants.ICON_RECTANGLE, null, true));
		bttns.put(Constants.ELLIPSE, createToolButton(mh.c, btn_size, null,
				LangModelGraph.getString(Constants.ELLIPSE), Constants.ICON_ELLIPSE, null, true));
		bttns.put(Constants.TEXT, createToolButton(mh.t, btn_size, null,
				LangModelGraph.getString(Constants.ICON), Constants.ICON_TEXT, null, true));
		bttns.put(Constants.ICON, createToolButton(mh.i, btn_size, null,
				LangModelGraph.getString(Constants.ICON), Constants.ICON_TEXT, null, true));
		bttns.put(Constants.LINE, createToolButton(mh.l, btn_size, null,
				LangModelGraph.getString(Constants.LINE), Constants.ICON_LINE, null, true));
		
		ButtonGroup group = new ButtonGroup();
		for (Iterator it = bttns.values().iterator(); it.hasNext();) {
			AbstractButton button = (AbstractButton) it.next();
			group.add(button);
		}
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
