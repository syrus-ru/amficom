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
import com.syrus.AMFICOM.administration.corba.User_Transferable;

public class User extends StorableObject {
	private String login;
	private String type;
	private Date last_logged;
	private Date logged;
	private int sessions;

	private ArrayList category_ids;
	private ArrayList group_ids;

	private StorableObject_Database userDatabase;

	public User(Identifier id) throws RetrieveObjectException {
		super(id);

		this.userDatabase = StorableObject_DatabaseContext.userDatabase;
		try {
			this.userDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public User(User_Transferable ut) throws CreateObjectException {
		super(new Identifier(ut.id),
					new Date(ut.created),
					new Date(ut.modified),
					new Identifier(ut.creator_id),
					new Identifier(ut.modifier_id));
		this.login = new String(ut.login);
		this.type = new String(ut.type);
		this.last_logged = new Date(ut.last_login);
		this.logged = new Date(ut.logged);
		this.sessions = ut.sessions;

		this.category_ids = new ArrayList(ut.category_ids.length);
		for (int i = 0; i < ut.category_ids.length; i++)
			this.category_ids.add(new Identifier(ut.category_ids[i]));

		this.group_ids = new ArrayList(ut.group_ids.length);
		for (int i = 0; i < ut.group_ids.length; i++)
			this.group_ids.add(new Identifier(ut.group_ids[i]));

		this.userDatabase = StorableObject_DatabaseContext.userDatabase;
		try {
			this.userDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] cat_ids = new Identifier_Transferable[this.category_ids.size()];
		for (Iterator iterator = this.category_ids.iterator(); iterator.hasNext();)
			cat_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] gr_ids = new Identifier_Transferable[this.group_ids.size()];
		for (Iterator iterator = this.group_ids.iterator(); iterator.hasNext();)
			gr_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new User_Transferable((Identifier_Transferable)this.id.getTransferable(),
																 super.created.getTime(),
																 super.modified.getTime(),
																 (Identifier_Transferable)super.creator_id.getTransferable(),
																 (Identifier_Transferable)super.modifier_id.getTransferable(),
																 new String(this.login),
																 new String(this.type),
																 this.last_logged.getTime(),
																 this.logged.getTime(),
																 this.sessions,
																 cat_ids,
																 gr_ids);
	}

	public String getLogin() {
		return this.login;
	}

	public String getType() {
		return this.type;
	}

	public Date getLastLogged() {
		return this.last_logged;
	}

	public Date getLogged() {
		return this.logged;
	}

	public int getSessions() {
		return this.sessions;
	}

	public ArrayList getCategoryIds() {
		return this.category_ids;
	}

	public ArrayList getGroupIds() {
		return this.group_ids;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String login,
																						String type,
																						Date last_logged,
																						Date logged,
																						int sessions) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.login = login;
		this.type = type;
		this.last_logged = last_logged;
		this.logged = logged;
		this.sessions = sessions;
	}

	protected void setCategoryIds(ArrayList category_ids) {
		this.category_ids = category_ids;
	}

	protected void setGroupIds(ArrayList group_ids) {
		this.group_ids = group_ids;
	}
}