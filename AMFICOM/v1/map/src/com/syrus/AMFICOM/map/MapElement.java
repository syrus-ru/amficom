/**
 * $Id: MapElement.java,v 1.6 2005/01/20 14:44:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import java.util.List;

/**
 *  Интерфейс для всех элементов карты. Нужен для определения координат
 *  элемента, его стратегии, выбора, передвижения, отображения и т.д.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/01/20 14:44:30 $
 * @module map_v1
 */
public interface MapElement 
{
	/**
	 * поучить идентификатор элемента
	 */
	Identifier getId();

	String getName();

	void setName(String name);

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

	void setAlarmState(boolean alarmState);
	
	boolean getAlarmState();

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

	List getCharacteristics();
	
	void addCharacteristic(Characteristic characteristic);

	void removeCharacteristic(Characteristic characteristic);

	/**
	 * Возвращает набор параметров, описывающих топологическюу схему,
	 * который используется для экспорта
	 */
	java.util.Map getExportMap();

}

