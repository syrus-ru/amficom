/*
 * $Id: MapEditorSaveMapCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
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

	public Object clone()
	{
		return new MapEditorSaveMapCommand(mapFrame, aContext);
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
