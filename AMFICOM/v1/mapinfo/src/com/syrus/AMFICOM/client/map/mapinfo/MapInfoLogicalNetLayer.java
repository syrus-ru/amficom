package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mapinfo.mapj.MapJ;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;
import com.syrus.AMFICOM.map.DoublePoint;

public class MapInfoLogicalNetLayer extends LogicalNetLayer
{
	// protected MapImagePanel mapImagePanel = null;

	// protected MapJ localMapJ = null;
	protected MapInfoNetMapViewer nmViewer = null;

	public static final double ZOOM_FACTOR = 2D;

	public MapInfoLogicalNetLayer(NetMapViewer viewer)
	{
		super();
		Environment.log(
				Environment.LOG_LEVEL_FINER,
				"constructor call",
				getClass().getName(),
				"SpatialLogicalNetLayer(" + viewer + ")");

		// logicalLayerMapTool = new LogicalLayerMapTool(this);
		setMapViewer(viewer);
	}

	public void setMapImageSize(int width, int height)
	{
		if((width > 0) && (height > 0))
		{
			getLocalMapJ().setDeviceBounds(new DoubleRect(
					0,
					0,
					width,
					height));

			repaint(true);
		}
	}

	/**
	 * �������� �������� ���������� �� �������������� �����������
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
		try
		{
			com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(
					point.getX(),
					point.getY());
			com.mapinfo.util.DoublePoint screendp =
				getLocalMapJ().transformNumericToScreen(mapdp);
			return new Point((int )screendp.x, (int )screendp.y);
		}
		catch(Exception exc)
		{
				exc.printStackTrace();
		}

		return null;
	}

	/**
	 * �������� �������������� ���������� �� ��������
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
		try
		{
			com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.DoublePoint(
					point.x,
					point.y);
			com.mapinfo.util.DoublePoint mapdp = 
				getLocalMapJ().transformScreenToNumeric(screendp);
			return new DoublePoint(mapdp.x, mapdp.y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public double convertScreenToMap(double screenDistance)
	{
		DoublePoint p1 = convertScreenToMap(new Point(0, 0));
		DoublePoint p2 = convertScreenToMap(new Point((int )screenDistance, 0));
		double d = distance(p1, p2);

		return d;
	}

	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
	}

	public DoublePoint pointAtDistance(
			DoublePoint startPoint,
			DoublePoint endPoint,
			double dist)
	{
		double len = distance(startPoint, endPoint);

		DoublePoint point = new DoublePoint(
				startPoint.getX() + (endPoint.getX() - startPoint.getX()) / len
						* dist,
				startPoint.getY() + (endPoint.getY() - startPoint.getY()) / len
						* dist);

		return point;
	}

	/**
	 * �������� ��������� ����� ����� ������� � �������� �����������
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
		try
		{
			return getLocalMapJ().sphericalDistance(
					new com.mapinfo.util.DoublePoint(from.getX(), from.getY()),
					new com.mapinfo.util.DoublePoint(to.getX(), to.getY()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0.0D;
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 */
	public void setCenter(DoublePoint center)
	{
		try
		{
			getLocalMapJ().setCenter(new com.mapinfo.util.DoublePoint(
					center.getX(),
					center.getY()));
		}
		catch(Exception exc)
		{
			System.out.println("MILNL - Failed setting center.");
			exc.printStackTrace();
		}
	}

	/**
	 * �������� ����������� ����� ���� �����
	 */
	public DoublePoint getCenter()
	{
		com.mapinfo.util.DoublePoint center = null;
		try
		{
			center = getLocalMapJ().getCenter();
			return new DoublePoint(center.x, center.y);
		}
		catch(Exception exc)
		{
			System.out.println("MapImagePanel - Failed getting center.");
		}

		return null;
	}

