package com.syrus.AMFICOM.Client.Schematics.General;

import com.jgraph.graph.DefaultEdge;

public class PortEdge extends DefaultEdge
{
	private static final long serialVersionUID = 01L;
	public PortEdge(Object userObject)
	{
		super(userObject);
	}

	public PortEdge(Object userObject, boolean allowCholdren)
	{
		super(userObject, allowCholdren);
	}
}
