/**
 * $Id: MapEditor.java,v 1.8 2005/06/14 12:07:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.Frame;
import java.awt.Toolkit;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModelFactory;
import com.syrus.util.Application;

/**
 * Запуск основного окна модуля "Редактор топологических схем".
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/06/14 12:07:14 $
 * @module mapviewclient_v1
 */
public class MapEditor {
	ApplicationContext aContext = new ApplicationContext();

	public MapEditor(MapEditorApplicationModelFactory factory) {
		if(!Environment.canRun(Environment.MODULE_MAP))
			return;

		Application.init("mapviewclient");
		this.aContext.setApplicationModel(factory.create());
		this.aContext.setDispatcher(new Dispatcher());
		Frame frame = new MapEditorMainFrame(this.aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/main/map_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}

