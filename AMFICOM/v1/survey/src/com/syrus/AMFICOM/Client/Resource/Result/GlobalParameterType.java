package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;
import java.io.*;

public class GlobalParameterType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "globalparametertype";

	public String id = "";
	public String name = "";
	public String codename = "";
	public String description = "";
	public String norm = "";
	public String value_type = "";
	public String unit = "";
	public String granularity = "";
	public String rangehi = "";
	public String rangelo = "";
	public String formula = "";

	public long modified = 0;

	GlobalParameterType_Transferable transferable;

	public GlobalParameterType()
	{
	}

	public GlobalParameterType(GlobalParameterType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void updateLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
		transferable.description = description;
		transferable.norm = norm;
		transferable.value_type = value_type;
		transferable.unit = unit;
		transferable.granularity = granularity;
		transferable.rangehi = rangehi;
		transferable.rangelo = rangelo;
		transferable.formula = formula;

		transferable.modified = modified;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		description = transferable.description;
		norm = transferable.norm;
		value_type = transferable.value_type;
		unit = transferable.unit;
		granularity = transferable.granularity;
		rangehi = transferable.rangehi;
		rangelo = transferable.rangelo;
		formula = transferable.formula;

		modified = transferable.modified;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public String getName()
	{
		return name;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getTyp ()
	{
		return typ;
	}

	public long getModified()
	{
		return modified;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(description);
		out.writeObject(norm);
		out.writeObject(value_type);
		out.writeObject(unit);
		out.writeObject(granularity);
		out.writeObject(rangehi);
		out.writeObject(rangelo);
		out.writeObject(formula);
		out.writeLong(modified);
	}
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		description = (String )in.readObject();
		norm = (String )in.readObject();
		value_type = (String )in.readObject();
		unit = (String )in.readObject();
		granularity = (String )in.readObject();
		rangehi = (String )in.readObject();
		rangelo = (String )in.readObject();
		formula = (String )in.readObject();
		modified = in.readLong();

		transferable = new GlobalParameterType_Transferable();
	}
}