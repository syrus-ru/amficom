/**
 * $Id: NetMapViewer.java,v 1.16 2005/06/15 07:42:28 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JComponent;

import com.syrus.AMFICOM.client.model.Environment;

/**
 * �����, ������� �����������, ��������� � ���� ����� 
 * ���������������� �� ���������� ������������ ���������������� ����������. 
 * ������, ����������� ������������ ���������� � ��� ��� ���� ������� 
 * (SpatialFX, MapInfo) ��������� ���� ����� � ��������� �������������
 * ��� ������� �������� �����������.
 * ��� ����, ����� �������� ���������, ���������� � ���� ����������� 
 * �����������, ������� ������� ����� {@link #getVisualComponent()}
 * <br> ���������� com.syrus.AMFICOM.client.map.objectfx.OfxNetMapViewer 
 * <br> ���������� com.syrus.AMFICOM.client.map.mapinfo.MapInfoNetMapViewer
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/06/15 07:42:28 $
 * @module mapviewclient_v1
 */
public abstract class NetMapViewer {
	private LogicalNetLayer logicalNetLayer;
	private MapContext mapContext;
	private MapImageRenderer renderer;

	private Dimension lastVisCompSize = null;
	private Image mapShotImage = null;
	
	public NetMapViewer(
			LogicalNetLayer logicalNetLayer,
			MapContext mapContext,
			MapImageRenderer renderer) {
		this.logicalNetLayer = logicalNetLayer;
		this.mapContext = mapContext;
		this.renderer = renderer;
	}
	
	/**
	 * ������������� ������ ����������� �����������. ������� �������� - 
	 * ����������� � ���������� ���������������� ����������. ��� ����������
	 * ������ ������������� �������� �� ����������� ��������������� ����������
	 * ������� �������������� ���� �����
	 * <br> ���������� com.syrus.AMFICOM.client.map.objectfx.OfxNetMapViewer.init() 
	 * <br> ���������� com.syrus.AMFICOM.client.map.mapinfo.MapInfoNetMapViewer.init()
	 */	
	public abstract void init()
		throws MapDataException;

	/**
	 * ������������ ���������� ������� ���������� ����������� ����� ��� 
	 * ��������� ������.
	 */
	public void saveConfig() {
		//empty
	}

	/**
	 * �������� ���������� ����.
	 * @return 
	 * ���������� ����
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	/**
	 * �������� ����������� ���������, � ������� ������������ �����������.
	 * @return ���������
	 */
	public abstract JComponent getVisualComponent();

	/**
	 * �������� ������� ������� � �������������� �����������.
	 * @return ������� �������
	 */
	public abstract Rectangle2D.Double getVisibleBounds()
		throws MapConnectionException, MapDataException;

	/**
	 * ������������ �������������� ������.
	 * @param so �������������� ������
	 */
	public abstract void centerSpatialObject(SpatialObject so)
		throws MapConnectionException, MapDataException;

	/**
	 * ������������ ���������� ���������� � ������.
	 * @param fullRepaint ����������� �� ������ �����������.
	 * <code>true</code> - ���������������� �������������� ������� � ��������
	 * �������������� �����. <code>false</code> - ���������������� ������ 
	 * �������� �������������� �����.
	 */
	public abstract void repaint(boolean fullRepaint)
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ������ ���� �� ���������� ����������� �����.
	 * @param cursor ������
	 */
	public abstract void setCursor(Cursor cursor);

	/**
	 * �������� ������������� ������.
	 * @return ������
	 */
	public abstract Cursor getCursor();

	/**
	 * � ������ ����������� ����� "������" ({@link MapState#MOVE_HAND})
	 * ����������� ���� � ������� ��������.
	 * @param me ������� �������
	 */	
	public abstract void handDragged(MouseEvent me)
		throws MapConnectionException, MapDataException;
	
	/**
	 * � ������ ����������� ����� "������" ({@link MapState#MOVE_HAND})
	 * ����������� ����.
	 * @param me ������� �������
	 */	
	public abstract void handMoved(MouseEvent me)
		throws MapConnectionException, MapDataException;

	/**
	 * � ������ ������ ({@link MapState#NULL_ACTION_MODE})
	 * ����������� ����.
	 * @param me ������� �������
	 */	
	public abstract void mouseMoved(MouseEvent me)
		throws MapConnectionException, MapDataException;

	/**
	 * �������� ������ ���� ����� � ������������� ���������.
	 * @return ������
	 */
	public Image getMapShot() {
		JComponent component = getVisualComponent();
		int width = component.getWidth();
		int height = component.getHeight();
		if (this.lastVisCompSize == null) {
			this.lastVisCompSize = component.getSize();
			this.mapShotImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		}
		else if (!this.lastVisCompSize.equals(component.getSize()))
			this.mapShotImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		
		component.paint(this.mapShotImage.getGraphics());
		
		return this.mapShotImage;
	}

	/**
	 * �������� ������ �������������� �����.
	 * @return ������ ����� &lt;{@link SpatialLayer}&gt;
	 * @deprecated use mapContext
	 */
	public List getLayers()
		throws MapDataException {
		return this.mapContext.getLayers();
	}
	
	/**
	 * ������� ������ ������.
	 * @param viewerClass ����� ������
	 * @return ������ ������
	 */
	public static NetMapViewer create(
			String viewerClass,
			LogicalNetLayer logicalNetLayer,
			MapContext mapContext,
			MapImageRenderer renderer)
				throws MapDataException {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call NetMapViewer.create()");

		try {
			Class clazz = Class.forName(viewerClass);
			Constructor[] constructors = clazz.getDeclaredConstructors();
			for (int i = 0; i < constructors.length; i++) {
				Class[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length == 3 
						&& parameterTypes[0].equals(LogicalNetLayer.class)
						&& parameterTypes[1].equals(MapContext.class)
						&& parameterTypes[2].equals(MapImageRenderer.class)) {
					Constructor constructor = constructors[i];
					constructor.setAccessible(true);
					Object[] initArgs = new Object[2];
					initArgs[0] = logicalNetLayer;
					initArgs[1] = mapContext;
					initArgs[2] = renderer;
					return (NetMapViewer)constructor.newInstance(initArgs);
				}
			}
//			mapViewer = (NetMapViewer )Class.forName(viewerClass).newInstance();
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new MapDataException(
					"NetMapViewer.create() throws ClassNotFoundException");
		} catch(InstantiationException ie) {
			ie.printStackTrace();
			throw new MapDataException(
					"NetMapViewer.create() throws InstantiationException");
		} catch(IllegalAccessException iae) {
			iae.printStackTrace();
			throw new MapDataException(
					"NetMapViewer.create() throws IllegalAccessException");
		} catch(IllegalArgumentException iae) {
			iae.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws IllegalArgumentException");
		} catch(InvocationTargetException ite) {
			ite.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws InvocationTargetException");
		}
		throw new MapDataException("NetMapViewer.create() cannot find constructor with arguments (LogicalNetLayer, MapImageRenderer) for class " + viewerClass);
	}
}
