package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;

public class Domain extends DomainMember {
	private String name;
	private String description;
	private Identifier owner_id;
	private PermissionAttributes permission_attributes;

	private ArrayList domain_ids;

	private StorableObject_Database domainDatabase;

	public Domain(Identifier id) throws RetrieveObjectException {
		super(id);

		this.domainDatabase = AdministrationDatabaseContext.domainDatabase;
		try {
			this.domainDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Domain(Domain_Transferable dt) throws CreateObjectException {
		super(new Identifier(dt.id),
					new Date(dt.created),
					new Date(dt.modified),
					new Identifier(dt.creator_id),
					new Identifier(dt.modifier_id),
					new Identifier(dt.domain_id));
		this.name = new String(this.name);
		this.description = new String(this.description);
		this.owner_id = new Identifier(dt.owner_id);
		try {
			this.permission_attributes = new PermissionAttributes(new Identifier(dt.permission_attributes_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}

		this.domain_ids = new ArrayList(dt.domain_ids.length);
		for (int i = 0; i < dt.domain_ids.length; i++)
			this.domain_ids.add(new Identifier(dt.domain_ids[i]));

		this.domainDatabase = AdministrationDatabaseContext.domainDatabase;
		try {
			this.domainDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] dom_ids = new Identifier_Transferable[this.domain_ids.size()];
		for (Iterator iterator = this.domain_ids.iterator(); iterator.hasNext();)
			dom_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
		
		return new Domain_Transferable((Identifier_Transferable)this.id.getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creator_id.getTransferable(),
																	 (Identifier_Transferable)super.modifier_id.getTransferable(),
																	 (Identifier_Transferable)super.domain_id.getTransferable(),
																	 new String(this.name),
																	 new String(this.description),
																	 (Identifier_Transferable)this.owner_id.getTransferable(),
																	 (Identifier_Transferable)this.permission_attributes.getId().getTransferable(),
																	 dom_ids);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public Identifier getOwnerId() {
		return this.owner_id;
	}

	public PermissionAttributes getPermissionAttributes() {
		return this.permission_attributes;
	}

	public ArrayList getDomainIds() {
		return this.domain_ids;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domain_id,
																						String name,
																						String description,
																						Identifier owner_id,
																						PermissionAttributes permission_attributes) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domain_id);
		this.name = name;
		this.description = description;
		this.owner_id = owner_id;
		this.permission_attributes = permission_attributes;
	}

	protected synchronized void setDomainIds(ArrayList domain_ids) {
		this.domain_ids = domain_ids;
	}
}