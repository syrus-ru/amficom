package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;

public class MonitoredElement extends DomainMember {
/**
 * @todo MonitoredElement - interface for Path, KIS, Link etc
 * */
	private Identifier kis_id;
	private String local_address;

	private StorableObject_Database monitoredElementDatabase;

	public MonitoredElement(Identifier id) throws RetrieveObjectException {
		super(id);

		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			this.monitoredElementDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MonitoredElement(MonitoredElement_Transferable met) throws CreateObjectException {
		super(new Identifier(met.id),
					new Date(met.created),
					new Date(met.modified),
					new Identifier(met.creator_id),
					new Identifier(met.modifier_id),
					new Identifier(met.domain_id));
		this.kis_id = new Identifier(met.kis_id);
		this.local_address = new String(met.local_address);

		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			this.monitoredElementDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new MonitoredElement_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																						 super.created.getTime(),
																						 super.modified.getTime(),
																						 (Identifier_Transferable)super.creator_id.getTransferable(),
																						 (Identifier_Transferable)super.modifier_id.getTransferable(),
																						 (Identifier_Transferable)super.domain_id.getTransferable(),
																						 (Identifier_Transferable)this.kis_id.getTransferable(),
																						 new String(this.local_address));
	}

	public Identifier getKISId() {
		return this.kis_id;
	}

	public String getLocalAddress() {
		return this.local_address;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domain_id,
																						Identifier kis_id,
																						String local_address) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domain_id);
		this.kis_id = kis_id;
		this.local_address = local_address;
	}

	public boolean belongsToKIS(String kis_id) {
		return (this.kis_id.equals(kis_id));
	}
}