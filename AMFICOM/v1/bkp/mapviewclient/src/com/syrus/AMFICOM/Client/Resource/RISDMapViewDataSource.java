package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.SessionInterface;

public class RISDMapViewDataSource
		extends RISDSchemeDataSource
		implements DataSourceInterface
{
	protected RISDMapViewDataSource()
	{
	}

	public RISDMapViewDataSource(SessionInterface si)
	{
		super(si);
	}
}