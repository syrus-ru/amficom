/**
 * $Id: NetMapViewer.java,v 1.11 2005/04/19 15:39:44 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
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
 * �����, ������� �����������, ��������� � ���� ����� 
 * ���������������� �� ���������� ������������ ���������������� ����������. 
 * ������, ����������� ������������ ���������� � ��� ��� ���� ������� 
 * (SpatialFX, MapInfo) ��������� ���� ����� � ��������� �������������
 * ��� ������� �������� �����������.
 * ��� ����, ����� �������� ���������, ���������� � ���� ����������� 
 * �����������, ������� ������� ����� {@link #getVisualComponent()}
 * <br> ���������� com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer 
 * <br> ���������� com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer
 * @author $Author: peskovsky $
 * @version $Revision: 1.11 $, $Date: 2005/04/19 15:39:44 $
 * @module mapviewclient_v1
 */
public abstract class NetMapViewer 
{
	private Dimension lastVisCompSize = null;
	private Image mapShotImage = null;
	
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
	 * ������������� ������ ����������� �����������. ������� �������� - 
	 * ����������� � ���������� ���������������� ����������. ��� ����������
	 * ������ ������������� �������� �� ����������� ��������������� ����������
	 * ������� �������������� ���� �����
	 * <br> ���������� com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer.init() 
	 * <br> ���������� com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer.init()
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
	 * �������� ���������� ����.
	 * @return 
	 * ���������� ����
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

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
