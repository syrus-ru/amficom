/**
 * $Id: NetMapViewer.java,v 1.11 2005/04/19 15:39:44 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;

import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * Класс, являясь абстрактным, реализует в себе общую 
 * функциональность по управлению отображением картографической информации. 
 * Классы, управляющие отображением информации в том или ином формате 
 * (SpatialFX, MapInfo) наследуют этот класс и реализуют специфические
 * для формата операции отображения.
 * Для того, чтобы получить компонент, содержащий в себе отображение 
 * картографии, следует вызвать метод {@link #getVisualComponent()}
 * <br> реализация com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer 
 * <br> реализация com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer
 * @author $Author: peskovsky $
 * @version $Revision: 1.11 $, $Date: 2005/04/19 15:39:44 $
 * @module mapviewclient_v1
 */
public abstract class NetMapViewer 
{
	private Dimension lastVisCompSize = null;
	private Image mapShotImage = null;
	
	/**
	 * Установить соединение с хранилищем топографической информации.
	 * @param conn соежинение
	 */
	public abstract void setConnection(MapConnection conn)
		throws MapDataException;
	
	/**
	 * Получить соединение с хранилищем топографической информации.
	 * @return соединение
	 */
	public abstract MapConnection getConnection();
	
	/**
	 * Получить графический компонент, в котором отображается картография.
	 * @return компонент
	 */
	public abstract JComponent getVisualComponent();

	/**
	 * Получить снимок вида карты с отрисованными объектами.
	 * @return снимок
	 */
	public Image getMapShot()
	{
		JComponent component = getVisualComponent();
		int width = component.getWidth();
		int height = component.getHeight();
		if (this.lastVisCompSize == null)
		{
			this.lastVisCompSize = component.getSize();
			this.mapShotImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		}
		else if (!this.lastVisCompSize.equals(component.getSize()))
			this.mapShotImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		
		component.paint(this.mapShotImage.getGraphics());
		
		return this.mapShotImage;
	}

	/**
	 * Инициализация класса отображения картографии. Базовое действие - 
	 * соединиться с хранилищем картографической информации. Для реализации
	 * других специфических действий по отображению топографической информации
	 * следует переопределить этот метод
	 * <br> реализация com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer.init() 
	 * <br> реализация com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer.init()
	 */	
	public void init()
		throws MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "init()");
	}

	/**
	 * Осуществляет сохранение текущик параметров отображения карты для 
	 * следующей сессии.
	 */
	public void saveConfig()
	{//empty
	}

	/**
	 * Получить логический слой.
	 * @return 
	 * логический слой
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	/**
	 * Получить список географических слоев.
	 * @return список слоев &lt;{@link SpatialLayer}&gt;
	 */
	public abstract List getLayers()
		throws MapDataException;
	
	/**
	 * Создает объект вьюера.
	 * @param viewerClass класс вьюера
	 * @return объект вьюера
	 */
	public static NetMapViewer create(String viewerClass)
		throws MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call NetMapViewer.create()");

		NetMapViewer mapViewer = null;

		try
		{
			mapViewer = (NetMapViewer )Class.forName(viewerClass).newInstance();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws ClassNotFoundException");
		}
		catch(InstantiationException ie)
		{
			ie.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws InstantiationException");
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws IllegalAccessException");
		}
		return mapViewer;
	}
}
