package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;

public class ParameterType extends StorableObject {
	private String codename;
	private String name;
	private String description;

	private StorableObject_Database parameterTypeDatabase;

	public ParameterType(Identifier id) throws RetrieveObjectException {
		super(id);

		this.parameterTypeDatabase = StorableObject_DatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public ParameterType(ParameterType_Transferable ptt) throws CreateObjectException {
		super(new Identifier(ptt.id));
		this.codename = new String(ptt.codename);
		this.name = new String(ptt.name);
		this.description = new String(ptt.description);

		this.parameterTypeDatabase = StorableObject_DatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private ParameterType(Identifier id,
												String codename,
												String name,
												String description) throws CreateObjectException {
		super(id);
		this.codename = codename;
		this.name = name;
		this.description = description;

		this.parameterTypeDatabase = StorableObject_DatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new ParameterType_Transferable((Identifier_Transferable)this.id.getTransferable(),
																					new String(this.codename),
																					new String(this.name),
																					new String(this.description));
	}

	public String getCodename() {
		return this.codename;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	protected void setAttributes(String codename,
															 String name,
															 String description) {
		this.codename = codename;													 
		this.name = name;
		this.description = description;
	}
}