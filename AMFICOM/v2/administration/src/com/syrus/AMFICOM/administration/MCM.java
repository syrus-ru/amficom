package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;

public class MCM extends StorableObject {
	private Identifier server_id;
	private String name;
	private String description;
	private String location;
	private String contact;
	private String hostname;

	private ArrayList kiss;

	private StorableObject_Database mcmDatabase;

	public MCM(Identifier id) throws RetrieveObjectException {
		super(id);

		this.mcmDatabase = StorableObject_DatabaseContext.mcmDatabase;
		try {
			this.mcmDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MCM(MCM_Transferable mt) throws CreateObjectException {
		super(new Identifier(mt.id),
					new Date(mt.created),
					new Date(mt.modified),
					new Identifier(mt.creator_id),
					new Identifier(mt.modifier_id));
		this.server_id = new Identifier(mt.server_id);
		this.name = new String(mt.name);
		this.description = new String(mt.description);
		this.location = new String(mt.location);
		this.contact = new String(mt.contact);
		this.hostname = new String(mt.hostname);

		this.kiss = new ArrayList(mt.kis_ids.length);
		for (int i = 0; i < mt.kis_ids.length; i++)
			try {
				this.kiss.add(new KIS(new Identifier(mt.kis_ids[i])));
			}
			catch (RetrieveObjectException roe) {
				throw new CreateObjectException(roe.getMessage(), roe);
			}

		this.mcmDatabase = StorableObject_DatabaseContext.mcmDatabase;
		try {
			this.mcmDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Identifier_Transferable[] kis_ids = new Identifier_Transferable[this.kiss.size()];
		int i = 0;
		for (Iterator iterator = this.kiss.iterator(); iterator.hasNext();)
			kis_ids[i++] = (Identifier_Transferable)((KIS)iterator.next()).getId().getTransferable();

		return new MCM_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																super.created.getTime(),
																super.modified.getTime(),
																(Identifier_Transferable)super.creator_id.getTransferable(),
																(Identifier_Transferable)super.modifier_id.getTransferable(),
																(Identifier_Transferable)this.server_id.getTransferable(),
																new String(this.name),
																new String(this.description),
																new String(this.location),
																new String(this.contact),
																new String(this.hostname),
																kis_ids);
	}

	public Identifier getServerId() {
		return this.server_id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getLocation() {
		return this.location;
	}

	public String getContact() {
		return this.contact;
	}

	public String getHostname() {
		return this.hostname;
	}

	public ArrayList getKISs() {
		return this.kiss;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier server_id,
																						String name,
																						String description,
																						String location,
																						String contact,
																						String hostname) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.server_id = server_id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.contact = contact;
		this.hostname = hostname;
	}

	protected synchronized void setKISs(ArrayList arraylist) {
		this.kiss = arraylist;
	}
}