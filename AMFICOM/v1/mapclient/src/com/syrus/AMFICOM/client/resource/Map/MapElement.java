/**
 * $Id: MapElement.java,v 1.9 2004/09/28 07:58:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

//import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *  Интерфейс для всех элементов карты. Нужен для определения координат
 *  элемента, его стратегии, выбора, передвижения, отображения и т.д.
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/09/28 07:58:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public interface MapElement 
{
	/**
	 * отрисовка элемента
	 */
	void paint (Graphics g, Rectangle2D.Double visibleBounds);

	/**
	 * поучить идентификатор элемента
	 */
	String getId();

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

	/**
	 * возвращает флаг, указывающий, что точка currentMousePoint находится
	 * в определенных границах элемента. Для узла границы определяются
	 * размерами иконки, для линии дельта-окрестностью линии. Дельта задается
	 * полем mouseTolerancy
	 */
	boolean isMouseOnThisObject(Point currentMousePoint);

	boolean isVisible(Rectangle2D.Double visibleBounds);

	/**
	 * панель свойств элемента
	 */
//	String getPropertyPaneClassName();

	/**
	 * флаг возможности передвижения элемента
	 */
	boolean isMovable();
	
	/**
	 * текст всплывающей подсказки
	 */
	String getToolTipText();

	/**
	 * центр (ГМТ) элемента
	 */
	Point2D.Double getAnchor();

	/**
	 * дублирование элемента
	 */	
	Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException;
	
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

	String[][] getExportColumns();

	void setColumn(String field, String value);
	
//	ObjectResourceController getPropertiesController();
}