	public Rectangle2D.Double getVisibleBounds()
	{
		try
		{
			DoubleRect rect = getLocalMapJ().getBounds();
			Rectangle2D.Double vb = new Rectangle2D.Double(
					rect.xmin,
					rect.ymin,
					rect.width(),
					rect.height());

			return vb;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������� ������� ���������� � ������ � ��������� ����������� �����
	 */
	public void release()
	{//empty
	}

	/**
	 * ������������ ���������� ���������� � ������
	 */
	public void repaint(boolean fullRepaint)
	{
		if(fullRepaint)
		{
			String uriString = ((MapInfoNetMapViewer )this.viewer).getConnection().getURL();
			uriString += createRenderCommandString();

			try
			{
				URI mapServerURI = new URI(uriString);
				URL mapServerURL = new URL(mapServerURI.toASCIIString());
				System.out.println("url: " + mapServerURL);
				URLConnection s = mapServerURL.openConnection();

				System.out.println("MIFLNL - repaint - Conection opened");

				if(s.getInputStream() == null)
					return;
				ObjectInputStream ois = new ObjectInputStream(s
						.getInputStream());

				System.out
						.println("MIFLNL - repaint - ObjectInputStream exists");

				try
				{
					Object readObject = ois.readObject();
					if(readObject instanceof String)
					{
						Environment.log(
								Environment.LOG_LEVEL_FINER,
								(String )readObject);
						return;
					}
				}
				catch(IOException optExc)
				{
				}

				int dataSize = this.nmViewer.mapImagePanel.getWidth()
						* this.nmViewer.mapImagePanel.getHeight() * 2;
				byte[] img = new byte[dataSize];

				try
				{
					ois.readFully(img);
				}
				catch(EOFException eofExc)
				{
				}
				
				System.out.println("MIFLNL - repaint - Image read");

				ois.close();
				System.out.println("MIFLNL - repaint - Stream closed");

				Image imageReceived = Toolkit.getDefaultToolkit().createImage(
						img);
				this.nmViewer.mapImagePanel.setImage(imageReceived);
				System.out.println("MIFLNL - repaint - Image setted");
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
		}
		else
			this.nmViewer.mapImagePanel.repaint();

		// super.paint(this.nmViewer.mapImagePanel.getGraphics());
	}

	/**
	 * ���������� ������ ���� �� ���������� ����������� �����
	 */
	public void setCursor(Cursor cursor)
	{
		System.out.println("Set cursor " + cursor.getName());
		this.nmViewer.mapImagePanel.setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return this.nmViewer.mapImagePanel.getCursor();
	}

	/**
	 * �������� ������� ������� ���� �����
	 */
	public double getScale()
	{
		double currentZoom = 0.0D;
		try
		{
			currentZoom = getLocalMapJ().getZoom();
		}
		catch(Exception exc)
		{
			System.out.println("MapImagePanel - Failed setting zoom.");
		}

		return currentZoom;
	}

	/**
	 * ���������� �������� ������� ���� �����
	 */
	public void setScale(double z)
	{
		try
		{
			getLocalMapJ().setZoom(z);
			updateZoom();
		}
		catch(Exception exc)
		{
			System.out.println("MapImagePanel - Failed setting zoom.");
			exc.printStackTrace();
		}
	}

	/**
	 * ���������� ������� ���� ����� � �������� �������������
	 */
	public void scaleTo(double scale�oef)
	{
		this.setScale(this.getScale() * scale�oef);
	}

	/**
	 * ���������� ��� ����� �� ����������� �������������
	 */
	public void zoomIn()
	{
		scaleTo(1.0D / ZOOM_FACTOR);
	}

	/**
	 * �������� ��� ����� �� ����������� �������������
	 */
	public void zoomOut()
	{
		scaleTo(ZOOM_FACTOR);
	}

	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����) ��
	 * ����������� ������� �����
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
		// System.out.println("Zoom to box (" + from.getX() + ", " + from.getY()
		// + ") - (" +
		// to.getX() + ", " + to.getY() + ")");
		try
		{
			getLocalMapJ().setBounds(new DoubleRect(from.getX(), from.getY(),
					to.getX(), to.getY()));

			updateZoom();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateZoom()
	{
		super.updateZoom();

		if(this.aContext == null)
			return;
		Dispatcher disp = this.aContext.getDispatcher();
		if(disp == null)
			return;
		Double p = new Double(getScale());
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_SCALE_CHANGED));
	}

	public void handDragged(MouseEvent me)
	{
		int shiftX = (int )(me.getX() - this.startPoint.getX());
		int shiftY = (int )(me.getY() - this.startPoint.getY());

		this.nmViewer.mapImagePanel.repaint(
				this.nmViewer.mapImagePanel.getGraphics(),
				shiftX,
				shiftY);
	}

	public List findSpatialObjects(String searchText)
	{
		List resultList = new ArrayList();

		String uriString = ((MapInfoNetMapViewer )this.viewer).getConnection().getURL();
		uriString += createSearchCommandString(searchText);

		try
		{
			URI mapServerURI = new URI(uriString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			System.out.println("url: " + mapServerURL);
			URLConnection s = mapServerURL.openConnection();

			System.out.println("MIFLNL - searchText - Conection opened");

			if(s.getInputStream() == null)
				return resultList;
			
			ObjectInputStream ois = new ObjectInputStream(s
					.getInputStream());

			System.out
					.println("MIFLNL - searchText - ObjectInputStream exists");

			//reading possible error from server
			try
			{
				Object readObject = ois.readObject();
				if(readObject instanceof String)
				{
					Environment.log(
							Environment.LOG_LEVEL_FINER,
							(String )readObject);
					return resultList;
				}
			}
			catch(IOException optExc)
			{
			}

			//reading names and centers
			try
			{
				for(;;)
				{
					double xCoord  = ois.readDouble();
					double yCoord  = ois.readDouble();				
					String featureName = (String)ois.readObject();
					
					resultList.add(new MapInfoSpatialObject(
							new DoublePoint(xCoord,yCoord),
							featureName));
				}
			}
			catch(EOFException eofExc)
			{
			}
			System.out.println("MIFLNL - searchText - Spatial objects read");

			ois.close();
			System.out.println("MIFLNL - searchText - Stream closed");
		}

		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		return resultList;
	}

	public void centerSpatialObject(SpatialObject so)
	{
		MapInfoSpatialObject miso = (MapInfoSpatialObject )so;
		this.setCenter(miso.getCenter());
	}

	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "setMapViewer(" + mapViewer + ")");

		super.setMapViewer(mapViewer);

		this.nmViewer = (MapInfoNetMapViewer )mapViewer;
		// this.mapImagePanel = this.nmViewer.mapImagePanel;
		// this.localMapJ = this.nmViewer.localMapJ;
	}

