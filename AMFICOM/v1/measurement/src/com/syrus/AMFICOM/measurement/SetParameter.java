/*
 * $Id: SetParameter.java,v 1.10 2004/08/06 16:07:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;

/**
 * @version $Revision: 1.10 $, $Date: 2004/08/06 16:07:06 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class SetParameter implements TransferableObject, TypedObject {
	private Identifier id;
	private ParameterType type;
	private byte[] value;	

	public SetParameter(Parameter_Transferable pt) {
		this.id = new Identifier(pt.id);
		this.type = (ParameterType)MeasurementStorableObjectPool.getStorableObject(new Identifier(pt.type_id), true);
		this.value = new byte[pt.value.length];
		for (int i = 0; i < this.value.length; i++)
			this.value[i] = pt.value[i];
	}

	public SetParameter(Identifier id,
											ParameterType type,
											byte[] value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}
	
//	public SetParameter(Identifier id,
//											String codename,
//											byte[] value) throws RetrieveObjectException, ObjectNotFoundException {
//		this.id = id;
//		this.type = ParameterTypeDatabase.retrieveForCodename(codename);
//		this.value = value;
//	}

	public Object getTransferable() {
		byte[] ptValue = new byte[this.value.length];
		for (int i = 0; i < ptValue.length; i++)
			ptValue[i] = this.value[i];
		return new Parameter_Transferable((Identifier_Transferable)this.id.getTransferable(),
																			(Identifier_Transferable)this.type.getId().getTransferable(),
																			ptValue,
																			new String(this.type.getCodename()));
	}

	public Identifier getId() {
		return this.id;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public byte[] getValue() {
		return this.value;
	}
}
