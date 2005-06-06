/**
 * $Id: MapEditor.java,v 1.6 2005/06/06 10:22:35 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Editor;

import java.awt.Frame;
import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Application;

/**
 * Запуск основного окна модуля "Редактор топологических схем".
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 10:22:35 $
 * @module mapviewclient_v1
 */
public class MapEditor {
	ApplicationContext aContext = new ApplicationContext();

	public MapEditor(MapEditorApplicationModelFactory factory) {
		if(!Environment.canRun(Environment.MODULE_MAP))
			return;

		Application.init("mapviewclient");

		this.aContext.setApplicationModel(factory.create());
		Frame frame = new MapEditorMainFrame(this.aContext);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/main/map_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}

