/*
 * $Id: MapEditorNewViewCommand.java,v 1.7 2004/12/28 17:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewCloseCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.map.Map;

/**
 * ����� $RCSfile: MapEditorNewViewCommand.java,v $ ������������ ��� �������� ����� �������������� ����� �
 * ������ "�������� �������������� ����". ��� ���� � ������ ����������� ���
 * ���� (������� ViewMapAllCommand) � ���������� ������� MapNewCommand
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/28 17:35:12 $
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

	public void execute()
	{
		MapFrame mapFrame = mainFrame.getMapFrame();
	
		if(mapFrame == null)
		{
			new ViewMapAllCommand(mainFrame.getDesktop(), aContext, new MapMapEditorApplicationModelFactory()).execute();
			mapFrame = mainFrame.getMapFrame();
		}

		if(!mapFrame.checkCanCloseMap())
			return;
		if(!mapFrame.checkCanCloseMapView())
			return;

		new MapViewCloseCommand(mapFrame.getMapView()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();
		
		Map map = cmd.getMap();

		MapViewNewCommand cmd2 = new MapViewNewCommand(map, mapFrame.getContext());
		cmd2.execute();

		MapView mapView = cmd2.getMapView();

        mapFrame.setMapView(mapView);

		setResult(Command.RESULT_OK);
	}

}
