package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;

public class KIS extends StorableObject {
	private Identifier mcm_id;
	private String name;
	private String description;

	private StorableObject_Database kisDatabase;
/*
	private ArrayList monitoredElements;*/

	public KIS(Identifier id) throws RetrieveObjectException {
		super(id);

		this.kisDatabase = StorableObject_DatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
/*
		this.monitoredElements = this.retrieveMonitoredElements();*/
	}

	public KIS(KIS_Transferable kt) throws CreateObjectException {
		super(new Identifier(kt.id));
		this.mcm_id = new Identifier(kt.mcm_id);
		this.name = new String(kt.name);
		this.description = new String(kt.description);

		this.kisDatabase = StorableObject_DatabaseContext.kisDatabase;
		try {
			this.kisDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
/*
		this.monitoredElements = this.retrieveMonitoredElements();*/
	}

	public Object getTransferable() {
		return new KIS_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																(Identifier_Transferable)this.mcm_id.getTransferable(),
																new String(this.name),
																new String(this.description));
	}

	public Identifier getMCMId() {
		return this.mcm_id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
/*
	public ArrayList getMonitoredElements() {
		return this.monitoredElements;
	}*/

	protected synchronized void setAttributes(Identifier mcm_id,
																						String name,
																						String description) {
		this.mcm_id = mcm_id;
		this.name = name;
		this.description = description;
	}
/*
	public ArrayList retrieveMonitoredElements() throws RetrieveObjectException {
		try {
			return this.kisDatabase.retrieveMonitoredElements(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}*/
}