/**
 * $Id: MapElement.java,v 1.7 2005/01/27 14:43:37 krupenn Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/01/27 14:43:37 $
 * @module map_v1
 */
public interface MapElement 
{
	/**
	 * Поучить идентификатор элемента.
	 * @return Идентификатор элемента карты.
	 */
	Identifier getId();

	/**
	 * Полуяить название элемента.
	 * @return Название элемента карты.
	 */
	String getName();

	/**
	 * Установить название элемента.
	 * @param name Новое название.
	 */
	void setName(String name);

	/**
	 * Поулчить ссылку на топологическую схему, на которую нанесен данный элемент.
	 * @return Ссылка на топологическую схему
	 */
	Map getMap();
	
	/**
	 * Установить объект карты. Используется при создании нового элемента, при
	 * подргузке элемента из базы данных и при 
	 * @param map ссылка на объект карты
	 */
	void setMap(Map map);

	/**
	 * Получить флаг выделения элемента.
	 * @return флаг выделения элемента
	 */
	boolean isSelected();
	
	/**
	 * Установить значения флага выделения элемента.
	 * @param selected новое значение флага выделения элемента
	 */
	void setSelected(boolean selected);

	/**
	 * Установить значение флага сигнала тревоги.
	 * @param alarmState новое значение флага сигнала тревоги
	 */
	void setAlarmState(boolean alarmState);
	
	/**
	 * Поучить значение флага сигнала тревоги.
	 * @return значение флага сигнала тревоги
	 */
	boolean getAlarmState();

	/**
	 * Получить центр (ГМТ) элемента.
	 * @return географические коорлинаты центра элемента
	 */
	DoublePoint getLocation();

	/**
	 * Получить текущее состояние элемента.
	 * @return текущее состояние элемента
	 */
	MapElementState getState();
	
	/**
	 * Восстановить состояние элемента.
	 * @param state состояние элемента
	 */
	void revert(MapElementState state);
	
	/**
	 * флаг того, что элемент удален.
	 * @return 1
	 * @deprecated 1
	 */
	boolean isRemoved();
	
	/**
	 * Установить флаг удаления элемента.
	 * @param removed 1
	 * @deprecated 1
	 */
	void setRemoved(boolean removed);

	/**
	 * Получить список атрибутов отображения.
	 * @return список атрибутов отображения
	 */
	List getCharacteristics();
	
	/**
	 * Добавить элементу новый атрибут отображения.
	 * @param characteristic новый атрибут отображения
	 */
	void addCharacteristic(Characteristic characteristic);

	/**
	 * Убрать из элемента атрибут отображения.
	 * @param characteristic атрибут отображения
	 */
	void removeCharacteristic(Characteristic characteristic);

	/**
	 * Возвращает описывающий элемент набор параметров,
	 * который используется для экспорта.
	 * @return хэш-таблица параметров элемента
	 */
	java.util.Map getExportMap();

}

