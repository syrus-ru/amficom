/**
 * $Id: SpatialLayer.java,v 1.3 2005/02/07 16:09:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.Client.Map;

import java.awt.Component;

/**
 * Слой географической карты. Реализация пренадлежит используемой системе ГИС.
 * Текущее использование - управление отображением слоев
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/07 16:09:25 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Setup.LayersPanel
 */
public interface SpatialLayer 
{
	/**
	 * Возвращает флаг видимости слоя на карте.
	 * @return флаг видимости
	 */
	boolean isVisible();
	
	/**
	 * Возвращает флаг видимости меток слоя на карте.
	 * @return влаг видимости метки
	 */
	boolean isLabelVisible();
	
	/**
	 * Устанавливает флаг видимости слоя на карте.
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
