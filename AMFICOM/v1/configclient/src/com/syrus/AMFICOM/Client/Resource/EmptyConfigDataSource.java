package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.SessionInterface;
//import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Resource.Object.*;

public class EmptyConfigDataSource extends EmptyMapDataSource 
{
	protected EmptyConfigDataSource()
	{
		super();
	}

	public EmptyConfigDataSource(SessionInterface si)
	{
		super(si);
	}


	public void LoadKISDescriptors()
	{
	}

	public void LoadNet()
	{
	}

	public void LoadISM()
	{
	}

	public void SaveNet()
	{
	}

	public void SaveISM()
	{
	}

	public void LoadNetDirectory()
	{
	}

	public void LoadISMDirectory()
	{
	}

}