/**
 * $Id: SpatialLayer.java,v 1.2 2005/02/03 16:24:59 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/02/03 16:24:59 $
 * @module mapviewclient_v1
 */
public interface SpatialLayer 
{
	/**
	 * Возвращает флаг видимости слоя на карте.
	 */
	boolean isVisible();
	
	/**
	 * Возвращает флаг видимости меток слоя на карте.
	 */
	boolean isLabelVisible();
	
	/**
	 * Устанавливает флаг видимости слоя на карте.
	 */
	void setVisible(boolean visible);
	
	/**
	 * Устанавливает флаг видимости меток слоя на карте.
	 */
	void setLabelVisible(boolean visible);
	
	/**
	 * Возвращает компонент, содержащий схематичное изображение элемента слоя.
	 * если изображение недоступно - должен возвращать null
	 */
	Component getLayerImage();

	/**
	 * Возвращает название слоя.
	 * @return название слоя
	 */
	String getName();
}
