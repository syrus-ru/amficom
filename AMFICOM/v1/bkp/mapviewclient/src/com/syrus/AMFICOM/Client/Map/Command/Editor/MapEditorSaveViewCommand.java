/*
 * $Id: MapEditorSaveViewCommand.java,v 1.6 2005/02/08 15:11:10 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewSaveCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapEditorSaveViewCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем". Использует команду MapSaveCommand
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 * @see MapViewSaveCommand
 */
public class MapEditorSaveViewCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	/**
	 * 
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveViewCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(mapFrame == null)
		{
			System.out.println("map frame is null! Cannot create new map.");
			setResult(Command.RESULT_NO);
			return;
		}
		new MapViewSaveCommand(mapFrame.getMapView(), this.aContext).execute();

		setResult(Command.RESULT_OK);
	}

}
