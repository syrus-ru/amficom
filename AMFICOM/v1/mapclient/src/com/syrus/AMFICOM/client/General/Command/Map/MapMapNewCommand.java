/*
 * $Id: MapMapNewCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapConfigureApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Editor.MapMDIMain;

/**
 * ����� $RCSfile: MapMapNewCommand.java,v $ ������������ ��� �������� ����� �������������� ����� �
 * ������ "�������� �������������� ����". ��� ���� � ������ ����������� ���
 * ���� (������� ViewMapAllCommand) � ���������� ������� MapNewCommand
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module
 * @author $Author: krupenn $
 * @see MapNewCommand, ViewMapAllCommand
 */
public class MapMapNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapMDIMain mapFrame;

	public MapMapNewCommand()
	{
	}

	public MapMapNewCommand(MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapMapNewCommand(mapFrame, aContext);
	}

	public void execute()
	{
		if(mapFrame.mapFrame == null)
			new ViewMapAllCommand(mapFrame.desktopPane, aContext, new MapConfigureApplicationModelFactory()).execute();
		new MapNewCommand(mapFrame.mapFrame, aContext).execute();
	}

}