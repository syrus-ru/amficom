/**
 * $Id: MapEditor.java,v 1.16 2005/09/22 10:39:11 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
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
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/09/22 10:39:11 $
 * @module mapviewclient
 */
public class MapEditor extends AbstractApplication {
	public static final String APPLICATION_NAME = "mapeditor"; //$NON-NLS-1$

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
