package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemePort;

public class PortCell extends EllipseCell
{
	private static final long serialVersionUID = 01L;
	private Identifier scheme_port_id;

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

	public void setSchemePortId(Identifier portId)
	{
		scheme_port_id = portId;
	}
}