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
	 * ���������� ��������� ��� �����������
	 * 
	 */
	public void setMap(String dataBasePath, String dataBaseView)
	{
	}
	
	/**
	 * ��������� ������ � ���������� � ������ 
	 */
	public void closeMap()
	{
	}
	
	/**
	 * ���������� ���������� � ���������� ��������������� ����������
	 */
	public void setConnection(MapConnection conn)
	{
	}
	
	/**
	 * �������� ���������� � ���������� ��������������� ����������
	 */
	public MapConnection getConnection()
	{
		return null;
	}
	
	/**
	 * �������� ����������� ���������, � ������� ������������ �����������
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