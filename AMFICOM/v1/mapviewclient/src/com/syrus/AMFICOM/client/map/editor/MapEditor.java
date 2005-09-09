/**
 * $Id: MapEditor.java,v 1.13 2005/09/09 17:26:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.DefaultMapEditorApplicationModelFactory;

/**
 * Запуск основного окна модуля "Редактор топологических схем".
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/09/09 17:26:12 $
 * @module mapviewclient
 */
public class MapEditor extends AbstractApplication {
	public static final String APPLICATION_NAME = "mapeditor";

	public MapEditor() {
		super(MapEditor.APPLICATION_NAME);
	}

	@Override
	protected void init() {
		super.init();
		super.aContext
				.setApplicationModel(new DefaultMapEditorApplicationModelFactory()
						.create());
		super.startMainFrame(
				new MapEditorMainFrame(this.aContext),
				(Image) UIManager.get("images/main/map_mini.gif"));
	}

	public static void main(String[] args) {
		new MapEditor();
	}
}
