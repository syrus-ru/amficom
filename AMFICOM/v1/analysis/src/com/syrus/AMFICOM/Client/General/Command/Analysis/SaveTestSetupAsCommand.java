package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class SaveTestSetupAsCommand extends VoidCommand
{
	private ApplicationContext aContext;
	private long type;

	public SaveTestSetupAsCommand(ApplicationContext aContext, long type)
	{
		this.aContext = aContext;
		this.type = type;
	}

	public Object clone()
	{
		return new SaveTestSetupAsCommand(aContext, type);
	}

	public void execute()
	{
		if (SaveTestSetupCommand.checkStrangeConditions() == false)
			return;

		// FIXME: нездоровый код
		if ((type & SaveTestSetupCommand.ETALON) != 0
			&& !Heap.hasEventParamsForPrimaryTrace())
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noAnalysisError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		String newName = CreateTestSetupCommand.getNewMSNameFromDialog();
		if (newName == null)
			return;

		if (SaveTestSetupCommand.createNewMSAndSave(newName, aContext, type))
			Heap.setNewMSName(null); // success
	}
}
