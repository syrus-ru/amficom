package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;

import java.awt.Component;

import java.util.List;

import javax.swing.JComponent;

public class MapInfoNetMapViewer extends NetMapViewer
{
	public MapInfoNetMapViewer()
	{
		super();
	}

	/**
	 * отобразить указанный вид кортографии
	 * 
	 */
	public void setMap(String dataBasePath, String dataBaseView)
	{
	}
	
	/**
	 * Закрывает сессию и соединение с картой 
	 */
	public void closeMap()
	{
	}
	
	/**
	 * Установить соединение с хранилищем топографической информации
	 */
	public void setConnection(MapConnection conn)
	{
	}
	
	/**
	 * Получить соединение с хранилищем топографической информации
	 */
	public MapConnection getConnection()
	{
		return null;
	}
	
	/**
	 * Получить графический компонент, в котором отображается картография
	 */
	public JComponent getVisualComponent()
	{
		return null;
	}

	/**
	 * 
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return null;
	}

	public JComponent getJComponent()
	{
		return null;
	}
	
	public Component getComponent()
	{
		return null;
	}
	
	public List getAvailableViews()
	{
		return null;
	}
	
	public void setView(String dataBaseView)
	{
	}

	public List getLayers()
	{
		return null;
	}
	
}