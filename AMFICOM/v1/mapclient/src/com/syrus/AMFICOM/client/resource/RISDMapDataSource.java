package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.SessionInterface;

public class RISDMapDataSource
		extends RISDConfigDataSource
		implements DataSourceInterface
{
	protected RISDMapDataSource()
	{
	}

	public RISDMapDataSource(SessionInterface si)
	{
		super(si);
	}

}
