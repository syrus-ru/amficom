/**
 * $Id: MapElement.java,v 1.9 2005/03/09 14:49:53 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;

/**
 *  Интерфейс для всех элементов карты. Нужен для определения координат
 *  элемента, его стратегии, выбора, передвижения, отображения и т.д.
 * 
 * 
 * 
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/03/09 14:49:53 $
 * @module map_v1
 */
public interface MapElement extends Characterizable
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
	void setName(final String name);

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
	void setMap(final Map map);

	/**
	 * Получить флаг выделения элемента.
	 * @return флаг выделения элемента
	 */
	boolean isSelected();
	
	/**
	 * Установить значения флага выделения элемента.
	 * @param selected новое значение флага выделения элемента
	 */
	void setSelected(final boolean selected);

	/**
	 * Установить значение флага сигнала тревоги.
	 * @param alarmState новое значение флага сигнала тревоги
	 */
	void setAlarmState(final boolean alarmState);
	
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
	void revert(final MapElementState state);
	
	/**
	 * флаг того, что элемент удален.
	 * @return флаг удаления элемента
	 */
	boolean isRemoved();
	
	/**
	 * Установить флаг удаления элемента.
	 * @param removed флаг удаления элемента
	 */
	void setRemoved(final boolean removed);

	/**
	 * Возвращает описывающий элемент набор параметров,
	 * который используется для экспорта.
	 * @return хэш-таблица параметров элемента
	 */
	java.util.Map getExportMap();

}

