/*
 * $Id: MapEditorOpenViewCommand.java,v 1.5 2004/10/19 10:41:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import javax.swing.JDesktopPane;

/**
 * ����� $RCSfile: MapEditorOpenViewCommand.java,v $ ������������ ��� �������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapOpenCommand, � ���� 
 * ������������ ������ MapContext, ����������� ���� ����� � ������������� ����
 * � MapContext ���������� � ���� �����
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/19 10:41:03 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenViewCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

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

	public Object clone()
	{
		return new MapEditorOpenViewCommand(desktop, aContext);
	}

	public void execute()
	{
		ApplicationModelFactory factory = new MapMapEditorApplicationModelFactory();
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDispatcher(aContext.getDispatcher());

		MapViewOpenCommand moc = new MapViewOpenCommand(desktop, null, aC);
		// � ������ �������������� �������������� ���� � ������������ ����
		// ����������� ������� MapContext � ���� ���������� �������
		moc.setCanDelete(true);
		moc.execute();
		if (moc.getResult() == Command.RESULT_OK)
		{
			ViewMapWindowCommand com2 = new ViewMapWindowCommand(aContext.getDispatcher(), desktop, aContext, factory);
			com2.execute();
			if(com2.frame == null)
				return;

			new ViewMapPropertiesCommand(desktop, aContext).execute();
			new ViewMapElementsCommand(desktop, aContext).execute();
//			new ViewMapSchemeNavigatorCommand(desktop, aContext).execute();

			com2.frame.setMapView((MapView)moc.getReturnObject());
		}
	}

}
