package com.syrus.AMFICOM.Client.General.Scheme;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;

import com.jgraph.graph.DefaultGraphCell;

public class BlockPortCell extends DefaultGraphCell
{
	private static final long serialVersionUID = 01L;
	private String scheme_port_id;
	private boolean is_cable_port = false;

	public BlockPortCell()
	{
		this(null, false);
	}

	public BlockPortCell(Object userObject, boolean is_cable_port)
	{
		super(userObject);
		this.is_cable_port = is_cable_port;
	}

	public boolean isCablePort()
	{
		return is_cable_port;
	}

	public SchemeCablePort getSchemeCablePort()
	{
		return(SchemeCablePort)Pool.get(SchemeCablePort.typ, scheme_port_id);
	}

	public SchemePort getSchemePort()
	{
		return (SchemePort)Pool.get(SchemePort.typ, scheme_port_id);
	}

	public String getSchemePortId()
	{
		return scheme_port_id;
	}

	public String getSchemeCablePortId()
	{
		return scheme_port_id;
	}

	public void setSchemePortId(String id)
	{
		scheme_port_id = id;
	}

	public void setSchemeCablePortId(String id)
	{
		scheme_port_id = id;
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_port_id);
		out.writeBoolean(is_cable_port);
	}*/
}