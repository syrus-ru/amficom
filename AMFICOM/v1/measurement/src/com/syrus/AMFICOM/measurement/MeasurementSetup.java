package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Date;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

public class MeasurementSetup extends StorableObject {
	protected static final int UPDATE_ATTACH_ME = 1;
	protected static final int UPDATE_DETACH_ME = 2;

	private Set parameterSet;
	private Set criteriaSet;
	private Set thresholdSet;
	private Set etalon;
	private String description;
	private long measurementDuration;
	private ArrayList monitoredElementIds;

	private StorableObjectDatabase measurementSetupDatabase;

	public MeasurementSetup(Identifier id) throws RetrieveObjectException {
		super(id);

		this.measurementSetupDatabase = MeasurementDatabaseContext.measurementSetupDatabase;
		try {
			this.measurementSetupDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MeasurementSetup(MeasurementSetup_Transferable mst) throws CreateObjectException {
		super(new Identifier(mst.id),
					new Date(mst.created),
					new Date(mst.modified),
					new Identifier(mst.creator_id),
					new Identifier(mst.modifier_id));
		try {
			this.parameterSet = new Set(new Identifier(mst.parameter_set_id));
			/**
			 * @todo when change DB Identifier model ,change identifier_string to
			 *       identifier_code
			 */
			this.criteriaSet = (mst.criteria_set_id.identifier_string != null)?(new Set(new Identifier(mst.criteria_set_id))):null;
			/**
			 * @todo when change DB Identifier model ,change identifier_string to
			 *       identifier_code
			 */
			this.thresholdSet = (mst.threshold_set_id.identifier_string != null)?(new Set(new Identifier(mst.threshold_set_id))):null;
			/**
			 * @todo when change DB Identifier model ,change identifier_string to
			 *       identifier_code
			 */
			this.etalon = (mst.etalon_id.identifier_string != null)?(new Set(new Identifier(mst.etalon_id))):null;
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		this.description = new String(mst.description);
		this.measurementDuration = mst.measurement_duration;
		this.monitoredElementIds = new ArrayList(mst.monitored_element_ids.length);
		for (int i = 0; i < mst.monitored_element_ids.length; i++)
			this.monitoredElementIds.add(new Identifier(mst.monitored_element_ids[i]));

		this.measurementSetupDatabase = MeasurementDatabaseContext.measurementSetupDatabase;
		try {
			this.measurementSetupDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
    return this.monitoredElementIds.contains(monitoredElementId);
  }

	public void attachToMonitoredElement(Identifier monitoredElementId,
																			 Identifier modifierId) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitoredElementId))
      return;
		super.modifier_id = (Identifier)modifierId.clone();
		try {
			this.measurementSetupDatabase.update(this, UPDATE_ATTACH_ME, monitoredElementId);
		}
		catch (Exception e) {
			throw new UpdateObjectException("MeasurementSetup.attachToMonitoredElement | Cannot attach measurement setup '" + this.id + "' to monitored element '" + monitoredElementId + "' -- " + e.getMessage(), e);
		}
		this.monitoredElementIds.add(monitoredElementId);
		this.monitoredElementIds.trimToSize();
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId,
																				 Identifier modifierId) throws UpdateObjectException {
    if (!this.isAttachedToMonitoredElement(monitoredElementId))
      return;
		super.modifier_id = (Identifier)modifierId.clone();
		try {
	    this.measurementSetupDatabase.update(this, UPDATE_DETACH_ME, monitoredElementId);
		}
		catch (Exception e) {
			throw new UpdateObjectException("MeasurementSetup.detachFromMonitoredElement | Cannot dettach measurement setup '" + this.id + "' from monitored element '" + monitoredElementId + "' -- " + e.getMessage(), e);
		}
		this.monitoredElementIds.remove(monitoredElementId);
		this.monitoredElementIds.trimToSize();
  }

	public Object getTransferable() {
		Identifier_Transferable[] meIds = new Identifier_Transferable[this.monitoredElementIds.size()];
		for (int i = 0; i < meIds.length; i++)
			meIds[i] = (Identifier_Transferable)((Identifier)this.monitoredElementIds.get(i)).getTransferable();

		return new MeasurementSetup_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																						 super.created.getTime(),
																						 super.modified.getTime(),
																						 (Identifier_Transferable)super.creator_id.getTransferable(),
																						 (Identifier_Transferable)super.modifier_id.getTransferable(),
																						 (Identifier_Transferable)this.parameterSet.getId().getTransferable(),
																						 (this.criteriaSet == null)?(Identifier_Transferable)this.criteriaSet.getId().getTransferable():null,
																						 (this.thresholdSet == null)?(Identifier_Transferable)this.thresholdSet.getId().getTransferable():null,
																						 (this.etalon == null)?(Identifier_Transferable)this.etalon.getId().getTransferable():null,
																						 this.description,
																						 this.measurementDuration,
																						 meIds);
	}

	public Set getParameterSet() {
		return this.parameterSet;
	}

	public Set getCriteriaSet() {
		return this.criteriaSet;
	}

	public Set getThresholdSet() {
		return this.thresholdSet;
	}

	public Set getEtalon() {
		return this.etalon;
	}

	public String getDescription() {
		return this.description;
	}

	public long getMeasurementDuration() {
		return this.measurementDuration;
	}

	public ArrayList getMonitoredElementIds() {
		return this.monitoredElementIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Set parameterSet,
																						Set criteriaSet,
																						Set thresholdSet,
																						Set etalon,
																						String description,
																						long measurementDuration) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.parameterSet = parameterSet;
		this.criteriaSet = criteriaSet;
		this.thresholdSet = thresholdSet;
		this.etalon = etalon;
		this.description = description;
		this.measurementDuration = measurementDuration;
	}

	protected synchronized void setMonitoredElementIds(ArrayList monitoredElementIds) {
		this.monitoredElementIds = monitoredElementIds;
	}
}