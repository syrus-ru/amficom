package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Date;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

public class MeasurementSetup extends StorableObject {
	protected static final int UPDATE_ATTACH_ME = 1;
	protected static final int UPDATE_DETACH_ME = 2;

	private Set parameter_set;
	private Set criteria_set;
	private Set threshold_set;
	private Set etalon;
	private Date created;
	private Identifier creator_id;
	private Date modified;
	private String description;
	private long measurement_duration;
	private ArrayList monitored_element_ids;

	private StorableObject_Database measurementSetupDatabase;

	public MeasurementSetup(Identifier id) throws RetrieveObjectException {
		super(id);

		this.measurementSetupDatabase = StorableObject_DatabaseContext.measurementSetupDatabase;
		try {
			this.measurementSetupDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MeasurementSetup(MeasurementSetup_Transferable mst) throws CreateObjectException {
		super(new Identifier(mst.id));
		try {
			this.parameter_set = new Set(new Identifier(mst.parameter_set_id));
			this.criteria_set = (mst.criteria_set_id.identifier_code == 0)?(new Set(new Identifier(mst.criteria_set_id))):null;
			this.threshold_set = (mst.threshold_set_id.identifier_code == 0)?(new Set(new Identifier(mst.threshold_set_id))):null;
			this.etalon = (mst.etalon_id.identifier_code == 0)?(new Set(new Identifier(mst.etalon_id))):null;
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		this.created = new Date(mst.created);
		this.creator_id = new Identifier(mst.creator_id);
		this.modified = new Date(mst.modified);
		this.description = new String(mst.description);
		this.measurement_duration = mst.measurement_duration;
		this.monitored_element_ids = new ArrayList(mst.monitored_element_ids.length);
		for (int i = 0; i < mst.monitored_element_ids.length; i++)
			this.monitored_element_ids.add(new Identifier(mst.monitored_element_ids[i]));

		this.measurementSetupDatabase = StorableObject_DatabaseContext.measurementSetupDatabase;
		try {
			this.measurementSetupDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public boolean isAttachedToMonitoredElement(Identifier monitored_element_id) {
    return this.monitored_element_ids.contains(monitored_element_id);
  }

	public void attachToMonitoredElement(Identifier monitored_element_id) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitored_element_id))
      return;
		try {
			this.measurementSetupDatabase.update(this, UPDATE_ATTACH_ME, monitored_element_id);
		}
		catch (Exception e) {
			throw new UpdateObjectException("MeasurementSetup.attachToMonitoredElement | Cannot attach measurement setup '" + this.id + "' to monitored element '" + monitored_element_id + "' -- " + e.getMessage(), e);
		}
		this.monitored_element_ids.add(monitored_element_id);
		this.monitored_element_ids.trimToSize();
	}

	public void detachFromMonitoredElement(Identifier monitored_element_id) throws UpdateObjectException {
    if (!this.isAttachedToMonitoredElement(monitored_element_id))
      return;
		try {
	    this.measurementSetupDatabase.update(this, UPDATE_DETACH_ME, monitored_element_id);
		}
		catch (Exception e) {
			throw new UpdateObjectException("MeasurementSetup.detachFromMonitoredElement | Cannot dettach measurement setup '" + this.id + "' from monitored element '" + monitored_element_id + "' -- " + e.getMessage(), e);
		}
		this.monitored_element_ids.remove(monitored_element_id);
		this.monitored_element_ids.trimToSize();
  }

	public Object getTransferable() {
		Identifier_Transferable[] me_ids = new Identifier_Transferable[this.monitored_element_ids.size()];
		for (int i = 0; i < me_ids.length; i++)
			me_ids[i] = (Identifier_Transferable)((Identifier)this.monitored_element_ids.get(i)).getTransferable();

		return new MeasurementSetup_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																						 (Identifier_Transferable)this.parameter_set.getId().getTransferable(),
																						 (this.criteria_set == null)?(Identifier_Transferable)this.criteria_set.getId().getTransferable():new Identifier_Transferable(0),
																						 (this.threshold_set == null)?(Identifier_Transferable)this.threshold_set.getId().getTransferable():new Identifier_Transferable(0),
																						 (this.etalon == null)?(Identifier_Transferable)this.etalon.getId().getTransferable():new Identifier_Transferable(0),
																						 this.created.getTime(),
																						 (Identifier_Transferable)this.creator_id.getTransferable(),
																						 this.modified.getTime(),
																						 this.description,
																						 this.measurement_duration,
																						 me_ids);
	}

	public Set getParameterSet() {
		return this.parameter_set;
	}

	public Set getCriteriaSet() {
		return this.criteria_set;
	}

	public Set getThresholdSet() {
		return this.threshold_set;
	}

	public Set getEtalon() {
		return this.etalon;
	}

	public Date getCreated() {
		return this.created;
	}

	public Identifier getCreatorId() {
		return this.creator_id;
	}

	public Date getModified() {
		return this.modified;
	}

	public String getDescription() {
		return this.description;
	}

	public long getMeasurementDuration() {
		return this.measurement_duration;
	}

	public ArrayList getMonitoredElementIds() {
		return this.monitored_element_ids;
	}

	protected synchronized void setAttributes(Set parameter_set,
																						Set criteria_set,
																						Set threshold_set,
																						Set etalon,
																						Date created,
																						Identifier creator_id,
																						Date modified,
																						String description,
																						long measurement_duration) {
		this.parameter_set = parameter_set;
		this.criteria_set = criteria_set;
		this.threshold_set = threshold_set;
		this.etalon = etalon;
		this.created = created;
		this.creator_id = creator_id;
		this.modified = modified;
		this.description = description;
		this.measurement_duration = measurement_duration;
	}

	protected synchronized void setMonitoredElementIds(ArrayList monitored_element_ids) {
		this.monitored_element_ids = monitored_element_ids;
	}
}