package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;

public class SetParameter implements TransferableObject, TypedObject {
	private Identifier id;
	private ParameterType type;
	private byte[] value;	

	public SetParameter(Parameter_Transferable pt) {
		this.id = new Identifier(pt.id);
		this.type = (ParameterType)MeasurementObjectTypePool.getObjectType(new Identifier(pt.type_id));
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
