package com.syrus.AMFICOM.Client.General.Scheme;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;

import com.jgraph.pad.EllipseCell;

public class PortCell extends EllipseCell
{
	private static final long serialVersionUID = 01L;
	private String scheme_port_id;

	public PortCell()
	{
		this(null);
	}

	public PortCell(Object userObject)
	{
		super(userObject);
	}

	public SchemePort getSchemePort()
	{
		return (SchemePort)Pool.get(SchemePort.typ, scheme_port_id);
	}

	public String getSchemePortId()
	{
		return scheme_port_id;
	}

	public void setSchemePortId(String id)
	{
		scheme_port_id = id;
	}
}