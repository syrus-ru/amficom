package com.syrus.AMFICOM.Client.Resource.Network;

import java.io.*;

import java.awt.Component;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CharacteristicType;

public class Characteristic extends StubResource
		implements Cloneable, Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "characteristic";

	public Characteristic_Transferable transferable;

	public String id = "";
	public String ch_class = "";
	public String name = "";
	public String description = "";
	public String type_id = "";
	public String value = "";
	public String value_type_id = "";

	public Characteristic()
	{
		transferable = new Characteristic_Transferable();
	}

	public Characteristic(Characteristic_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Characteristic(
			String id,
			String ch_class,
			String name,
			String description,
			String type_id,
			String value,
			String value_type_id)
	{
		this.id = id;
		this.ch_class = ch_class;
		this.name = name;
		this.description = description;
		this.type_id = type_id;
		this.value = value;
		this.value_type_id = value_type_id;

		transferable = new Characteristic_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		ch_class = transferable.ch_class;
		name = transferable.name;
		description = transferable.description;
		type_id = transferable.type_id;
		value = transferable.value;
		value_type_id = transferable.value_type_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.ch_class = ch_class;
		transferable.name = name;
		transferable.description = description;
		transferable.type_id = type_id;
		transferable.value = value;
		transferable.value_type_id = value_type_id;
	}

	public Characteristic duplicate()
	{
		try
		{
			return (Characteristic )clone();
		}
		catch(Exception e)
		{
			System.out.println("could not clone");
			e.printStackTrace();
			return null;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return type_id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public Object clone()
	{
		return new 	Characteristic(
				id,
				ch_class,
				name,
				description,
				type_id,
				value,
				value_type_id);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(ch_class);
		out.writeObject(description);
		out.writeObject(type_id);
		out.writeObject(value);
		out.writeObject(value_type_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		ch_class = (String )in.readObject();
		description = (String )in.readObject();
		type_id = (String )in.readObject();
		value = (String )in.readObject();
		value_type_id = (String )in.readObject();

		transferable = new Characteristic_Transferable();
	}



	public Component getRenderer()
		{
		return null;
	}

	public Component getEditor()
		{
		return null;
	}

	public void setValue(Object val)
	{
		value = (String )val;
	}

	public boolean isEditable()
	{
		CharacteristicType ct = (CharacteristicType )Pool.get(CharacteristicType.typ, this.type_id);
		if(ct == null)
			return false;
		return ct.editable;
	}


}
