package com.syrus.AMFICOM.Client.Resource.Map;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import com.syrus.AMFICOM.map.MapElement;

public interface MapElementController 
{
	void setLogicalNetLayer(LogicalNetLayer lnl);
	
	LogicalNetLayer getLogicalNetLayer();

	/**
	 * отрисовка элемента
	 */
	void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds);

	/**
	 * возвращает флаг, указывающий, что точка currentMousePoint находитс€
	 * в определенных границах элемента. ƒл€ узла границы определ€ютс€
	 * размерами иконки, дл€ линии дельта-окрестностью линии. ƒельта задаетс€
	 * полем mouseTolerancy
	 */
	boolean isMouseOnElement(MapElement me, Point currentMousePoint);

	/**
	 * определить, попадает ли элемент в область visibleBounds.
	 * »спользуетс€ при отрисовке (отображаютс€ только элементы, попавшие
	 * в видимую область)
	 */
	boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds);

	/**
	 * текст всплывающей подсказки
	 */
	String getToolTipText(MapElement me);
}