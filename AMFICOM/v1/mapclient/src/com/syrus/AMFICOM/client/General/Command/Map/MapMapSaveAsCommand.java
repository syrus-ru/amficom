/*
 * $Id: MapMapSaveAsCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Editor.MapMDIMain;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;

import javax.swing.JDesktopPane;

/**
 * ����� $RCSfile: MapMapSaveAsCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����" � ����� ������. ���������� �������
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapMapSaveAsCommand extends VoidCommand
{
	MapMainFrame mapFrame;
	ApplicationContext aContext;

	public MapMapSaveAsCommand()
	{
	}

	/**
	 * 
	 * @param myMapFrame ���� �����, �� �������� ��������� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapMapSaveAsCommand(MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}
	
	/**
	 * @deprecated parameter desktop, parameter myMapFrame
	 */
	public MapMapSaveAsCommand(JDesktopPane desktop, MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this(myMapFrame.mapFrame, aContext);
	}

	public void setParameter(String param, Object val)
	{
		if(param.equals("mapFrame"))
			this.mapFrame = (MapMainFrame )val;
	}

	public Object clone()
	{
		return new MapMapSaveAsCommand(mapFrame, aContext);
	}

	public void execute()
	{
		if(mapFrame == null)
		{
			System.out.println("map frame is null! Cannot create new map.");
			return;
		}
		new MapSaveAsCommand(mapFrame, aContext).execute();
	}

}