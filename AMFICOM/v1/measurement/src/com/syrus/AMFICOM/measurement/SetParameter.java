package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;

public class SetParameter implements TransferableObject {
	private Identifier id;
	private Identifier type_id;
	private byte[] value;

	public SetParameter(Parameter_Transferable pt) {
		this.id = new Identifier(pt.id);
		this.type_id = new Identifier(pt.type_id);
		this.value = new byte[pt.value.length];
		for (int i = 0; i < this.value.length; i++)
			this.value[i] = pt.value[i];
	}

	protected SetParameter(Identifier id,
												 Identifier type_id,
												 byte[] value) {
		this.id = id;
		this.type_id = type_id;
		this.value = value;
	}

	public Object getTransferable() {
		byte[] pt_value = new byte[this.value.length];
		for (int i = 0; i < pt_value.length; i++)
			pt_value[i] = this.value[i];
		return new Parameter_Transferable((Identifier_Transferable)this.id.getTransferable(),
																			(Identifier_Transferable)this.type_id.getTransferable(),
																			pt_value);
	}

	public Identifier getId() {
		return this.id;
	}

	public Identifier getTypeId() {
		return this.type_id;
	}

	public byte[] getValue() {
		return this.value;
	}
}