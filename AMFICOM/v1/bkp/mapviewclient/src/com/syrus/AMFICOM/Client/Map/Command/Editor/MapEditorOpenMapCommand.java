/*
 * $Id: MapEditorOpenMapCommand.java,v 1.11 2005/02/18 12:19:45 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ����� $RCSfile: MapEditorOpenMapCommand.java,v $ ������������ ��� �������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapOpenCommand, � ���� 
 * ������������ ������ MapContext, ����������� ���� ����� � ������������� ����
 * � MapContext ���������� � ���� �����
 * 
 * @version $Revision: 1.11 $, $Date: 2005/02/18 12:19:45 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenMapCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

	MapFrame mapFrame = null;
	MapPropertyFrame propFrame = null;
	MapElementsFrame elementsFrame = null;
	
	Map map = null;
	MapView mapView = null;

	/**
	 * 
	 * @param desktop ���� ������ ���� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorOpenMapCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null)
		{
			if(!this.mapFrame.checkCanCloseMap())
				return;
			if(!this.mapFrame.checkCanCloseMapView())
				return;
		}

		MapOpenCommand mapOpenCommand = new MapOpenCommand(this.desktop, this.aContext);
		// � ������ �������������� �������������� ���� � ������������ ����
		// ����������� ������� MapContext � ���� ���������� �������
		mapOpenCommand.setCanDelete(true);
		mapOpenCommand.execute();
		
		if (mapOpenCommand.getResult() == Command.RESULT_OK)
		{
			this.map = mapOpenCommand.getMap();

			if(this.mapFrame == null)
			{
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
					this.aContext.getDispatcher(), 
					this.desktop, 
					this.aContext, 
					new MapMapEditorApplicationModelFactory());

				mapCommand.execute();
				this.mapFrame = mapCommand.mapFrame;
			}

			if(this.mapFrame == null)
				return;

			MapViewNewCommand cmd = new MapViewNewCommand(this.map, this.aContext);
			cmd.execute();

			this.mapView = cmd.getMapView();

			try {
				this.mapFrame.setMapView(this.mapView);
				ViewMapPropertiesCommand propCommand = new ViewMapPropertiesCommand(this.desktop, this.aContext);
				propCommand.execute();
				this.propFrame = propCommand.frame;
				ViewMapElementsCommand elementsCommand = new ViewMapElementsCommand(this.desktop, this.aContext);
				elementsCommand.execute();
				this.elementsFrame = elementsCommand.frame;
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public MapFrame getMapFrame()
	{
		return this.mapFrame;
	}

	public MapPropertyFrame getPropertiesFrame()
	{
		return this.propFrame;
	}

	public MapElementsFrame getElementsFrame()
	{
		return this.elementsFrame;
	}

}
