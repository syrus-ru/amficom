/*
 * $Id: MapEditorSaveMapCommand.java,v 1.3 2004/10/19 14:10:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapSaveCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * ����� MapEditorSaveContextCommand ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapSaveCommand
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 14:10:03 $
 * @module
 * @author $Author: krupenn $
 * @see MapSaveCommand
 */
public class MapEditorSaveMapCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	public MapEditorSaveMapCommand()
	{
	}

	/**
	 * 
	 * @param mapFrame ���� �����, �� �������� ��������� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveMapCommand(MapFrame mapFrame, ApplicationContext aContext)
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
		new MapSaveCommand(mapFrame, aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
