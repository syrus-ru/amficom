/**
 * $Id: NetMapViewer.java,v 1.7 2005/02/18 12:19:44 krupenn Exp $
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
 * �����, ������� �����������, ��������� � ���� ����� 
 * ���������������� �� ���������� ������������ ���������������� ����������. 
 * ������, ����������� ������������ ���������� � ��� ��� ���� ������� 
 * (SpatialFX, MapInfo) ��������� ���� ����� � ��������� �������������
 * ��� ������� �������� �����������.
 * ��� ����, ����� �������� ���������, ���������� � ���� ����������� 
 * �����������, ������� ������� ����� {@link #getComponent()}
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/18 12:19:44 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer 
 * @see com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer
 */
public abstract class NetMapViewer 
{
	/**
	 * ���������� � ���������� ���������������� ����������.
	 */
	protected MapConnection mapConnection;

	/**
	 * ���������� ��������� ��� �����������.
	 * @param dataBasePath ���� � ���� �����������
	 * @param dataBaseView ��� � ���� �����������
	 * @deprecated all func should be contained in {@link #setConnection(MapConnection)}
	 */
//	public abstract void setMap(String dataBasePath, String dataBaseView);
	
	/**
	 * ��������� ������ � ���������� � ������.
	 * @deprecated all func should be contained in {@link #setConnection(MapConnection)}
	 */
//	public abstract void closeMap();
	
	/**
	 * ���������� ���������� � ���������� ��������������� ����������.
	 * @param conn ����������
	 */
	public abstract void setConnection(MapConnection conn)
		throws MapDataException;
	
	/**
	 * �������� ���������� � ���������� ��������������� ����������.
	 * @return ����������
	 */
	public abstract MapConnection getConnection();
	
	/**
	 * �������� ����������� ���������, � ������� ������������ �����������.
	 * @return ���������
	 */
	public abstract JComponent getVisualComponent();

	/**
	 * �������� ������ ���� ����� � ������������� ���������.
	 * @return ������
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
	 * ������������� ������ ����������� �����������. ������� �������� - 
	 * ����������� � ���������� ���������������� ����������. ��� ����������
	 * ������ ������������� �������� �� ����������� ��������������� ����������
	 * ������� �������������� ���� �����
	 * 
	 * @see com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer#init() 
	 * @see com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer#init()
	 */	
	public void init()
		throws MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "init()");
	}

	/**
	 * ������������ ���������� ������� ���������� ����������� ����� ��� 
	 * ��������� ������.
	 */
	public void saveConfig()
	{//empty
	}

	/**
	 * ���������.
	 * @return ���������
	 */
	public abstract JComponent getJComponent();
	
	/**
	 * ���������.
	 * @return ���������
	 */
	public abstract Component getComponent();
	
	/**
	 * �������� ���������� ����.
	 * @return 
	 * ���������� ����
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	/**
	 * �������� ������ �������� ��������� �����.
	 * @return ������ ����� &lt;{@link String}&gt;
	 * @deprecated use {@link MapConnection#getAvailableViews()}
	 */
//	public abstract List getAvailableViews()
//		throws MapDataException;
	
	/**
	 * ���������� ���.
	 * @param dataBaseView ���
	 * @deprecated all func should be contained in {@link #setConnection(MapConnection)}
	 */
//	public abstract void setView(String dataBaseView);

	/**
	 * �������� ������ �������������� �����.
	 * @return ������ ����� &lt;{@link SpatialLayer}&gt;
	 */
	public abstract List getLayers()
		throws MapDataException;
	
	/**
	 * ������� ������ ������.
	 * @param viewerClass ����� ������
	 * @return ������ ������
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
