/*
 * $Id: MapEditorNewViewCommand.java,v 1.3 2004/10/18 07:39:19 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * ����� $RCSfile: MapEditorNewViewCommand.java,v $ ������������ ��� �������� ����� �������������� ����� �
 * ������ "�������� �������������� ����". ��� ���� � ������ ����������� ���
 * ���� (������� ViewMapAllCommand) � ���������� ������� MapNewCommand
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 07:39:19 $
 * @module
 * @author $Author: krupenn $
 * @see MapNewCommand, ViewMapAllCommand
 */
public class MapEditorNewViewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapEditorMainFrame mainFrame;

	public MapEditorNewViewCommand()
	{
	}

	public MapEditorNewViewCommand(MapEditorMainFrame mainFrame, ApplicationContext aContext)
	{
		this.mainFrame = mainFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapEditorNewViewCommand(mainFrame, aContext);
	}

	public void execute()
	{
		MapFrame mmf = mainFrame.getMapFrame();
	
		if(mmf == null)
		{
			new ViewMapAllCommand(mainFrame.getDesktop(), aContext, new MapMapEditorApplicationModelFactory()).execute();
			mmf = mainFrame.getMapFrame();
		}
		new MapViewNewCommand(mmf, aContext).execute();
//		new MapNewCommand(mmf, aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
