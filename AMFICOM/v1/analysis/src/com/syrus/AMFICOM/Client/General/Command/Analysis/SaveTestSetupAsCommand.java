package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class SaveTestSetupAsCommand extends AbstractCommand
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

		String newName = CreateTestSetupCommand.getNewMSNameFromDialog();
		if (newName == null)
			return;

		if (SaveTestSetupCommand.createNewMSAndSave(newName, aContext, type))
			Heap.setNewMSName(null); // success
	}
}
