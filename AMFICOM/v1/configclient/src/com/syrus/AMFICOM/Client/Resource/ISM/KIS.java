package com.syrus.AMFICOM.Client.Resource.ISM;

import java.io.Serializable;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.ISM.KIS_Transferable;
import com.syrus.AMFICOM.Client.Resource.StubResource;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

public class KIS extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "kis";

	KIS_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String typeId = "";

	public String equipmentId = "";
	public String mcmId = "";

	public Map characteristics;

	public KIS()
	{
		characteristics = new HashMap();

		transferable = new KIS_Transferable();
	}

	public KIS(KIS_Transferable transferable)
	{
		super();
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public KIS(
			String id,
			String name,
			String typeId,
			String description,
			String equipmentId,
			String mcmId)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.typeId = typeId;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
		characteristics = new HashMap();

		transferable = new KIS_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		typeId = transferable._typeId;
		equipmentId = transferable.equipmentId;
		mcmId = transferable.mcmId;

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable._typeId = typeId;
		transferable.equipmentId = equipmentId;
		transferable.mcmId = mcmId;

		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[characteristics.size()];
		for (Iterator it = characteristics.values().iterator(); it.hasNext(); ) {
			Characteristic ch = (Characteristic) it.next();
			ch.setTransferableFromLocal();
			transferable.characteristics[i++] = ch.transferable;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public void updateLocalFromTransferable()
	{
	}
/*
	public ObjectResourceModel getModel()
	{
		return new KISModel(this);
	}
*/

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.KISPane";
	}

}

