package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;

public class Set extends StorableObject {
	protected static final int UPDATE_ATTACH_ME = 1;
	protected static final int UPDATE_DETACH_ME = 2;

	private int sort;
	private String description;
	private SetParameter[] parameters;
	private ArrayList monitored_element_ids;

	private StorableObject_Database setDatabase;

	public Set(Identifier id) throws RetrieveObjectException {
		super(id);

		this.setDatabase = MeasurementDatabaseContext.setDatabase;
		try {
			this.setDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Set(Set_Transferable st) throws CreateObjectException {
		super(new Identifier(st.id),
					new Date(st.created),
					new Date(st.modified),
					new Identifier(st.creator_id),
					new Identifier(st.modifier_id));
		this.sort = st.sort.value();
		this.description = new String(st.description);

		this.parameters = new SetParameter[st.parameters.length];
		for (int i = 0; i < this.parameters.length; i++)
			this.parameters[i] = new SetParameter(st.parameters[i]);

		this.monitored_element_ids = new ArrayList(st.monitored_element_ids.length);
		for (int i = 0; i < st.monitored_element_ids.length; i++)
			this.monitored_element_ids.add(new Identifier(st.monitored_element_ids[i]));

		this.setDatabase = MeasurementDatabaseContext.setDatabase;
		try {
			this.setDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public boolean isAttachedToMonitoredElement(Identifier monitored_element_id) {
    return this.monitored_element_ids.contains(monitored_element_id);
  }

	public void attachToMonitoredElement(Identifier monitored_element_id,
																			 Identifier modifier_id) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitored_element_id))
      return;
		super.modifier_id = (Identifier)modifier_id.clone();
		try {
			this.setDatabase.update(this, UPDATE_ATTACH_ME, monitored_element_id);
		}
		catch (Exception e) {
			throw new UpdateObjectException("MeasurementSetup.attachToMonitoredElement | Cannot attach measurement setup '" + this.id + "' to monitored element '" + monitored_element_id + "' -- " + e.getMessage(), e);
		}
		this.monitored_element_ids.add(monitored_element_id);
		this.monitored_element_ids.trimToSize();
	}

	public void detachFromMonitoredElement(Identifier monitored_element_id,
																				 Identifier modifier_id) throws UpdateObjectException {
    if (!this.isAttachedToMonitoredElement(monitored_element_id))
      return;
		super.modifier_id = (Identifier)modifier_id.clone();
		try {
	    this.setDatabase.update(this, UPDATE_DETACH_ME, monitored_element_id);
		}
		catch (Exception e) {
			throw new UpdateObjectException("MeasurementSetup.detachFromMonitoredElement | Cannot dettach measurement setup '" + this.id + "' from monitored element '" + monitored_element_id + "' -- " + e.getMessage(), e);
		}
		this.monitored_element_ids.remove(monitored_element_id);
		this.monitored_element_ids.trimToSize();
  }

	public Object getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable)this.parameters[i].getTransferable();

		Identifier_Transferable[] me_ids = new Identifier_Transferable[this.monitored_element_ids.size()];
		int i = 0;
		for (Iterator iterator = this.monitored_element_ids.iterator(); iterator.hasNext();)
			me_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
	
		return new Set_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																super.created.getTime(),
																super.modified.getTime(),
																(Identifier_Transferable)super.creator_id.getTransferable(),
																(Identifier_Transferable)super.modifier_id.getTransferable(),
																SetSort.from_int(this.sort),
																new String(this.description),
																pts,
																me_ids);
	}

	public SetSort getSort() {
		return SetSort.from_int(this.sort);
	}

	public String getDescription() {
		return this.description;
	}

	public SetParameter[] getParameters() {
		return this.parameters;
	}

	public ArrayList getMonitoredElementIds() {
		return this.monitored_element_ids;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						int sort,
																						String description) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.sort = sort;
		this.description = description;
	}

	protected synchronized void setParameters(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	protected synchronized void setMonitoredElementIds(ArrayList monitored_element_ids) {
		this.monitored_element_ids = monitored_element_ids;
	}
}