	public String createRenderCommandString()
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
		+ ServletCommandNames.CN_RENDER_IMAGE;
		
		result += "&" + ServletCommandNames.PAR_WIDTH + "="
				+ this.nmViewer.mapImagePanel.getWidth();
		result += "&" + ServletCommandNames.PAR_HEIGHT + "="
				+ this.nmViewer.mapImagePanel.getHeight();
		result += "&" + ServletCommandNames.PAR_CENTER_X + "="
				+ this.getCenter().getX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "="
				+ this.getCenter().getY();
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + this.getScale();

		int index = 0;
		Iterator layersIt = ((MapInfoNetMapViewer )this.viewer).getLayers()
				.iterator();
		for(; layersIt.hasNext();)
		{
			SpatialLayer spL = (SpatialLayer )layersIt.next();
			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "="
					+ (spL.isVisible() ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index
					+ "=" + (spL.isLabelVisible() ? 1 : 0);
			index++;
		}

		return result;
	}

	public String createSearchCommandString(String nameToSearch)
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
			+ ServletCommandNames.CN_SEARCH_NAME;
		
		result += "&" + ServletCommandNames.PAR_NAME_TO_SEARCH + "=" + nameToSearch;

		return result;
	}
	
	public MapJ getLocalMapJ()
	{
		MapInfoConnection miConnection = 
			(MapInfoConnection)this.nmViewer.getConnection();
		
		return miConnection.getLocalMapJ();
	}
}
