/**
 * $Id: MapElement.java,v 1.13 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

/**
 *  Интерфейс для всех элементов карты. Нужен для определения координат
 *  элемента, его стратегии, выбора, передвижения, отображения и т.д.
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public interface MapElement 
{
	/**
	 * поучить идентификатор элемента
	 */
	String getId();

	/**
	 * возвращает карту
	 */
	Map getMap();
	
	/**
	 * установить объект карты
	 */
	void setMap(Map map);

	/**
	 * флаг выделения элемента
	 */
	boolean isSelected();
	
	/**
	 * установить выделение элемента
	 */
	void setSelected(boolean selected);

	/**
	 * центр (ГМТ) элемента
	 */
	DoublePoint getLocation();

	/**
	 * получить текущее состояние элемента
	 */
	MapElementState getState();
	
	/**
	 * восстановить состояние элемента
	 */
	void revert(MapElementState state);
	
	/**
	 * флаг того, что элемент удален
	 */
	boolean isRemoved();
	
	/**
	 * установить флаг удаления элемента
	 */
	void setRemoved(boolean removed);

	/**
	 * Возвращает массив параметров, описывающих топологическюу схему,
	 * который используется для экспорта
	 */
	String[][] getExportColumns();

	/**
	 * установить параметр топологический схемы по переданному значению.
	 * используется при импорте
	 */	
	void setColumn(String field, String value);
	
}

