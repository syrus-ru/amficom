/**
 * $Id: SpatialLayer.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public interface SpatialLayer 
{
	/**
	 * возвращает флаг видимости слоя на карте
	 */
	boolean isVisible();
	
	/**
	 * возвращает флаг видимости меток слоя на карте
	 */
	boolean isLabelVisible();
	
	/**
	 * устанавливает флаг видимости слоя на карте
	 */
	void setVisible(boolean visible);
	
	/**
	 * устанавливает флаг видимости меток слоя на карте
	 */
	void setLabelVisible(boolean visible);
	
	/**
	 * возвращает компонент, содержащий схематичное изображение элемента слоя.
	 * если изображение недоступно - должен возвращать null
	 */
	Component getLayerImage();

	/**
	 * возвращает название слоя
	 */	
	String getName();
}
