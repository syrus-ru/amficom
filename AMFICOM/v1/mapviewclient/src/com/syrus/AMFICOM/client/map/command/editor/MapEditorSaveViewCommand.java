/*
 * $Id: MapEditorSaveViewCommand.java,v 1.4 2004/12/28 17:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewSaveCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * ����� $RCSfile: MapEditorSaveViewCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapSaveCommand
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/28 17:35:12 $
 * @module
 * @author $Author: krupenn $
 * @see MapSaveCommand
 */
public class MapEditorSaveViewCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	/**
	 * 
	 * @param mapFrame ���� �����, �� �������� ��������� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveViewCommand(MapFrame mapFrame, ApplicationContext aContext)
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
		new MapViewSaveCommand(mapFrame.getMapView(), aContext).execute();

		setResult(Command.RESULT_OK);
	}

}
