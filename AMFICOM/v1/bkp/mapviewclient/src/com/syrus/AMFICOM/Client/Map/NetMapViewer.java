/**
 * $Id: NetMapViewer.java,v 1.5 2005/02/07 16:09:25 krupenn Exp $
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
 * Класс, являясь абстрактным, реализует в себе общую 
 * функциональность по управлению отображением картографической информации. 
 * Классы, управляющие отображением информации в том или ином формате 
 * (SpatialFX, MapInfo) наследуют этот класс и реализуют специфические
 * для формата операции отображения.
 * Для того, чтобы получить компонент, содержащий в себе отображение 
 * картографии, следует вызвать метод {@link #getComponent()}
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/02/07 16:09:25 $
 * @module mapviewclient_v1
 * @see {@link com.syrus.AMFICOM.Client.Map.ObjectFX.SpatialNetMapViewer} 
 * @see {@link com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer}
 */
public abstract class NetMapViewer 
{
	/**
	 * Соединение с хранилищем картографической информации.
	 */
	protected MapConnection mapConnection;

	/**
	 * Отобразить указанный вид кортографии.
	 * @param dataBasePath путь к базе картографии
	 * @param dataBaseView вид в базе картографии
	 */
	public abstract void setMap(String dataBasePath, String dataBaseView);
	
	/**
	 * Закрывает сессию и соединение с картой.
	 */
	public abstract void closeMap();
	
	/**
	 * Установить соединение с хранилищем топографической информации.
	 * @param conn соежинение
	 */
	public abstract void setConnection(MapConnection conn);
	
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
	 * @see SpatialNetMapViewer.init() 
	 * @see MapInfoNetMapViewer.init()
	 */	
	public void init()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "init()");
	}

	/**
	 * Осуществляет сохранение текущик параметров отображения карты для 
	 * следующей сессии.
	 */
	public void saveConfig()
	{
	}

	/**
	 * Компонент.
	 * @return компонент
	 */
	public abstract JComponent getJComponent();
	
	/**
	 * Компонент.
	 * @return компонент
	 */
	public abstract Component getComponent();
	
	/**
	 * Получить логический слой.
	 * @return 
	 * логический слой
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	/**
	 * Получить список названий доступных видов.
	 * @return Список видов &lt;{@link String}&gt;
	 */
	public abstract List getAvailableViews();
	
	/**
	 * Установить вид.
	 * @param dataBaseView вид
	 */
	public abstract void setView(String dataBaseView);

	/**
	 * Получить список географических слоев.
	 * @return список слоев &lt;{@link SpatialLayer}&gt;
	 */
	public abstract List getLayers();
	
	/**
	 * Создает объект вьюера.
	 * @param viewerClass класс вьюера
	 * @return объект вьюера
	 */
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
