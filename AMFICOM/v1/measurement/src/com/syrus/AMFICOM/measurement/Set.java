package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.UpdateObjectException;
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
	private List monitoredElementIds;

	private StorableObjectDatabase setDatabase;

	public Set(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.setDatabase = MeasurementDatabaseContext.setDatabase;
		try {
			this.setDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
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

		this.monitoredElementIds = new ArrayList(st.monitored_element_ids.length);
		for (int i = 0; i < st.monitored_element_ids.length; i++)
			this.monitoredElementIds.add(new Identifier(st.monitored_element_ids[i]));

		this.setDatabase = MeasurementDatabaseContext.setDatabase;
		try {
			this.setDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}	
	
	private Set(Identifier id,
							Identifier creatorId,
							SetSort sort,
							String description,
							SetParameter[] parameters,
							List monitoredElementIds) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.sort = sort.value();
		this.description = description;
		this.parameters = parameters;
		this.monitoredElementIds = monitoredElementIds;

		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param sort
	 * @param description
	 * @param parameters
	 * @param monitoredElementIds
	 * @return
	 */
	public static Set createInstance(Identifier id,
																	 Identifier creatorId,
																	 SetSort sort,
																	 String description,
																	 SetParameter[] parameters,
																	 List monitoredElementIds) {
		return new Set(id,
									 creatorId,
									 sort,
									 description,
									 parameters,
									 monitoredElementIds);
	}

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
    return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(Identifier monitoredElementId,
																			 Identifier modifierId) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitoredElementId))
      return;
		super.modifierId = (Identifier)modifierId.clone();
		try {
			this.setDatabase.update(this, UPDATE_ATTACH_ME, monitoredElementId);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException("MeasurementSetup.attachToMonitoredElement | Cannot attach measurement setup '" + this.id + "' to monitored element '" + monitoredElementId + "' -- " + e.getMessage(), e);
		}
		this.monitoredElementIds.add(monitoredElementId);
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId,
																				 Identifier modifierId) throws UpdateObjectException {
    if (!this.isAttachedToMonitoredElement(monitoredElementId))
      return;
		super.modifierId = (Identifier)modifierId.clone();
		try {
	    this.setDatabase.update(this, UPDATE_DETACH_ME, monitoredElementId);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException("MeasurementSetup.detachFromMonitoredElement | Cannot dettach measurement setup '" + this.id + "' from monitored element '" + monitoredElementId + "' -- " + e.getMessage(), e);
		}
		this.monitoredElementIds.remove(monitoredElementId);
  }

	public Object getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable)this.parameters[i].getTransferable();

		Identifier_Transferable[] meIds = new Identifier_Transferable[this.monitoredElementIds.size()];
		int i = 0;
		for (Iterator iterator = this.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
	
		return new Set_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																super.created.getTime(),
																super.modified.getTime(),
																(Identifier_Transferable)super.creatorId.getTransferable(),
																(Identifier_Transferable)super.modifierId.getTransferable(),
																SetSort.from_int(this.sort),
																new String(this.description),
																pts,
																meIds);
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

	public List getMonitoredElementIds() {
		return this.monitoredElementIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						int sort,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.sort = sort;
		this.description = description;
	}

	protected synchronized void setParameters(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	protected synchronized void setMonitoredElementIds(List monitoredElementIds) {
		this.monitoredElementIds = monitoredElementIds;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.currentVersion = super.getNextVersion();
		this.description = description;
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(int sort) {
		this.currentVersion = super.getNextVersion();
		this.sort = sort;
	}
}
