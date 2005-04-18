/*-
 * $Id: SchemeToolBar.java,v 1.2 2005/04/18 09:55:03 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Toolkit;
import java.util.Map;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 09:55:03 $
 * @module schemeclient_v1
 */

public class SchemeToolBar extends ElementsToolBar {

	private static String[] buttons = new String[] { Constants.marqueeTool,
			Constants.separator, Constants.rectangleTool,//
			Constants.ellipseTool,//
			Constants.lineTool, Constants.textTool, Constants.separator,
			Constants.deviceTool,
			Constants.portOutKey, Constants.portInKey, Constants.linkTool,
			Constants.cableTool, Constants.separator, Constants.blockPortKey,
			Constants.createTopLevelElementKey, Constants.groupKey,
			Constants.ungroupKey, Constants.separator, Constants.deleteKey,
			// Constants.undoKey,
			// Constants.redoKey,
			Constants.separator, Constants.zoomInKey, Constants.zoomOutKey,
			Constants.zoomActualKey, Constants.separator, Constants.backgroundSize,
			Constants.separator, Constants.LINK_MODE, Constants.PATH_MODE,
			Constants.separator, Constants.TOP_LEVEL_SCHEME_MODE 
	};
	
	public SchemeToolBar(UgoTabbedPane sourcePane, UgoTabbedPane targetPane,
			ApplicationContext aContext) {
		super(sourcePane, targetPane, aContext);
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
		Map bttns = super.createGraphButtons();

		SchemeMarqueeHandler mh = pane.getMarqueeHandler();

		bttns.put(Constants.groupKey, createToolButton(mh.gr2, btn_size, null,
				"создать компонент", new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage("images/group.gif")), new CreateGroup(pane), false));

		bttns.put(Constants.createTopLevelSchemeKey, createToolButton(
				mh.scheme_ugo, btn_size, null, "УГО схемы", new ImageIcon(Toolkit
						.getDefaultToolkit().getImage("images/sheme_ugo.gif")),
				new CreateTopLevelSchemeAction(pane, targetPane, aContext), true));
		bttns.put(Constants.backgroundSize, createToolButton(mh.bSize, btn_size,
				null, "размер схемы", new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage("images/sheme_size.gif")), new SetBackgroundSizeAction(
						pane), true));

		bttns.put(Constants.LINK_MODE, createToolButton(mh.linkButt, btn_size,
				null, "режим линий", new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage("images/linkmode.gif")), new SetLinkModeAction(pane),
				true));
		bttns.put(Constants.PATH_MODE, createToolButton(mh.pathButt, btn_size,
				null, "режим путей", new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage("images/pathmode.gif")), new SetPathModeAction(pane),
				true));

		bttns.put(Constants.TOP_LEVEL_SCHEME_MODE, createToolButton(mh.topModeButt,
				btn_size, null, "режим схематичного изображения", new ImageIcon(Toolkit
						.getDefaultToolkit().getImage("images/scheme.gif")),
				new SetTopLevelModeAction(pane), true));

		ButtonGroup group = new ButtonGroup();
		group.add(mh.linkButt);
		group.add(mh.pathButt);
		mh.linkButt.doClick();
		mh.s.doClick();

		return bttns;
	}
}
