/**
 * $Id: MapElementController.java,v 1.6 2005/06/06 12:20:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Интерфейс контроллера элемента карты. Позволяет управлять отображением
 * элементов карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:20:32 $
 * @module mapviewclient_v1
 */
public interface MapElementController 
{
	/**
	 * Установить логический слой .
	 * @param lnl логический слой
	 */
	void setLogicalNetLayer(LogicalNetLayer lnl);
	
	/**
	 * Получить логический слой.
	 * @return логический слой
	 */
	LogicalNetLayer getLogicalNetLayer();

	/**
	 * Отрисовать элемент. При отрисовке необходимо производить проверку 
	 * вхождения элемента карты в вилимую область, и отрисовывать только
	 * в этом случае.
	 * @param me элемент карты, который необходимо отрисовать
	 * @param g графический контекст
	 * @param visibleBounds видимая облать
	 */
	void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException;

	/**
	 * Возвращает флаг, указывающий, что точка currentMousePoint находится
	 * в определенных границах элемента. Для узла границы определяются
	 * размерами иконки, для линии дельта-окрестностью линии. Дельта задается
	 * полем {@link com.syrus.AMFICOM.Client.Map.MapPropertiesManager#getMouseTolerancy()}.
	 * @param me элемент карты
	 * @param currentMousePoint точка в экранных координатах
	 * @return <code>true</code>, если точка на элементе карты, иначе 
	 * <code>false</code>
	 */
	boolean isMouseOnElement(MapElement me, Point currentMousePoint)
		throws MapConnectionException, MapDataException;

	/**
	 * Определить, попадает ли элемент в область visibleBounds.
	 * Используется при отрисовке (отображаются только элементы, попавшие
	 * в видимую область).
	 * @param me элемент карты
	 * @param visibleBounds видимая облать
	 * @return <code>true</code>, если элемент попадает в область, иначе 
	 * <code>false</code>
	 */
	boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException;

	/**
	 * Получить текст всплывающей подсказки для элемента карты.
	 * @param me элемент карты
	 * @return строка для всплывающей подсказки
	 */
	String getToolTipText(MapElement me);
}
