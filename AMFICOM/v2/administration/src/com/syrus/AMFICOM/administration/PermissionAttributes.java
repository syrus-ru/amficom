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
import com.syrus.AMFICOM.administration.corba.PermissionAttributes_Transferable;

public class PermissionAttributes extends StorableObject {
	private String name;
	private String codename;
	private String rwx;
	private String deny_message;

	private ArrayList group_ids;
	private ArrayList category_ids;

	private StorableObject_Database permissionAttributesDatabase;

	public PermissionAttributes(Identifier id) throws RetrieveObjectException {
		super(id);

		this.permissionAttributesDatabase = AdministrationDatabaseContext.permissionAttributesDatabase;
		try {
			this.permissionAttributesDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PermissionAttributes(PermissionAttributes_Transferable pat) throws CreateObjectException {
		super(new Identifier(pat.id),
					new Date(pat.created),
					new Date(pat.modified),
					new Identifier(pat.creator_id),
					new Identifier(pat.modifier_id));
		this.name = new String(pat.name);
		this.codename = new String(pat.codename);
		this.rwx = new String(pat.rwx);
		this.deny_message = new String(pat.deny_message);

		this.group_ids = new ArrayList(pat.group_ids.length);
		for (int i = 0; i < pat.group_ids.length; i++)
			this.group_ids.add(new Identifier(pat.group_ids[i]));

		this.category_ids = new ArrayList(pat.category_ids.length);
		for (int i = 0; i < pat.category_ids.length; i++)
			this.category_ids.add(new Identifier(pat.category_ids[i]));

		this.permissionAttributesDatabase = AdministrationDatabaseContext.permissionAttributesDatabase;
		try {
			this.permissionAttributesDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] gro_ids = new Identifier_Transferable[this.group_ids.size()];
		for (Iterator iterator = this.group_ids.iterator(); iterator.hasNext();)
			gro_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] cat_ids = new Identifier_Transferable[this.category_ids.size()];
		for (Iterator iterator = this.category_ids.iterator(); iterator.hasNext();)
			cat_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new PermissionAttributes_Transferable((Identifier_Transferable)this.id.getTransferable(),
																								 super.created.getTime(),
																								 super.modified.getTime(),
																								 (Identifier_Transferable)super.creator_id.getTransferable(),
																								 (Identifier_Transferable)super.modifier_id.getTransferable(),
																								 new String(this.name),
																								 new String(this.codename),
																								 new String(this.rwx),
																								 new String(this.deny_message),
																								 gro_ids,
																								 cat_ids);
	}

	public String getName() {
		return this.name;
	}

	public String getCodename() {
		return this.codename;
	}

	public String getRWX() {
		return this.rwx;
	}

	public String getDenyMessage() {
		return this.deny_message;
	}

	public ArrayList getGroupIds() {
		return this.group_ids;
	}

	public ArrayList getCategoryIds() {
		return this.category_ids;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String name,
																						String codename,
																						String rwx,
																						String deny_message) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.name = name;
		this.codename = codename;
		this.rwx = rwx;
		this.deny_message = deny_message;
	}

	protected synchronized void setGroupIds(ArrayList group_ids) {
		this.group_ids = group_ids;
	}

	protected synchronized void setCategoryIds(ArrayList category_ids) {
		this.category_ids = category_ids;
	}
}