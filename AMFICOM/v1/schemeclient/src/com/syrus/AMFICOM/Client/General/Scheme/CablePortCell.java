package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort;

public class CablePortCell extends DefaultGraphCell
{
	private static final long serialVersionUID = 01L;
	private Identifier scheme_cableport_id;

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
		try {
			return (SchemeCablePort)SchemeStorableObjectPool.getStorableObject(scheme_cableport_id, true);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public Identifier getSchemeCablePortId()
	{
		return scheme_cableport_id;
	}

	public void setSchemeCablePortId(Identifier id)
	{
		scheme_cableport_id = id;
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_cableport_id);
	}*/
}
