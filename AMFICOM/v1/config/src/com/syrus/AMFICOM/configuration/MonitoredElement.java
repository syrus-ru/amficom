package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;

public class MonitoredElement extends DomainMember {
/**
 * @todo MonitoredElement - interface for Path, KIS, Link etc
 * */
	private Identifier kisId;
	private String localAddress;

	private StorableObjectDatabase monitoredElementDatabase;

	public MonitoredElement(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			this.monitoredElementDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MonitoredElement(MonitoredElement_Transferable met) throws CreateObjectException {
		super(new Identifier(met.id),
					new Date(met.created),
					new Date(met.modified),
					new Identifier(met.creator_id),
					new Identifier(met.modifier_id),
					new Identifier(met.domain_id));
		this.kisId = new Identifier(met.kis_id);
		this.localAddress = new String(met.local_address);

		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			this.monitoredElementDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		return new MonitoredElement_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																						 super.created.getTime(),
																						 super.modified.getTime(),
																						 (Identifier_Transferable)super.creatorId.getTransferable(),
																						 (Identifier_Transferable)super.modifierId.getTransferable(),
																						 (Identifier_Transferable)super.domainId.getTransferable(),
																						 (Identifier_Transferable)this.kisId.getTransferable(),
																						 new String(this.localAddress));
	}

	public Identifier getKISId() {
		return this.kisId;
	}

	public String getLocalAddress() {
		return this.localAddress;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						Identifier kisId,
																						String localAddress) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.kisId = kisId;
		this.localAddress = localAddress;
	}

	public boolean belongsToKIS(String kisId) {
		return (this.kisId.equals(kisId));
	}
}
