/*
 * $Id: MapEditorSaveViewAsCommand.java,v 1.9 2005/02/18 12:19:45 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewSaveAsCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import java.util.Iterator;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 * ����� $RCSfile: MapEditorSaveViewAsCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����" � ����� ������. ���������� �������
 * MapSaveAsCommand
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/18 12:19:45 $
 * @module mapviewclient_v1
 * @see MapViewSaveAsCommand
 */
public class MapEditorSaveViewAsCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	/**
	 * 
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveViewAsCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}
	
	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
		{
			System.out.println("MapView SaveAs: map frame is null! Cannot complete operation.");
			setResult(Command.RESULT_NO);
			return;
		}
		MapViewSaveAsCommand saveAsCommand = new MapViewSaveAsCommand(mapFrame.getMapView(), this.aContext);

		saveAsCommand.execute();
		
		if(saveAsCommand.getResult() == RESULT_OK)
		{
			MapView newMapView = saveAsCommand.getNewMapView();
			MapView oldMapView = mapFrame.getMapView();
			
			MapViewController controller = mapFrame.getMapViewer()
				.getLogicalNetLayer().getMapViewController();

			if (mapFrame != null)
			{
				try {
					controller.setMapView(newMapView);
				} catch(Exception e) {
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							"Map View saved, but failed to open it in Map Frame",
							"Map View SaveAs Command",
							JOptionPane.WARNING_MESSAGE);
					setResult(Command.RESULT_OK);
					e.printStackTrace();
					return;
				}
			
				controller.setMap(oldMapView.getMap());
				
				for(Iterator it = oldMapView.getSchemes().iterator(); it.hasNext();)
				{
					controller.addScheme((Scheme )it.next());
				}
		
				mapFrame.setTitle( 
					LangModelMap.getString("MapView") + " - " + newMapView.getName());
			}
			setResult(Command.RESULT_OK);
		}

	}

}
