/*-
 * $Id: SchemeToolBar.java,v 1.9 2005/08/05 18:44:38 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateGroup;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateTopLevelSchemeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SetLinkModeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SetPathModeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SetTopLevelModeAction;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/08/05 18:44:38 $
 * @module schemeclient_v1
 */

public class SchemeToolBar extends ElementsToolBar {
	private static final long serialVersionUID = 7897889339745050703L;

	private static String[] buttons = new String[] { Constants.MARQUEE,
			Constants.SEPARATOR, Constants.RECTANGLE,//
			Constants.ELLIPSE,//
			Constants.LINE, Constants.TEXT, Constants.SEPARATOR,
			Constants.DEVICE,
			Constants.PORT, Constants.CABLE_PORT, Constants.LINK,
			Constants.CABLE, Constants.SEPARATOR, Constants.BLOCK_PORT,
			Constants.CREATE_UGO, Constants.GROUP,
			Constants.UNGROUP, Constants.SEPARATOR, Constants.DELETE,
			// Constants.undoKey,
			// Constants.redoKey,
			Constants.SEPARATOR, Constants.ZOOM_IN, Constants.ZOOM_OUT,
			Constants.ZOOM_ACTUAL, Constants.SEPARATOR, 
			Constants.SEPARATOR, Constants.LINK_MODE, Constants.PATH_MODE,
			Constants.SEPARATOR, Constants.TOP_LEVEL_MODE, Constants.HORIZONTAL_GLUE 
	};
	
	public SchemeToolBar(UgoTabbedPane sourcePane,
			ApplicationContext aContext) {
		super(sourcePane, aContext);
	}

	@Override
	protected String[] getButtons() {
		return buttons;
	}
	
	@Override
	protected Map<String, AbstractButton> createGraphButtons() {
		Map<String, AbstractButton> bttns = super.createGraphButtons();

		SchemeMarqueeHandler mh = this.pane.getMarqueeHandler();

		bttns.put(Constants.GROUP, createToolButton(mh.gr2, this.btn_size, null,
				LangModelGraph.getString(Constants.GROUP), Constants.ICON_GROUP, new CreateGroup(this.pane, DeviceGroup.SCHEME_ELEMENT), false));
		bttns.put(Constants.CREATE_UGO, createToolButton(
				mh.scheme_ugo, this.btn_size, null, LangModelGraph.getString(Constants.CREATE_UGO), 
				Constants.ICON_CREATE_UGO,
				new CreateTopLevelSchemeAction(this.pane), true));
		bttns.put(Constants.LINK_MODE, createToolButton(mh.linkButt, this.btn_size,
				null, LangModelGraph.getString(Constants.LINK_MODE), Constants.ICON_LINK_MODE, new SetLinkModeAction(this.pane),
				true));
		bttns.put(Constants.PATH_MODE, createToolButton(mh.pathButt, this.btn_size,
				null, LangModelGraph.getString(Constants.PATH_MODE), Constants.ICON_PATH_MODE, new SetPathModeAction(this.pane),
				true));

		bttns.put(Constants.TOP_LEVEL_MODE, createToolButton(mh.topModeButt,
				this.btn_size, null, LangModelGraph.getString(Constants.TOP_LEVEL_MODE), Constants.ICON_TOP_LEVEL_MODE,
				new SetTopLevelModeAction(this.pane), true));

		ButtonGroup group = new ButtonGroup();
		group.add(mh.linkButt);
		group.add(mh.pathButt);
		mh.linkButt.doClick();
		mh.s.doClick();

		return bttns;
	}
}
