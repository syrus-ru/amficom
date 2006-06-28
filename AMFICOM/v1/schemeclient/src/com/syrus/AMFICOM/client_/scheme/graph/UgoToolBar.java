/*-
 * $Id: UgoToolBar.java,v 1.12 2006/05/03 04:50:03 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Component;
import java.awt.Dimension;
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

import com.syrus.AMFICOM.client_.scheme.graph.actions.MarqeeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomActualAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomInAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomOutAction;

/**
 * @author $Author: stas $
 * @version $Revision: 1.12 $, $Date: 2006/05/03 04:50:03 $
 * @module schemeclient
 */

public class UgoToolBar extends JToolBar {
	private static final long serialVersionUID = 5139643435052148700L;

	public final Dimension btn_size = new Dimension(24, 24);
	protected UgoTabbedPane pane;
	protected int position = 0;
	protected Map<String, AbstractButton> commands = new HashMap<String, AbstractButton>();
//	private static LayoutManager vertical = new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, false, false);
//	private static LayoutManager horizontal = new FlowLayout(FlowLayout.LEFT, 0, 0);
	private static String[] buttons = new String[] { Constants.MARQUEE,
			Constants.SEPARATOR, Constants.RECTANGLE, Constants.ELLIPSE,
			Constants.LINE, Constants.TEXT, Constants.HORIZONTAL_GLUE };

	protected UgoToolBar(UgoTabbedPane pane) {
		this.pane = pane;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		createToolBar();
		setFloatable(false);
	}
	/*
	public void setOrientation(int o) {
		super.setOrientation(o);
		if (o == VERTICAL) {
			setLayout(vertical);
		} 
		else if (o == HORIZONTAL) {
			setLayout(horizontal);
		}
	}*/
	
	protected void createToolBar() {
		this.commands.putAll(createGraphButtons());

		String[] butt = getButtons();
		
		for (int i = 0; i < butt.length; i++) {
			if (butt[i].equals(Constants.SEPARATOR))
				insert(new JToolBar.Separator());
			else if (butt[i].equals(Constants.HORIZONTAL_GLUE))
				insert(Box.createHorizontalGlue());
			else
				insert(this.commands.get(butt[i]));
		}
	}
	
	protected String[] getButtons() {
		return buttons;
	}

	protected Map<String, AbstractButton> createGraphButtons() {
		Map<String, AbstractButton> bttns = new HashMap<String, AbstractButton>();
		SchemeMarqueeHandler mh = this.pane.getMarqueeHandler();
		bttns.put(Constants.MARQUEE, createToolButton(mh.s, this.btn_size, null,
				null, Constants.ICON_MARQUEE, new MarqeeAction(this.pane), true));
		bttns.put(Constants.RECTANGLE, createToolButton(mh.r, this.btn_size, null,
				LangModelGraph.getString(Constants.RECTANGLE), Constants.ICON_RECTANGLE, null, true));
		bttns.put(Constants.ELLIPSE, createToolButton(mh.c, this.btn_size, null,
				LangModelGraph.getString(Constants.ELLIPSE), Constants.ICON_ELLIPSE, null, true));
		bttns.put(Constants.TEXT, createToolButton(mh.t, this.btn_size, null,
				LangModelGraph.getString(Constants.ICON), Constants.ICON_TEXT, null, true));
		bttns.put(Constants.ICON, createToolButton(mh.i, this.btn_size, null,
				LangModelGraph.getString(Constants.ICON), Constants.ICON_TEXT, null, true));
		bttns.put(Constants.LINE, createToolButton(mh.l, this.btn_size, null,
				LangModelGraph.getString(Constants.LINE), Constants.ICON_LINE, null, true));
		bttns.put(Constants.ZOOM_IN, createToolButton(mh.zi, this.btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_IN), Constants.ICON_ZOOM_IN, new ZoomInAction(this.pane),
				true));
		bttns.put(Constants.ZOOM_OUT, createToolButton(mh.zo, this.btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_OUT), Constants.ICON_ZOOM_OUT, new ZoomOutAction(
						this.pane), true));
		bttns.put(Constants.ZOOM_ACTUAL, createToolButton(mh.za, this.btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_ACTUAL), Constants.ICON_ZOOM_NORMAL,
				new ZoomActualAction(this.pane), true));
		bttns.put(Constants.ZOOM_BOX, createToolButton(mh.zb, this.btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_BOX), Constants.ICON_ZOOM_BOX, null,
				true));
		
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
			JComponent source = UgoToolBar.this.pane.getGraph();
			if (source != null)
				e = new ActionEvent(source, e.getID(), e.getActionCommand(), e.getModifiers());
			this.action.actionPerformed(e);
		}
	}
}
