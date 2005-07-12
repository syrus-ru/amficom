package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.logging.Level;

import javax.swing.JComponent;

import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageRenderer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.util.Log;

public class MapInfoNetMapViewer extends NetMapViewer
{
	protected MapImagePanel visualComponent = null;

	protected MapInfoConnection mapConnection = null;

	protected DropTarget dropTarget = null;

	public MapInfoNetMapViewer(
			LogicalNetLayer logicalNetLayer,
			MapContext mapContext,
			MapImageRenderer renderer) throws MapDataException,
			MapConnectionException
	{
		super(logicalNetLayer, mapContext, renderer);

		this.mapConnection = (MapInfoConnection) this.mapContext.getMapConnection();
		this.visualComponent = new MapImagePanel(this);
	}

	public void init() throws MapDataException
	{
		super.init();
		try
		{
			this.dropTarget = new DropTarget(this.visualComponent, this.dtl);
			this.dropTarget.setActive(true);

			this.visualComponent.addMouseListener(this.mttp.toolTippedPanelListener);
			this.visualComponent
					.addMouseMotionListener(this.mttp.toolTippedPanelListener);

			this.visualComponent.addKeyListener(this.mka);
			this.visualComponent.grabFocus();

			this.visualComponent.addMouseListener(this.ml);
			this.visualComponent.addMouseMotionListener(this.mml);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new MapDataException("abracadabra");
		}
	}

	public void dispose()
	{
		this.renderer.cancel();
	}

	/**
	 * Получить графический компонент, в котором отображается картография
	 */
	public JComponent getVisualComponent()
	{
		return this.visualComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#getVisibleBounds()
	 */
	public Double getVisibleBounds() throws MapConnectionException,
			MapDataException
	{
		try
		{
			DoubleRect rect = this.mapConnection.getLocalMapJ().getBounds();
			Rectangle2D.Double vb = new Rectangle2D.Double(rect.xmin, rect.ymin, rect
					.width(), rect.height());

			return vb;
		} catch (Exception ex)
		{
			throw new MapConnectionException("cannot get visible bounds", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#centerSpatialObject(com.syrus.AMFICOM.client.map.SpatialObject)
	 */
	public void centerSpatialObject(SpatialObject so)
			throws MapConnectionException, MapDataException
	{
		MapInfoSpatialObject miso = (MapInfoSpatialObject) so;
		this.mapContext.setCenter(miso.getCenter());
		this.renderer.setCenter(miso.getCenter());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#repaint(boolean)
	 */
	public void repaint(boolean fullRepaint) throws MapConnectionException,
			MapDataException
	{
		long t1 = System.currentTimeMillis();		
		if (fullRepaint)
		{
			Log.debugMessage(" MIFLNL - repaint - Entered full repaint",
					Level.FINEST);

			this.visualComponent.setMapImage(this.renderer.getImage());
			Log.debugMessage(" MIFLNL - repaint - Exiting full repaint",
					Level.FINEST);
		}

		long t2 = System.currentTimeMillis();		
		this.visualComponent.refreshLayerImage();
		long t3 = System.currentTimeMillis();		
		this.visualComponent.repaint();
		long t4 = System.currentTimeMillis();

		Log.debugMessage("MapInfoNetMapViewer.repaint | finished\n"
				+ (t2 - t1) + "(image loaded)\n"
				+ (t3 - t2) + "(LogicalNetLayer image painted to the buffer)\n"				
				+ (t4 - t3) + "(visual component repainted).", Level.FINE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#setCursor(java.awt.Cursor)
	 */
	public void setCursor(Cursor cursor)
	{
		this.visualComponent.setCursor(cursor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#getCursor()
	 */
	public Cursor getCursor()
	{
		return this.visualComponent.getCursor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#handDragged(java.awt.event.MouseEvent)
	 */
	public void handDragged(MouseEvent me) throws MapConnectionException,
			MapDataException
	{
		// empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#handMoved(java.awt.event.MouseEvent)
	 */
	public void handMoved(MouseEvent me) throws MapConnectionException,
			MapDataException
	{
		// empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.NetMapViewer#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me) throws MapConnectionException,
			MapDataException
	{
		this.renderer.analyzeMouseLocation(me);
	}

	public void setMapImageSize(Dimension newSize) throws MapConnectionException,
			MapDataException
	{
		if ((newSize.width > 0) && (newSize.height > 0))
		{
			this.mapConnection.getLocalMapJ().setDeviceBounds(
					new DoubleRect(0, 0, newSize.width, newSize.height));

			this.renderer.setSize(newSize);

			this.repaint(true);
		}
	}
}
