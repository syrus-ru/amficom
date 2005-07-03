/**
 * $Id: MapElement.java,v 1.13 2005/05/30 14:50:23 krupenn Exp $
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
import com.syrus.AMFICOM.general.Namable;

/**
 *  Интерфейс для всех элементов карты. Нужен для определения координат
 *  элемента, его стратегии, выбора, передвижения, отображения и т.д.
 *
 *
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/05/30 14:50:23 $
 * @module map_v1
 */
public interface MapElement extends Characterizable, Namable
{
	/**
	 * Поучить идентификатор элемента.
	 * @return Идентификатор элемента карты.
	 */
	Identifier getId();

	/**
	 * Получить флаг выделения элемента.
	 * @return флаг выделения элемента
	 */
	boolean isSelected();
	
	/**
	 * Установить значения флага выделения элемента.
	 * Для функционального выделения элемента в пользовательском приложении
	 * следует использовать метод {@link Map#setSelected(MapElement, boolean)},
	 * который и устанавливает флаг для данного элемента
	 * @param selected новое значение флага выделения элемента
	 * @see Map#setSelected(MapElement, boolean)
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
	 * @deprecated use <pre>XMLBeansTransferable.fillXMLTransferable(XMLObject)</pre> instead
	 */
	java.util.Map getExportMap();

}

