/*
 * $Id: MapEditorNewMapCommand.java,v 1.8 2005/01/21 13:49:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapCloseCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.map.Map;
import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapEditorNewMapCommand.java,v $ используется для создания новой топологической схемы в
 * модуле "Редактор топологических схем". При этом в модуле открываются все
 * окна (команда ViewMapAllCommand) и вызывается команда MapNewCommand
 * 
 * @version $Revision: 1.8 $, $Date: 2005/01/21 13:49:27 $
 * @module
 * @author $Author: krupenn $
 * @see MapNewCommand, ViewMapAllCommand
 */
public class MapEditorNewMapCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

	public MapEditorNewMapCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(desktop);
	
		if(mapFrame == null)
		{
			new ViewMapAllCommand(
					desktop, 
					aContext, 
					new MapMapEditorApplicationModelFactory()).execute();
			mapFrame = MapDesktopCommand.findMapFrame(desktop);
		}

		if(!mapFrame.checkCanCloseMap())
			return;
		if(!mapFrame.checkCanCloseMapView())
			return;

		new MapCloseCommand(mapFrame.getMap()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();
		
		Map map = cmd.getMap();

		MapView mapView = mapFrame.getMapView();

		mapView.setMap(map);

		setResult(Command.RESULT_OK);
	}

}
