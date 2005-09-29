/*-
 * $Id: MapElement.java,v 1.26 2005/09/29 10:46:56 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 *  Интерфейс для всех элементов карты. Нужен для определения координат
 *  элемента, его стратегии, выбора, передвижения, отображения и т.д.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.26 $, $Date: 2005/09/29 10:46:56 $
 * @module map
 */
public interface MapElement extends Identifiable, Namable {
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

	Characterizable getCharacterizable();
}
