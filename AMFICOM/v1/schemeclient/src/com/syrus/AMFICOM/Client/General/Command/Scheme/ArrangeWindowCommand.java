package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.UI.WindowArranger;

public class ArrangeWindowCommand extends VoidCommand
{
	WindowArranger arranger;
	public ArrangeWindowCommand(WindowArranger arranger)
	{
		this.arranger = arranger;
	}

	public Object clone()
	{
		return new ArrangeWindowCommand(arranger);
	}

	public void execute()
	{
		arranger.arrange();
	}
}

