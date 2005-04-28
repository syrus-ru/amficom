/*-
 * $Id: ElementsToolBar.java,v 1.4 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class ElementsToolBar extends UgoToolBar {
	protected ApplicationContext aContext;
	protected AbstractPropertiesFrame additionalFrame;
	private static String[] buttons = new String[] { Constants.marqueeTool,
		Constants.separator, Constants.rectangleTool, Constants.ellipseTool,
		Constants.lineTool, Constants.textTool, Constants.separator,
		Constants.deviceTool, Constants.portOutKey, Constants.portInKey,
		Constants.linkTool, Constants.separator, Constants.blockPortKey,
		Constants.createTopLevelElementKey, Constants.groupKey,
		Constants.ungroupKey, Constants.separator, Constants.deleteKey,
		Constants.separator, Constants.zoomInKey, Constants.zoomOutKey,
		Constants.zoomActualKey
	};
	
//	public void createToolBar() {
//		commands.putAll(createGraphButtons());
//
//		for (int i = 0; i < buttons.length; i++) {
//			if (buttons[i].equals(Constants.separator))
//				insert(new JToolBar.Separator());
//			else
//				insert((JComponent)commands.get(buttons[i]));
//		}
//	}
	
	protected String[] getButtons() {
		return buttons;
	}

	protected ElementsToolBar(UgoTabbedPane sourcePane, AbstractPropertiesFrame additionalFrame,
			ApplicationContext aContext) {
		super(sourcePane);
		this.additionalFrame = additionalFrame;
		this.aContext = aContext;
	}

	protected Map createGraphButtons() {
		Map bttns = new HashMap();

		SchemeMarqueeHandler mh = pane.getMarqueeHandler();

		bttns.put(Constants.marqueeTool, createToolButton(mh.s, btn_size, null,
				null, Constants.ICON_MARQUEE, new MarqeeAction(pane), true));
		bttns.put("s_cell", mh.s_cell);
		bttns.put(Constants.deviceTool, createToolButton(mh.dev, btn_size, null,
				Constants.TEXT_DEVICE, Constants.ICON_DEVICE, null, true));
		bttns.put(Constants.rectangleTool, createToolButton(mh.r, btn_size, null,
				Constants.TEXT_RECTANGLE, Constants.ICON_RECTANGLE, null, true));
		bttns.put(Constants.ellipseTool, createToolButton(mh.c, btn_size, null,
				Constants.TEXT_ELLIPSE, Constants.ICON_ELLIPSE, null, true));
		bttns.put(Constants.textTool, createToolButton(mh.t, btn_size, null,
				Constants.TEXT_TEXT, Constants.ICON_TEXT, null, true));
		bttns.put(Constants.iconTool, createToolButton(mh.i, btn_size,
				Constants.TEXT_TEXT, Constants.TEXT_TEXT, null, null, true));
		bttns.put(Constants.lineTool, createToolButton(mh.l, btn_size, null,
				Constants.TEXT_LINE, Constants.ICON_LINE, null, true));
		bttns.put(Constants.cableTool, createToolButton(mh.ce, btn_size, null,
				Constants.TEXT_CABLE, Constants.ICON_CABLE, null, true));
		bttns.put(Constants.linkTool, createToolButton(mh.e, btn_size, null,
				Constants.TEXT_LINK, Constants.ICON_LINK, null, true));
		bttns.put(Constants.zoomTool, createToolButton(mh.z, btn_size,
				Constants.TEXT_ZOOM, Constants.TEXT_ZOOM, null, null, true));
		bttns.put(Constants.portOutKey, createToolButton(mh.p1, btn_size, null,
				Constants.TEXT_PORT, Constants.ICON_PORT, new PortToolAction(), false));
		bttns.put(Constants.portInKey, createToolButton(mh.p2, btn_size, null,
				Constants.TEXT_CABLE_PORT, Constants.ICON_CABLE_PORT,
				new PortToolAction(), false));
		bttns.put(Constants.groupKey, createToolButton(mh.gr, btn_size, null,
				Constants.TEXT_GROUP, Constants.ICON_GROUP, new CreateGroup(pane),
				false));
		bttns.put(Constants.ungroupKey, createToolButton(mh.ugr, btn_size, null,
				Constants.TEXT_UNGROUP, Constants.ICON_UNGROUP,
				new UngroupAction(pane), false));
		bttns.put(Constants.undoKey, createToolButton(mh.undo, btn_size, null,
				Constants.TEXT_UNDO, Constants.ICON_UNDO, new UndoAction(pane), false));
		bttns.put(Constants.redoKey, createToolButton(mh.redo, btn_size, null,
				Constants.TEXT_REDO, Constants.ICON_REDO, new RedoAction(pane), false));
		bttns.put(Constants.zoomInKey, createToolButton(mh.zi, btn_size, null,
				Constants.TEXT_ZOOM_IN, Constants.ICON_ZOOM_IN, new ZoomInAction(pane),
				true));
		bttns.put(Constants.zoomOutKey, createToolButton(mh.zo, btn_size, null,
				Constants.TEXT_ZOOM_OUT, Constants.ICON_ZOOM_OUT, new ZoomOutAction(
						pane), true));
		bttns.put(Constants.zoomActualKey, createToolButton(mh.za, btn_size, null,
				Constants.TEXT_ZOOM_NORMAL, Constants.ICON_ZOOM_NORMAL,
				new ZoomActualAction(pane), true));
		bttns.put(Constants.deleteKey, createToolButton(mh.del, btn_size, null,
				Constants.TEXT_DELETE, Constants.ICON_DELETE, new DeleteAction(pane,
						aContext), false));
		// bttns.put(Constants.hierarchyUpKey,
		// createToolButton(mh.hup, btn_size, null, "вверх",
		// new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hand.gif")),
		// new HierarchyUpAction (getGraph()), true));
		bttns.put(Constants.createTopLevelElementKey, createToolButton(mh.ugo,
				btn_size, null, Constants.TEXT_CREATE_UGO, Constants.ICON_CREATE_UGO,
				new CreateTopLevelSchemeAction(pane, additionalFrame, aContext), false));
		bttns.put(Constants.blockPortKey, createToolButton(mh.bp, btn_size, null,
				Constants.TEXT_HIERARCHY_PORT, Constants.ICON_HIERARCHY_PORT,
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
