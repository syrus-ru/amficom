/*
 * $Id: MapExportCommand.java,v 1.2 2004/10/04 16:04:43 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

import com.syrus.AMFICOM.Client.Map.Command.Map.ExportCommand;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.io.File;
import java.util.Iterator;

/**
 * ����� $RCSfile: MapExportCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/04 16:04:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapExportCommand extends ExportCommand
{
	/**
	 * ���� �����
	 */
	MapFrame mapFrame;

	public MapExportCommand(MapFrame mapFrame)
	{
		super(null);
		this.mapFrame = mapFrame;
	}

	public void execute()
	{
		if(mapFrame == null)
			mapFrame = MapFrame.getMapMainFrame();
		if(mapFrame == null)
			return;
        System.out.println("Closing map");

		Map map = mapFrame.getMap();
		String[][] exportColumns;
		
		String fileName = super.openFileForWriting(MapPropertiesManager.getLastDirectory());
		if(fileName == null)
			return;
		MapPropertiesManager.setLastDirectory(new File(fileName).getParent());
		super.open(fileName);
		
		super.startObject(Map.typ);
		exportColumns = map.getExportColumns();
		for (int i = 0; i < exportColumns.length; i++) 
		{
			super.put(exportColumns[i][0], exportColumns[i][1]);
		}
		super.endObject();

		for(Iterator it = map.getAllElements().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			super.startObject(((ObjectResource )me).getTyp());
			exportColumns = me.getExportColumns();
			for (int i = 0; i < exportColumns.length; i++) 
			{
				super.put(exportColumns[i][0], exportColumns[i][1]);
			}
			super.endObject();
		}
		
		super.close();

//        mapFrame.setTitle(LangModelMap.getString("Map"));
		setResult(Command.RESULT_OK);
	}

}
