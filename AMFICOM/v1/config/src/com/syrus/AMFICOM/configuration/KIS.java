/*
 * $Id: KIS.java,v 1.9 2004/07/27 16:03:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;

/**
 * @version $ $, $ $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class KIS extends Equipment {
	private Identifier mcmId;

	private StorableObjectDatabase kisDatabase;

	public KIS(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(Equipment_Transferable eq) throws CreateObjectException {
		super(eq);
		this.mcmId = new Identifier(eq.mcm_id);

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[super.characteristicIds.size()];
		for (Iterator iterator = super.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] pIds = new Identifier_Transferable[super.portIds.size()];
		for (Iterator iterator = super.portIds.iterator(); iterator.hasNext();)
			pIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] cportIds = new Identifier_Transferable[super.cablePortIds.size()];
		for (Iterator iterator = super.cablePortIds.iterator(); iterator.hasNext();)
			cportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] sportIds = new Identifier_Transferable[super.specialPortIds.size()];
		for (Iterator iterator = super.specialPortIds.iterator(); iterator.hasNext();)
			sportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new Equipment_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),
																			(Identifier_Transferable)super.domainId.getTransferable(),
																			(Identifier_Transferable)super.type.getId().getTransferable(),
																			new String(super.name),
																			new String(super.description),
																			new String(super.latitude),
																			new String(super.longitude),
																			new String(super.hwSerial),
																			new String(super.swSerial),
																			new String(super.hwVersion),
																			new String(super.swVersion),
																			new String(super.inventoryNumber),
																			new String(super.manufacturer),
																			new String(super.manufacturerCode),
																			new String(super.supplier),
																			new String(super.supplierCode),
																			new String(super.eqClass),
																			(Identifier_Transferable)super.imageId.getTransferable(),
																			charIds,
																			pIds,
																			cportIds,
																			sportIds,
																			EquipmentSort.EQUIPMENT_SORT_KIS,
																			(Identifier_Transferable)this.mcmId.getTransferable());
	}

	public Identifier getMCMId() {
		return this.mcmId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						EquipmentType type,
																						String name,
																						String description,
																						String latitude,
																						String longitude,
																						String hwSerial,
																						String swSerial,
																						String hwVersion,
																						String swVersion,
																						String inventoryNumber,
																						String manufacturer,
																						String manufacturerCode,
																						String supplier,
																						String supplierCode,
																						String eqClass,
																						Identifier imageId,
																						Identifier mcmId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId,
												type,
												name,
												description,
												latitude,
												longitude,
												hwSerial,
												swSerial,
												hwVersion,
												swVersion,
												inventoryNumber,
												manufacturer,
												manufacturerCode,
												supplier,
												supplierCode,
												eqClass,
												imageId,
												EquipmentSort.EQUIPMENT_SORT_KIS.value());
		this.mcmId = mcmId;
	}
}
