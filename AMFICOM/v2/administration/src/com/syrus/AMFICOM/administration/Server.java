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
import com.syrus.AMFICOM.administration.corba.Server_Transferable;

public class Server extends StorableObject {
	private String name;
	private String description;
	private String location;
	private String contact;
	private String hostname;
	private int sessions;

	private ArrayList mcms;

	private StorableObject_Database serverDatabase;

	public Server(Identifier id) throws RetrieveObjectException {
		super(id);

		this.serverDatabase = StorableObject_DatabaseContext.serverDatabase;
		try {
			this.serverDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Server(Server_Transferable st) throws CreateObjectException {
		super(new Identifier(st.id),
					new Date(st.created),
					new Date(st.modified),
					new Identifier(st.creator_id),
					new Identifier(st.modifier_id));
		this.name = new String(st.name);
		this.description = new String(st.description);
		this.location = new String(st.location);
		this.contact = new String(st.contact);
		this.hostname = new String(st.hostname);
		this.sessions = st.sessions;

		this.mcms = new ArrayList(st.mcm_ids.length);
		for (int i = 0; i < st.mcm_ids.length; i++)
			try {
				this.mcms.add(new MCM(new Identifier(st.mcm_ids[i])));
			}
			catch (RetrieveObjectException roe) {
				throw new CreateObjectException(roe.getMessage(), roe);
			}

		this.serverDatabase = StorableObject_DatabaseContext.serverDatabase;
		try {
			this.serverDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Identifier_Transferable[] mcm_ids = new Identifier_Transferable[this.mcms.size()];
		int i = 0;
		for (Iterator iterator = this.mcms.iterator(); iterator.hasNext();)
			mcm_ids[i++] = (Identifier_Transferable)((MCM)iterator.next()).getId().getTransferable();

		return new Server_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creator_id.getTransferable(),
																	 (Identifier_Transferable)super.modifier_id.getTransferable(),
																	 new String(this.name),
																	 new String(this.description),
																	 new String(this.location),
																	 new String(this.contact),
																	 new String(this.hostname),
																	 this.sessions,
																	 mcm_ids);
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

	public int getSessions() {
		return this.sessions;
	}

	public ArrayList getMCMs() {
		return this.mcms;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String name,
																						String description,
																						String location,
																						String contact,
																						String hostname,
																						int sessions) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.name = name;
		this.description = description;
		this.location = location;
		this.contact = contact;
		this.hostname = hostname;
		this.sessions = sessions;
	}

	protected synchronized void setMCMs(ArrayList arraylist) {
		this.mcms = arraylist;
	}
}