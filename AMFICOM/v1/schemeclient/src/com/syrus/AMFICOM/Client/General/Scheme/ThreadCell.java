package com.syrus.AMFICOM.Client.General.Scheme;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.jgraph.pad.EllipseCell;

public class ThreadCell extends EllipseCell
{
	private static final long serialVersionUID = 01L;
	private SchemeCableThread thread;

	public ThreadCell()
	{
		this(null);
	}

	public ThreadCell(Object userObject)
	{
		super(userObject);
	}

	public SchemeCableThread getSchemeCableThread()
	{
		return thread;
	}

	public void setSchemeCableThread(SchemeCableThread thread)
	{
		this.thread = thread;
	}
}