/**
 * $Id: NetMapViewer.java,v 1.4 2005/02/03 16:24:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.awt.Component;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JComponent;

/**
 * Класс NetMapViewer, являясь абстрактным, реализует в себе общую 
 * функциональность по управлению отображением картографической информации. 
 * Классы, управляющие отображением информации в том или ином формате 
 * (SpatialFX, MapInfo) наследуют этот класс и реализуют специфические
 * для формата операции отображения.
 * Для того, чтобы получить компонент, содержащий в себе отображение 
 * картографии, следует вызвать метод GetComponent()
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/02/03 16:24:59 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see {@link com.syrus.AMFICOM.Client.Map.ObjectFX.SpatialNetMapViewer} 
 * @see {@link com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer}
 */
public abstract class NetMapViewer 
{
	/**
	 * Соединение с хранилищем картографической информации
	 */
	protected MapConnection mapConnection;

	/**
	 * отобразить указанный вид кортографии
	 * 
	 */
	public abstract void setMap(String dataBasePath, String dataBaseView);
	
	/**
	 * Закрывает сессию и соединение с картой 
	 */
	public abstract void closeMap();
	
	/**
	 * Установить соединение с хранилищем топографической информации
	 */
	public abstract void setConnection(MapConnection conn);
	
	/**
	 * Получить соединение с хранилищем топографической информации
	 */
	public abstract MapConnection getConnection();
	
	/**
	 * Получить графический компонент, в котором отображается картография
	 */
	public abstract JComponent getVisualComponent();

	public Image getMapShot()
	{
		JComponent component = getVisualComponent();
		int width = component.getWidth();
		int height = component.getHeight();
		BufferedImage bim = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		component.paint(bim.getGraphics());
		
		return bim;
	}

	/**
	 * Инициализация класса отображения картографии. Базовое действие - 
	 * соединиться с хранилищем картографической информации. Для реализации
	 * других специфических действий по отображению топографической информации
	 * следует переопределить этот метод
	 * 
	 * @see SpatialNetMapViewer.init, MapInfoNetMapViewer.init
	 */	
	public void init()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "init()");
		
	}
	
	/**
	 * осуществляет сохранение текущик параметров отображения карты для 
	 * следующей сессии
	 * 
	 */
	public void saveConfig()
	{
	}

	public abstract JComponent getJComponent();
	
	public abstract Component getComponent();
	
	/**
	 * 
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	public abstract List getAvailableViews();
	
	public abstract void setView(String dataBaseView);

	public abstract List getLayers();	
	
	public static NetMapViewer create(String viewerClass)
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
		}
		catch(InstantiationException ie)
		{
			ie.printStackTrace();
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
		}
		return mapViewer;
	}
}
