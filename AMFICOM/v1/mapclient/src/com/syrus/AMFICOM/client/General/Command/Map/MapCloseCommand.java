/*
 * $Id: MapCloseCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;

import java.util.Vector;

/**
 * ����� $RCSfile: MapCloseCommand.java,v $ ������������ ��� �������� ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapCloseCommand extends VoidCommand
{
	/**
	 * ���� �����
	 */
	MapMainFrame mapFrame;

	public MapCloseCommand()
	{
	}

	public MapCloseCommand(MapMainFrame myMapFrame)
	{
		this.mapFrame = myMapFrame;
	}

	public Object clone()
	{
		return new MapCloseCommand(mapFrame);
	}

	public void execute()
	{
		if(mapFrame == null)
			return;
        System.out.println("Closing new map context");
        mapFrame.setMapContext(null);

        MapMainFrame.iniFile.setValue( "last_long", String.valueOf( mapFrame.myMapViewer.getCenter()[0])  );
        MapMainFrame.iniFile.setValue( "last_lat", String.valueOf( mapFrame.myMapViewer.getCenter()[1]) );

        if ( MapMainFrame.iniFile.saveKeys() )
        {
			System.out.println("Params saved");
        }
        mapFrame.setTitle( LangModelMap.String("AppTitle"));

         // mapFrame.setTitle( mapFrame.getTitle() + " - " + mapFrame.mapPanel.myMapViewer.lnlgetMapContext().name);
	}

}