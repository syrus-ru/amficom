package com.syrus.AMFICOM.Client.Schematics.General;

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
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_port_id);
	}*/
/*
	public void setUserObject (Object object)
	{
		super.setUserObject(object);
		if (scheme_port_id != null)
			getSchemePort().name = (String)object;
	}

	public Object getUserObject()
	{
		return super.getUserObject();
	}

	public Object cloneUserObject()
	{
		return super.cloneUserObject();
	}

	public void setAttributes(Map map)
	{
		super.setAttributes(map);
	}

	public Map changeAttributes(Map map)
	{
		return super.changeAttributes(map);
	}

	public Map getAttributes()
	{
		return super.getAttributes();
	}
*/
}
/*package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.graph.*;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class PortCell extends EllipseCell
{
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
}*/