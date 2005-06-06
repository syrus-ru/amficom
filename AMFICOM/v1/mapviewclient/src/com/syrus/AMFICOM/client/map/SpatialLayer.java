/**
 * $Id: SpatialLayer.java,v 1.7 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Component;

/**
 * Слой географической карты. Реализация пренадлежит используемой системе ГИС.
 * Текущее использование - управление отображением слоев
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/06/06 12:20:29 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Operations.LayersPanel
 */
public interface SpatialLayer 
{
	/**
	 * Возвращает флаг видимости слоя на карте при текущих настройках клиента.
	 * @return флаг видимости
	 */
	boolean isVisible();
	
	/**
	 * Возвращает флаг видимости меток слоя на карте.
	 * @return влаг видимости метки
	 */
	boolean isLabelVisible();

	/**
	 * Возвращает флаг видимости слоя на карте при заданном масштабе.
	 * @return флаг видимости
	 */
	boolean isVisibleAtScale(double scale);
	
	/**
	 * Возвращает флаг видимости слоя на карте при текущем масштабе.
	 * @return флаг видимости
	 */
	boolean isVisibleAtCurrentScale();

	/**
	 * Устанавливает флаг видимости слоя на карте при текущих настройках клиента.
	 * @param visible флаг видимости
	 */
	void setVisible(boolean visible);
	
	/**
	 * Устанавливает флаг видимости меток слоя на карте.
	 * @param visible флаг видимости меток
	 */
	void setLabelVisible(boolean visible);
	
	/**
	 * Возвращает компонент, содержащий схематичное изображение элемента слоя.
	 * если изображение недоступно - должен возвращать null
	 * @return компонент
	 */
	Component getLayerImage();

	/**
	 * Возвращает название слоя.
	 * @return название слоя
	 */
	String getName();
}
