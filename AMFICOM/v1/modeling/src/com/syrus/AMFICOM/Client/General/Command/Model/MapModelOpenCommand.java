package com.syrus.AMFICOM.Client.General.Command.Model;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorOpenViewCommand;

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
