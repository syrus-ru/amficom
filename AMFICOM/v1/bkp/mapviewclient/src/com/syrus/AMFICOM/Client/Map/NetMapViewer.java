/**
 * $Id: NetMapViewer.java,v 1.4 2005/02/03 16:24:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.awt.Component;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JComponent;

/**
 * ����� NetMapViewer, ������� �����������, ��������� � ���� ����� 
 * ���������������� �� ���������� ������������ ���������������� ����������. 
 * ������, ����������� ������������ ���������� � ��� ��� ���� ������� 
 * (SpatialFX, MapInfo) ��������� ���� ����� � ��������� �������������
 * ��� ������� �������� �����������.
 * ��� ����, ����� �������� ���������, ���������� � ���� ����������� 
 * �����������, ������� ������� ����� GetComponent()
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
	 * ���������� � ���������� ���������������� ����������
	 */
	protected MapConnection mapConnection;

	/**
	 * ���������� ��������� ��� �����������
	 * 
	 */
	public abstract void setMap(String dataBasePath, String dataBaseView);
	
	/**
	 * ��������� ������ � ���������� � ������ 
	 */
	public abstract void closeMap();
	
	/**
	 * ���������� ���������� � ���������� ��������������� ����������
	 */
	public abstract void setConnection(MapConnection conn);
	
	/**
	 * �������� ���������� � ���������� ��������������� ����������
	 */
	public abstract MapConnection getConnection();
	
	/**
	 * �������� ����������� ���������, � ������� ������������ �����������
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
	 * ������������� ������ ����������� �����������. ������� �������� - 
	 * ����������� � ���������� ���������������� ����������. ��� ����������
	 * ������ ������������� �������� �� ����������� ��������������� ����������
	 * ������� �������������� ���� �����
	 * 
	 * @see SpatialNetMapViewer.init, MapInfoNetMapViewer.init
	 */	
	public void init()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "init()");
		
	}
	
	/**
	 * ������������ ���������� ������� ���������� ����������� ����� ��� 
	 * ��������� ������
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
