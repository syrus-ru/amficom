/*-
 * $Id: ElementsToolBar.java,v 1.6 2005/06/22 10:16:05 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/06/22 10:16:05 $
 * @module schemeclient_v1
 */

public class ElementsToolBar extends UgoToolBar {
	protected ApplicationContext aContext;
	private static String[] buttons = new String[] { Constants.MARQUEE,
		Constants.SEPARATOR, Constants.RECTANGLE, Constants.ELLIPSE,
		Constants.LINE, Constants.TEXT, Constants.SEPARATOR,
		Constants.DEVICE, Constants.PORT, Constants.CABLE_PORT,
		Constants.LINK, Constants.SEPARATOR, Constants.BLOCK_PORT,
		Constants.CREATE_UGO, Constants.GROUP,
		Constants.UNGROUP, Constants.SEPARATOR, Constants.DELETE,
		Constants.SEPARATOR, Constants.ZOOM_IN, Constants.ZOOM_OUT,
		Constants.ZOOM_ACTUAL
	};
	
	protected String[] getButtons() {
		return buttons;
	}

	protected ElementsToolBar(UgoTabbedPane sourcePane, ApplicationContext aContext) {
		super(sourcePane);
		this.aContext = aContext;
	}

	protected Map createGraphButtons() {
		Map bttns = new HashMap();

		SchemeMarqueeHandler mh = pane.getMarqueeHandler();

		bttns.put(Constants.MARQUEE, createToolButton(mh.s, btn_size, null,
				null, Constants.ICON_MARQUEE, new MarqeeAction(pane), true));
		bttns.put("s_cell", mh.s_cell);
		bttns.put(Constants.DEVICE, createToolButton(mh.dev, btn_size, null,
				LangModelGraph.getString(Constants.DEVICE), Constants.ICON_DEVICE, null, true));
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
		bttns.put(Constants.CABLE, createToolButton(mh.ce, btn_size, null,
				LangModelGraph.getString(Constants.CABLE), Constants.ICON_CABLE, null, true));
		bttns.put(Constants.LINK, createToolButton(mh.e, btn_size, null,
				LangModelGraph.getString(Constants.LINK), Constants.ICON_LINK, null, true));
//		bttns.put(Constants.ZOOM, createToolButton(mh.z, btn_size, null,
//				LangModelGraph.getString(Constants.ZOOM), Constants.ICON_ZOOM_NORMAL, null, null, true));
		bttns.put(Constants.PORT, createToolButton(mh.p1, btn_size, null,
				LangModelGraph.getString(Constants.PORT), Constants.ICON_PORT, new PortToolAction(), false));
		bttns.put(Constants.CABLE_PORT, createToolButton(mh.p2, btn_size, null,
				LangModelGraph.getString(Constants.CABLE_PORT), Constants.ICON_CABLE_PORT,
				new PortToolAction(), false));
		bttns.put(Constants.GROUP, createToolButton(mh.gr, btn_size, null,
				LangModelGraph.getString(Constants.GROUP), Constants.ICON_GROUP, new CreateGroup(pane),
				false));
		bttns.put(Constants.UNGROUP, createToolButton(mh.ugr, btn_size, null,
				LangModelGraph.getString(Constants.UNGROUP), Constants.ICON_UNGROUP,
				new UngroupAction(pane), false));
		bttns.put(Constants.UNDO, createToolButton(mh.undo, btn_size, null,
				LangModelGraph.getString(Constants.UNDO), Constants.ICON_UNDO, new UndoAction(pane), false));
		bttns.put(Constants.REDO, createToolButton(mh.redo, btn_size, null,
				LangModelGraph.getString(Constants.REDO), Constants.ICON_REDO, new RedoAction(pane), false));
		bttns.put(Constants.ZOOM_IN, createToolButton(mh.zi, btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_IN), Constants.ICON_ZOOM_IN, new ZoomInAction(pane),
				true));
		bttns.put(Constants.ZOOM_OUT, createToolButton(mh.zo, btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_OUT), Constants.ICON_ZOOM_OUT, new ZoomOutAction(
						pane), true));
		bttns.put(Constants.ZOOM_ACTUAL, createToolButton(mh.za, btn_size, null,
				LangModelGraph.getString(Constants.ZOOM_ACTUAL), Constants.ICON_ZOOM_NORMAL,
				new ZoomActualAction(pane), true));
		bttns.put(Constants.DELETE, createToolButton(mh.del, btn_size, null,
				LangModelGraph.getString(Constants.DELETE), Constants.ICON_DELETE, new DeleteAction(pane,
						aContext), false));
		// bttns.put(Constants.hierarchyUpKey,
		// createToolButton(mh.hup, btn_size, null, "вверх",
		// new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hand.gif")),
		// new HierarchyUpAction (getGraph()), true));
		bttns.put(Constants.CREATE_UGO, createToolButton(mh.ugo,
				btn_size, null, LangModelGraph.getString(Constants.CREATE_UGO), Constants.ICON_CREATE_UGO,
				new CreateTopLevelSchemeAction(pane, aContext), false));
		bttns.put(Constants.BLOCK_PORT, createToolButton(mh.bp, btn_size, null,
				LangModelGraph.getString(Constants.BLOCK_PORT), Constants.ICON_HIERARCHY_PORT,
				new CreateBlockPortAction(pane), false));

		ButtonGroup group = new ButtonGroup();
		for (Iterator it = bttns.values().iterator(); it.hasNext();) {
			AbstractButton button = (AbstractButton) it.next();
			group.add(button);
		}
		mh.s.doClick();

		return bttns;
	}
}
