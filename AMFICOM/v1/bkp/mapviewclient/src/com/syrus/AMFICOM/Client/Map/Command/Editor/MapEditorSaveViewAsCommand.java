/*
 * $Id: MapEditorSaveViewAsCommand.java,v 1.3 2004/10/19 14:10:03 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewSaveAsCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * ����� $RCSfile: MapEditorSaveViewAsCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����" � ����� ������. ���������� �������
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 14:10:03 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveViewAsCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	public MapEditorSaveViewAsCommand()
	{
	}

	/**
	 * 
	 * @param mapFrame ���� �����, �� �������� ��������� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveViewAsCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}
	
	public void setParameter(String param, Object val)
	{
		if(param.equals("mapFrame"))
			this.mapFrame = (MapFrame)val;
	}

	public void execute()
	{
		if(mapFrame == null)
		{
			System.out.println("map frame is null! Cannot create new map.");
			setResult(Command.RESULT_NO);
			return;
		}
		new MapViewSaveAsCommand(mapFrame, aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
