/*
 * $Id: MapEditorNewMapCommand.java,v 1.6 2004/10/20 10:14:39 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * ����� $RCSfile: MapEditorNewMapCommand.java,v $ ������������ ��� �������� ����� �������������� ����� �
 * ������ "�������� �������������� ����". ��� ���� � ������ ����������� ���
 * ���� (������� ViewMapAllCommand) � ���������� ������� MapNewCommand
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/20 10:14:39 $
 * @module
 * @author $Author: krupenn $
 * @see MapNewCommand, ViewMapAllCommand
 */
public class MapEditorNewMapCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapEditorMainFrame mainFrame;

	public MapEditorNewMapCommand()
	{
	}

	public MapEditorNewMapCommand(MapEditorMainFrame mainFrame, ApplicationContext aContext)
	{
		this.mainFrame = mainFrame;
		this.aContext = aContext;
	}

	public void execute()
	{
		MapFrame mmf = mainFrame.getMapFrame();
	
		if(mmf == null)
		{
			new ViewMapAllCommand(mainFrame.getDesktop(), aContext, new MapMapEditorApplicationModelFactory()).execute();
			mmf = mainFrame.getMapFrame();
		}

		if(!mainFrame.getMapFrame().checkCanCloseMap())
			return;
		if(!mainFrame.getMapFrame().checkCanCloseMapView())
			return;

		new MapNewCommand(mmf, aContext).execute();

		setResult(Command.RESULT_OK);
	}

}
