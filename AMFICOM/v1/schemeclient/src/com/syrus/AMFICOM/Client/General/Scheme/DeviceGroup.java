package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class DeviceGroup extends DefaultGraphCell
{
	private static final long serialVersionUID = 01L;
	String proto_element_id = "";
	String scheme_element_id = "";
	String scheme_id = "";
	public String param = "";

	public DeviceGroup()
	{
		this(null);
	}

	public DeviceGroup(Object userObject)
	{
		super(userObject);
	}

	public ProtoElement getProtoElement()
	{
		ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, proto_element_id);
		return p;
	}

	public String getProtoElementId()
	{
		return proto_element_id;
	}

	public void setProtoElementId(String id)
	{
		proto_element_id = id;
	}

	public SchemeElement getSchemeElement()
	{
		SchemeElement p = (SchemeElement)Pool.get(SchemeElement.typ, scheme_element_id);
		return p;
	}

	public String getSchemeElementId()
	{
		return scheme_element_id;
	}

	public void setSchemeElementId(String id)
	{
		scheme_element_id = id;
	}

	public String getSchemeId()
	{
		return scheme_id;
	}

	public void setSchemeId(String id)
	{
		scheme_id = id;
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
//		Object[] tValues;

//		out.defaultWriteObject();
		// Save the userObject, if its Serializable.
/*	if(userObject != null && userObject instanceof Serializable) {
			tValues = new Object[2];
			tValues[0] = "userObject";
			tValues[1] = userObject;
		}
		else
			tValues = new Object[0];
		out.writeObject(tValues);/

		out.writeObject(proto_element_id);
		out.writeObject(scheme_element_id);
		out.writeObject(scheme_id);
		out.writeObject(param);
	}*/
/*
	private void readObject(ObjectInputStream s)
			throws IOException, ClassNotFoundException
	{
		Object[] tValues;

		s.defaultReadObject();
		tValues = (Object[])s.readObject();

		if(tValues.length > 0 && tValues[0].equals("userObject"))
			userObject = tValues[1];

		proto_element_id = (String)s.readObject();
		scheme_element_id = (String)s.readObject();
		scheme_id = (String)s.readObject();
		param = (String)s.readObject();
	}*/
}
