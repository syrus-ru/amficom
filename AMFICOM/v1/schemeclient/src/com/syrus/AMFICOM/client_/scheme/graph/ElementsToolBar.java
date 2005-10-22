/*-
 * $Id: ElementsToolBar.java,v 1.14 2005/10/22 10:04:18 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateBlockPortAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateGroup;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateTopLevelSchemeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.DeleteAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.PortToolAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.RedoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.UndoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.UngroupAction;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;

/**
 * @author $Author: stas $
 * @version $Revision: 1.14 $, $Date: 2005/10/22 10:04:18 $
 * @module schemeclient
 */

public class ElementsToolBar extends UgoToolBar {
	private static final long serialVersionUID = -8239098824427482935L;

	protected ApplicationContext aContext;
	private static String[] buttons = new String[] { Constants.MARQUEE,
		Constants.SEPARATOR, Constants.RECTANGLE, Constants.ELLIPSE,
		Constants.LINE, Constants.TEXT, Constants.SEPARATOR,
		Constants.DEVICE, Constants.PORT, Constants.CABLE_PORT,
		Constants.LINK, Constants.SEPARATOR, Constants.BLOCK_PORT,
		Constants.CREATE_UGO, Constants.GROUP,
		Constants.UNGROUP, Constants.SEPARATOR, Constants.DELETE,
		Constants.SEPARATOR, Constants.ZOOM_IN, Constants.ZOOM_OUT, Constants.ZOOM_BOX, 
		Constants.ZOOM_ACTUAL, Constants.HORIZONTAL_GLUE
	};
	
	@Override
	protected String[] getButtons() {
		return buttons;
	}

	protected ElementsToolBar(UgoTabbedPane sourcePane, ApplicationContext aContext) {
		super(sourcePane);
		this.aContext = aContext;
	}

	@Override
	protected Map<String, AbstractButton> createGraphButtons() {
		Map<String, AbstractButton> bttns = super.createGraphButtons();

		SchemeMarqueeHandler mh = this.pane.getMarqueeHandler();

		bttns.put(Constants.DEVICE, createToolButton(mh.dev, this.btn_size, null,
				LangModelGraph.getString(Constants.DEVICE), Constants.ICON_DEVICE, null, true));
		bttns.put(Constants.CABLE, createToolButton(mh.ce, this.btn_size, null,
				LangModelGraph.getString(Constants.CABLE), Constants.ICON_CABLE, null, true));
		bttns.put(Constants.LINK, createToolButton(mh.e, this.btn_size, null,
				LangModelGraph.getString(Constants.LINK), Constants.ICON_LINK, null, true));
//		bttns.put(Constants.ZOOM, createToolButton(mh.z, btn_size, null,
//				LangModelGraph.getString(Constants.ZOOM), Constants.ICON_ZOOM_NORMAL, null, null, true));
		bttns.put(Constants.PORT, createToolButton(mh.p1, this.btn_size, null,
				LangModelGraph.getString(Constants.PORT), Constants.ICON_PORT, new PortToolAction(), false));
		bttns.put(Constants.CABLE_PORT, createToolButton(mh.p2, this.btn_size, null,
				LangModelGraph.getString(Constants.CABLE_PORT), Constants.ICON_CABLE_PORT,
				new PortToolAction(), false));
		bttns.put(Constants.GROUP, createToolButton(mh.gr, this.btn_size, null,
				LangModelGraph.getString(Constants.GROUP), Constants.ICON_GROUP, new CreateGroup(this.pane, DeviceGroup.PROTO_ELEMENT),
				false));
		bttns.put(Constants.UNGROUP, createToolButton(mh.ugr, this.btn_size, null,
				LangModelGraph.getString(Constants.UNGROUP), Constants.ICON_UNGROUP,
				new UngroupAction(this.pane), false));
		bttns.put(Constants.UNDO, createToolButton(mh.undo, this.btn_size, null,
				LangModelGraph.getString(Constants.UNDO), Constants.ICON_UNDO, new UndoAction(this.pane), false));
		bttns.put(Constants.REDO, createToolButton(mh.redo, this.btn_size, null,
				LangModelGraph.getString(Constants.REDO), Constants.ICON_REDO, new RedoAction(this.pane), false));
		bttns.put(Constants.DELETE, createToolButton(mh.del, this.btn_size, null,
				LangModelGraph.getString(Constants.DELETE), Constants.ICON_DELETE, new DeleteAction((ElementsTabbedPane)this.pane), false));
		// bttns.put(Constants.hierarchyUpKey,
		// createToolButton(mh.hup, btn_size, null, "вверх",
		// new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hand.gif")),
		// new HierarchyUpAction (getGraph()), true));
		bttns.put(Constants.CREATE_UGO, createToolButton(mh.ugo,
				this.btn_size, null, LangModelGraph.getString(Constants.CREATE_UGO), Constants.ICON_CREATE_UGO,
				new CreateTopLevelSchemeAction(this.pane), false));
		bttns.put(Constants.BLOCK_PORT, createToolButton(mh.bp, this.btn_size, null,
				LangModelGraph.getString(Constants.BLOCK_PORT), Constants.ICON_HIERARCHY_PORT,
				new CreateBlockPortAction(this.pane), false));

		ButtonGroup group = new ButtonGroup();
		for (Iterator it = bttns.values().iterator(); it.hasNext();) {
			AbstractButton button = (AbstractButton) it.next();
			group.add(button);
		}
		mh.s.doClick();

		return bttns;
	}
}
