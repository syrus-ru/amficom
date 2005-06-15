/**
 * $Id: NetMapViewer.java,v 1.16 2005/06/15 07:42:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
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
 * Класс, являясь абстрактным, реализует в себе общую 
 * функциональность по управлению отображением картографической информации. 
 * Классы, управляющие отображением информации в том или ином формате 
 * (SpatialFX, MapInfo) наследуют этот класс и реализуют специфические
 * для формата операции отображения.
 * Для того, чтобы получить компонент, содержащий в себе отображение 
 * картографии, следует вызвать метод {@link #getVisualComponent()}
 * <br> реализация com.syrus.AMFICOM.client.map.objectfx.OfxNetMapViewer 
 * <br> реализация com.syrus.AMFICOM.client.map.mapinfo.MapInfoNetMapViewer
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
	 * Инициализация класса отображения картографии. Базовое действие - 
	 * соединиться с хранилищем картографической информации. Для реализации
	 * других специфических действий по отображению топографической информации
	 * следует переопределить этот метод
	 * <br> реализация com.syrus.AMFICOM.client.map.objectfx.OfxNetMapViewer.init() 
	 * <br> реализация com.syrus.AMFICOM.client.map.mapinfo.MapInfoNetMapViewer.init()
	 */	
	public abstract void init()
		throws MapDataException;

	/**
	 * Осуществляет сохранение текущик параметров отображения карты для 
	 * следующей сессии.
	 */
	public void saveConfig() {
		//empty
	}

	/**
	 * Получить логический слой.
	 * @return 
	 * логический слой
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	/**
	 * Получить графический компонент, в котором отображается картография.
	 * @return компонент
	 */
	public abstract JComponent getVisualComponent();

	/**
	 * Получить видимую область в географических координатах.
	 * @return видимая область
	 */
	public abstract Rectangle2D.Double getVisibleBounds()
		throws MapConnectionException, MapDataException;

	/**
	 * Центрировать географический объект.
	 * @param so географический объект
	 */
	public abstract void centerSpatialObject(SpatialObject so)
		throws MapConnectionException, MapDataException;

	/**
	 * Перерисовать содержимое компонента с картой.
	 * @param fullRepaint производить ли полную перерисовку.
	 * <code>true</code> - перерисовываются географические объекты и элементы
	 * топологической схемы. <code>false</code> - перерисовываются только 
	 * элементы топологической схемы.
	 */
	public abstract void repaint(boolean fullRepaint)
		throws MapConnectionException, MapDataException;

	/**
	 * Устанавить курсор мыши на компоненте отображения карты.
	 * @param cursor курсор
	 */
	public abstract void setCursor(Cursor cursor);

	/**
	 * Получить установленный курсор.
	 * @return курсор
	 */
	public abstract Cursor getCursor();

	/**
	 * В режиме перемещения карты "лапкой" ({@link MapState#MOVE_HAND})
	 * передвинута мышь с нажатой клавишей.
	 * @param me мышиное событие
	 */	
	public abstract void handDragged(MouseEvent me)
		throws MapConnectionException, MapDataException;
	
	/**
	 * В режиме перемещения карты "лапкой" ({@link MapState#MOVE_HAND})
	 * передвинута мышь.
	 * @param me мышиное событие
	 */	
	public abstract void handMoved(MouseEvent me)
		throws MapConnectionException, MapDataException;

	/**
	 * В пустом режиме ({@link MapState#NULL_ACTION_MODE})
	 * передвинута мышь.
	 * @param me мышиное событие
	 */	
	public abstract void mouseMoved(MouseEvent me)
		throws MapConnectionException, MapDataException;

	/**
	 * Получить снимок вида карты с отрисованными объектами.
	 * @return снимок
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
	 * Получить список географических слоев.
	 * @return список слоев &lt;{@link SpatialLayer}&gt;
	 * @deprecated use mapContext
	 */
	public List getLayers()
		throws MapDataException {
		return this.mapContext.getLayers();
	}
	
	/**
	 * Создает объект вьюера.
	 * @param viewerClass класс вьюера
	 * @return объект вьюера
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
