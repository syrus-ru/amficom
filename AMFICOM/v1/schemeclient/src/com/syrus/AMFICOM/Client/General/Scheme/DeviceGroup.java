package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;

public class DeviceGroup extends DefaultGraphCell
{
	private static final long serialVersionUID = 02L;
	private Identifier proto_element_id;
	private Identifier scheme_element_id;
	private Identifier scheme_id;

	public DeviceGroup()
	{
		this(null);
	}

	public DeviceGroup(Object userObject)
	{
		super(userObject);
	}

	public SchemeProtoElement getProtoElement()
	{
		try {
			return (SchemeProtoElement)SchemeStorableObjectPool.getStorableObject(proto_element_id, true);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public Identifier getProtoElementId()
	{
		return proto_element_id;
	}

	public void setProtoElementId(Identifier id)
	{
		proto_element_id = id;
	}

	public SchemeElement getSchemeElement()
	{
		try {
			return (SchemeElement)SchemeStorableObjectPool.getStorableObject(scheme_element_id, true);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public Scheme getScheme()
	{
		try {
			return (Scheme)SchemeStorableObjectPool.getStorableObject(scheme_id, true);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public Identifier getSchemeElementId()
	{
		return scheme_element_id;
	}

	public void setSchemeElementId(Identifier id)
	{
		scheme_element_id = id;
	}

	public Identifier getSchemeId()
	{
		return scheme_id;
	}

	public void setSchemeId(Identifier id)
	{
		scheme_id = id;
	}
}
