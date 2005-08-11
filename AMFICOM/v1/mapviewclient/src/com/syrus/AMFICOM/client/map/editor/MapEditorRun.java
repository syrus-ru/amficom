/**
 * $Id: MapEditorRun.java,v 1.11 2005/08/11 12:43:31 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */
package com.syrus.AMFICOM.client.map.editor;

/**
 * Запуск приложения "Редактор топологических схем" 
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2005/08/11 12:43:31 $
 * @module mapviewclient
 * @author $Author: arseniy $
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		new MapEditor();
	}
}
