package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;

public class SetParameter implements TransferableObject, TypedObject {
	private Identifier id;
	private Identifier typeId;
	private byte[] value;

	public SetParameter(Parameter_Transferable pt) {
		this.id = new Identifier(pt.id);
		this.typeId = new Identifier(pt.type_id);
		this.value = new byte[pt.value.length];
		for (int i = 0; i < this.value.length; i++)
			this.value[i] = pt.value[i];
	}

	public SetParameter(Identifier id,
											Identifier typeId,
											byte[] value) {
		this.id = id;
		this.typeId = typeId;
		this.value = value;
	}

	public Object getTransferable() {
		byte[] ptValue = new byte[this.value.length];
		for (int i = 0; i < ptValue.length; i++)
			ptValue[i] = this.value[i];
		return new Parameter_Transferable((Identifier_Transferable)this.id.getTransferable(),
																			(Identifier_Transferable)this.typeId.getTransferable(),
																			ptValue);
	}

	public Identifier getId() {
		return this.id;
	}

	public Identifier getTypeId() {
		return this.typeId;
	}

	public byte[] getValue() {
		return this.value;
	}
}