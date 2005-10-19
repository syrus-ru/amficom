package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;

// FIXME: unused
public class OptionsSetColorsCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public OptionsSetColorsCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new OptionsSetColorsCommand(this.aContext);
	}

	@Override
	public void execute()
	{

	}
}