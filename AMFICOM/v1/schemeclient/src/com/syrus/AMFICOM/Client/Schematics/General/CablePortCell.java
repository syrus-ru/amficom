package com.syrus.AMFICOM.Client.Schematics.General;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;

import com.jgraph.graph.DefaultGraphCell;

public class CablePortCell extends DefaultGraphCell
{
	private static final long serialVersionUID = 01L;
	private String scheme_cableport_id;

	public CablePortCell()
	{
		this(null);
	}

	public CablePortCell(Object userObject)
	{
		super(userObject);
	}

	public SchemeCablePort getSchemeCablePort()
	{
		return (SchemeCablePort)Pool.get(SchemeCablePort.typ, scheme_cableport_id);
	}

	public String getSchemeCablePortId()
	{
		return scheme_cableport_id;
	}


	public void setSchemeCablePortId(String id)
	{
		scheme_cableport_id = id;
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_cableport_id);
	}*/
}
