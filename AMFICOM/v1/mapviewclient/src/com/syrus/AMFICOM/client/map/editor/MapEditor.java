/*-
 * $$Id: MapEditor.java,v 1.18 2005/10/21 14:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.DefaultMapEditorApplicationModelFactory;

/**
 * Запуск основного окна модуля "Редактор топологических схем".
 * 
 * @version $Revision: 1.18 $, $Date: 2005/10/21 14:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditor extends AbstractApplication {
	public static final String APPLICATION_NAME = "map_editor"; //$NON-NLS-1$

	public MapEditor() {
		super(MapEditor.APPLICATION_NAME);
	}

	@Override
	protected void init() {
		super.aContext
				.setApplicationModel(new DefaultMapEditorApplicationModelFactory()
						.create());
		super.startMainFrame(
				new MapEditorMainFrame(this.aContext),
				(Image) UIManager.get("images/main/map_mini.gif")); //$NON-NLS-1$
	}

	public static void main(String[] args) {
		Launcher.launchApplicationClass(MapEditor.class);
//		new MapEditor();
	}
}
