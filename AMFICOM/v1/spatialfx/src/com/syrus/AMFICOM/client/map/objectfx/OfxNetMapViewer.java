/**
 * $Id: OfxNetMapViewer.java,v 1.9 2005/06/20 15:38:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.ofx.component.swing.JMapViewer;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxRectangle;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageRenderer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialObject;

/**
 * 
 * @version $Revision: 1.9 $, $Date: 2005/06/20 15:38:54 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
 */
public class OfxNetMapViewer extends NetMapViewer
{
	OfxConnection mapConnection;

	protected DropTarget dropTarget = null;

	protected JPanel visualComponent = null;

	public OfxNetMapViewer(LogicalNetLayer logicalNetLayer, MapContext mapContext, MapImageRenderer renderer) throws MapDataException, MapConnectionException {
		super(logicalNetLayer, mapContext, renderer);

		OfxContext ofxContext = (OfxContext )mapContext;
		this.mapConnection = (OfxConnection )ofxContext.getMapConnection();
		//TODO how spatialLayer should be repainted?
		this.mapConnection.setOfxLayerPainter(this);
		
		JMapViewer jMapViewer = this.mapConnection.getJMapViewer();
		
		if(jMapViewer == null)
			throw new MapDataException("Соединение не инициализировано.");

		this.visualComponent = new JPanel();
		this.visualComponent.setLayout(new BorderLayout());
		this.visualComponent.add(jMapViewer);
	}
	
	public void init() throws MapDataException {
		super.init();
		try
		{
			JMapViewer jMapViewer = this.mapConnection.getJMapViewer();

			this.dropTarget = new DropTarget( jMapViewer.getMapCanvas(), this.dtl);
			this.dropTarget.setActive(true);

			jMapViewer.addMouseListener(this.mttp.toolTippedPanelListener);
			jMapViewer.addMouseMotionListener(this.mttp.toolTippedPanelListener);

			this.visualComponent.addKeyListener(this.mka);
			this.visualComponent.grabFocus();

			jMapViewer.addMouseListener(this.ml);
			jMapViewer.addMouseMotionListener(this.mml);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new MapDataException("abracadabra");
		}
	}

	public void dispose()
	{
		JMapViewer jMapViewer = this.mapConnection.getJMapViewer();
		jMapViewer.removeMouseListener(this.mttp.toolTippedPanelListener);
		jMapViewer.removeMouseMotionListener(this.mttp.toolTippedPanelListener);
		this.dropTarget.setActive(false);
		super.dispose();
	}
		
	public JComponent getVisualComponent()
	{
		return this.visualComponent;
	}

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint(boolean fullRepaint)
	{
		this.mapConnection.getSpatialLayer().postDirtyEvent();
		this.mapConnection.getSpatialLayer().postPaintEvent();
	}
	
	public Rectangle2D.Double getVisibleBounds()
	{
		SxRectangle sxRect = this.mapConnection.getSxMapViewer().getMapCanvas().getGroundRect();
		sxRect = this.mapConnection.getSxMapViewer().convertDBToLatLong(sxRect);
		Rectangle2D.Double rect = new Rectangle2D.Double(
			sxRect.getBottomLeft().getX(),
			sxRect.getBottomLeft().getY(),
			sxRect.getWidth(),
			sxRect.getHeight());
		return rect;
	}

	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public void setCursor(Cursor cursor)
	{
		this.mapConnection.getSxMapViewer().getMapCanvas().setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return this.mapConnection.getSxMapViewer().getMapCanvas().getCursor();
	}

	public void handDragged(MouseEvent me)
	{
		java.awt.Point point = new Point(
				me.getX() - (int )this.logicalNetLayer.getStartPoint().getX(), 
				me.getY() - (int )this.logicalNetLayer.getStartPoint().getY());
		this.mapConnection.getSxMapViewer().getMapCanvas().setBufferOffset(point);
		this.mapConnection.getSxMapViewer().getMapCanvas().repaint();
	}
	
	public void handMoved(MouseEvent me) throws MapConnectionException, MapDataException {
		// nothing
	}

	public void mouseMoved(MouseEvent me) throws MapConnectionException, MapDataException {
		// nothing
	}

	public void centerSpatialObject(SpatialObject so)
	{
		try 
		{
			OfxSpatialObject oso = (OfxSpatialObject)so;
			SxDoublePoint center = oso.getSxSpatialObject().geometry.getCenter();
			System.out.print("Center " + center.getX() + ", " + center.getY());
			center = this.mapConnection.getSxMapViewer().convertLatLongToMap(center);
			System.out.println(" --> " + center.getX() + ", " + center.getY());
			this.mapConnection.getSxMapViewer().setCenter(center.getX(), center.getY());
		} 
		catch (Exception ex) 
		{
			System.out.println("Cannot center object: ");
			ex.printStackTrace();
			
		} 
	}

}
