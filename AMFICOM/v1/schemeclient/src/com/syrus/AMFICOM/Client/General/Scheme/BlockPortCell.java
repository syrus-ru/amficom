package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;

public class BlockPortCell extends DefaultGraphCell
{
	private static final long serialVersionUID = 01L;
	private Identifier scheme_port_id;
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
		try {
			return (SchemeCablePort)SchemeStorableObjectPool.getStorableObject(scheme_port_id, true);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public SchemePort getSchemePort()
	{
		try {
			return (SchemePort)SchemeStorableObjectPool.getStorableObject(scheme_port_id, true);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public Identifier getSchemePortId()
	{
		return scheme_port_id;
	}

	public Identifier getSchemeCablePortId()
	{
		return scheme_port_id;
	}

	public void setSchemePortId(Identifier portId)
	{
		scheme_port_id = portId;
	}

	public void setSchemeCablePortId(Identifier portId)
	{
		scheme_port_id = portId;
	}
}
