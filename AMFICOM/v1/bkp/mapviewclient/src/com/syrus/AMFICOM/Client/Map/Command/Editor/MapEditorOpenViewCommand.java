/*
 * $Id: MapEditorOpenViewCommand.java,v 1.6 2004/10/19 14:10:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import javax.swing.JDesktopPane;

/**
 * ����� $RCSfile: MapEditorOpenViewCommand.java,v $ ������������ ��� �������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapOpenCommand, � ���� 
 * ������������ ������ MapContext, ����������� ���� ����� � ������������� ����
 * � MapContext ���������� � ���� �����
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/19 14:10:03 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenViewCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	
	MapFrame mapFrame = null;
	MapPropertyFrame propFrame = null;
	MapElementsFrame elementsFrame = null;

	public MapEditorOpenViewCommand()
	{
	}

	/**
	 * 
	 * @param desktop ���� ������ ���� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorOpenViewCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		ApplicationModelFactory factory = new MapMapEditorApplicationModelFactory();

		MapViewOpenCommand moc = new MapViewOpenCommand(desktop, null, aContext);
		// � ������ �������������� �������������� ���� � ������������ ����
		// ����������� ������� MapContext � ���� ���������� �������
		moc.setCanDelete(true);
		moc.execute();
		if (moc.getResult() == Command.RESULT_OK)
		{
			ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(aContext.getDispatcher(), desktop, aContext, factory);
			mapCommand.execute();
			this.mapFrame = mapCommand.frame;

			if(mapFrame == null)
				return;

			ViewMapPropertiesCommand propCommand = new ViewMapPropertiesCommand(desktop, aContext);
			propCommand.execute();
			this.propFrame = propCommand.frame;

			ViewMapElementsCommand elementsCommand = new ViewMapElementsCommand(desktop, aContext);
			elementsCommand.execute();
			this.elementsFrame = elementsCommand.frame;

			mapFrame.setMapView((MapView)moc.getReturnObject());
		}
	}

	public MapFrame getMapFrame()
	{
		return mapFrame;
	}

	public MapPropertyFrame getPropertiesFrame()
	{
		return propFrame;
	}

	public MapElementsFrame getElementsFrame()
	{
		return elementsFrame;
	}

}
