package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.WeightsDialog;

public class NetStudyCommand extends VoidCommand
{
	public NetStudyCommand()
	{
	}

	public Object clone()
	{
		return new NetStudyCommand();
	}

	public void execute()
	{
		WeightsDialog dialog = new WeightsDialog();
		dialog.setVisible(true);
	}
}

