/*-
 * $Id: SchemeToolBar.java,v 1.5 2005/06/22 10:16:06 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.Map;

import javax.swing.ButtonGroup;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/06/22 10:16:06 $
 * @module schemeclient_v1
 */

public class SchemeToolBar extends ElementsToolBar {

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
			Constants.ZOOM_ACTUAL, Constants.SEPARATOR, Constants.BACKGROUND_SIZE,
			Constants.SEPARATOR, Constants.LINK_MODE, Constants.PATH_MODE,
			Constants.SEPARATOR, Constants.TOP_LEVEL_MODE 
	};
	
	public SchemeToolBar(UgoTabbedPane sourcePane,
			ApplicationContext aContext) {
		super(sourcePane, aContext);
	}

	protected String[] getButtons() {
		return buttons;
	}
	
	protected Map createGraphButtons() {
		Map bttns = super.createGraphButtons();

		SchemeMarqueeHandler mh = pane.getMarqueeHandler();

		bttns.put(Constants.GROUP, createToolButton(mh.gr2, btn_size, null,
				LangModelGraph.getString(Constants.GROUP), Constants.ICON_GROUP, new CreateGroup(pane), false));
		bttns.put(Constants.CREATE_UGO, createToolButton(
				mh.scheme_ugo, btn_size, null, LangModelGraph.getString(Constants.CREATE_UGO), 
				Constants.ICON_CREATE_UGO,
				new CreateTopLevelSchemeAction(pane, aContext), true));
		bttns.put(Constants.BACKGROUND_SIZE, createToolButton(mh.bSize, btn_size,
				null, LangModelGraph.getString(Constants.BACKGROUND_SIZE), Constants.ICON_SCHEME_SIZE, new SetBackgroundSizeAction(
						pane), true));
		bttns.put(Constants.LINK_MODE, createToolButton(mh.linkButt, btn_size,
				null, LangModelGraph.getString(Constants.LINK_MODE), Constants.ICON_LINK_MODE, new SetLinkModeAction(pane),
				true));
		bttns.put(Constants.PATH_MODE, createToolButton(mh.pathButt, btn_size,
				null, LangModelGraph.getString(Constants.PATH_MODE), Constants.ICON_PATH_MODE, new SetPathModeAction(pane),
				true));

		bttns.put(Constants.TOP_LEVEL_MODE, createToolButton(mh.topModeButt,
				btn_size, null, LangModelGraph.getString(Constants.TOP_LEVEL_MODE), Constants.ICON_TOP_LEVEL_MODE,
				new SetTopLevelModeAction(pane), true));

		ButtonGroup group = new ButtonGroup();
		group.add(mh.linkButt);
		group.add(mh.pathButt);
		mh.linkButt.doClick();
		mh.s.doClick();

		return bttns;
	}
}
