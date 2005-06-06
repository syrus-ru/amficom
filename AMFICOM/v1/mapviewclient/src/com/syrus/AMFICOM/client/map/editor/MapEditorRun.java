/**
 * $Id: MapEditorRun.java,v 1.7 2005/06/06 12:20:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */
package com.syrus.AMFICOM.client.map.editor;

import com.syrus.AMFICOM.client.model.DefaultMapEditorApplicationModelFactory;
import com.syrus.util.Application;

/**
 * Запуск приложения "Редактор топологических схем" 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/06/06 12:20:33 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}
