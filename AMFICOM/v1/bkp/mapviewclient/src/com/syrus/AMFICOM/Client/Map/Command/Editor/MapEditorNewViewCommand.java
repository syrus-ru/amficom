/*
 * $Id: MapEditorNewViewCommand.java,v 1.2 2004/10/15 14:09:21 krupenn Exp $
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

import com.syrus.AMFICOM.Client.General.Model.MapConfigureApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * Класс $RCSfile: MapEditorNewViewCommand.java,v $ используется для создания новой топологической схемы в
 * модуле "Редактор топологических схем". При этом в модуле открываются все
 * окна (команда ViewMapAllCommand) и вызывается команда MapNewCommand
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/15 14:09:21 $
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
			new ViewMapAllCommand(mainFrame.getDesktop(), aContext, new MapEditorApplicationModelFactory()).execute();
			mmf = mainFrame.getMapFrame();
		}
		new MapViewNewCommand(mmf, aContext).execute();
//		new MapNewCommand(mmf, aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
