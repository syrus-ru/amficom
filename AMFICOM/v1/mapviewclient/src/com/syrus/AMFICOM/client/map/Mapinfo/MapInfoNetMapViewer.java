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
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Закрывает сессию и соединение с картой 
	 */
	public void closeMap()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Установить соединение с хранилищем топографической информации
	 */
	public void setConnection(MapConnection conn)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Получить соединение с хранилищем топографической информации
	 */
	public MapConnection getConnection()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Получить графический компонент, в котором отображается картография
	 */
	public JComponent getVisualComponent()
	{
		throw new UnsupportedOperationException();
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		throw new UnsupportedOperationException();
	}

	public JComponent getJComponent()
	{
		throw new UnsupportedOperationException();
	}
	
	public Component getComponent()
	{
		throw new UnsupportedOperationException();
	}
	
	public List getAvailableViews()
	{
		throw new UnsupportedOperationException();
	}
	
	public void setView(String dataBaseView)
	{
		throw new UnsupportedOperationException();
	}

	public List getLayers()
	{
		throw new UnsupportedOperationException();
	}
	
}