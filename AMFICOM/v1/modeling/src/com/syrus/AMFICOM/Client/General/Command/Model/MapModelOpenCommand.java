package com.syrus.AMFICOM.Client.General.Command.Model;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Model.MapModelingApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Model.ModelMDIMain;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import javax.swing.JDesktopPane;

public class MapModelOpenCommand extends MapEditorOpenViewCommand
{
	Checker checker;

	public MapModelOpenCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super(desktop, aContext);
	}

	public void execute()
	{
		this.checker = new Checker(this.aContext.getSessionInterface());

		if(!checker.checkCommand(checker.openMapForModeling))
			return;
		
		super.setCanDelete(false);
		super.setCheckSave(false);
		super.execute();

		if(super.getResult() == Command.RESULT_OK)
			super.mapFrame.setLocation(500, 0);

	}
}
