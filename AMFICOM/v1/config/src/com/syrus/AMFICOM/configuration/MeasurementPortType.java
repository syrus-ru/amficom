/*
 * $Id: MeasurementPortType.java,v 1.3 2004/08/18 18:08:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/18 18:08:05 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class MeasurementPortType extends StorableObjectType {

	private StorableObjectDatabase measurementPortTypeDatabase;

	public MeasurementPortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			this.measurementPortTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(MeasurementPortType_Transferable ptt) throws CreateObjectException {
		super(new Identifier(ptt.id),
					new Date(ptt.created),
					new Date(ptt.modified),
					new Identifier(ptt.creator_id),
					new Identifier(ptt.modifier_id),
					new String(ptt.codename),
					new String(ptt.description));

		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			this.measurementPortTypeDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}
	
	private MeasurementPortType(Identifier id,
								Identifier creatorId,
								String codename,
								String description){
						super(id,
								new Date(System.currentTimeMillis()),
								new Date(System.currentTimeMillis()),
								creatorId,
								creatorId,
								codename,
								description);						
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @return
	 */
	public static MeasurementPortType createInstance(Identifier id,
													 Identifier creatorId,
													 String codename,
													 String description){
		return new MeasurementPortType(id,
									   creatorId,
									   codename,
									   description);
	}
	
	public Object getTransferable() {
		return new MeasurementPortType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																								super.created.getTime(),
																								super.modified.getTime(),
																								(Identifier_Transferable)super.creatorId.getTransferable(),
																								(Identifier_Transferable)super.modifierId.getTransferable(),
																								new String(super.codename),
																								(super.description != null) ? (new String(super.description)) : "");
	}
	
	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
	}
}
