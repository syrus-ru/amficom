package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.General.*;

public class RISDResultDataSource
		extends RISDMapViewDataSource
		implements DataSourceInterface
{
	protected RISDResultDataSource()
	{
		//
	}

	public RISDResultDataSource(SessionInterface si)
	{
		super(si);
	}

	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetResultSetResultIds(si.accessIdentity, result_set_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetResultSetResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;
	}

	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id, String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetResultSetResultMEIds(si.accessIdentity, result_set_id, me_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetResultSetResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;

	}


}

