/*
 * $Id: GlobalParameterType.java,v 1.1 2004/08/18 13:13:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;

import java.io.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class GlobalParameterType implements ObjectResource, Serializable {

	private static final long					serialVersionUID	= 01L;
	public static final String					TYPE				= "globalparametertype";
	/**
	 * @deprecated use TYPE
	 */
	public static final String					typ					= TYPE;
	private String								codename			= "";
	private String								description			= "";
	private String								formula				= "";
	private String								granularity			= "";
	private String								id					= "";
	private long									modified			= 0;
	private String								name				= "";
	private String								norm				= "";
	private String								rangehi				= "";
	private String								rangelo				= "";

	private GlobalParameterType_Transferable	transferable;
	private String								unit				= "";
	private String								valueType			= "";
	
	private boolean changed = false;

	public GlobalParameterType() {
		// nothing to do
	}

	public GlobalParameterType(GlobalParameterType_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the codename.
	 */
	public String getCodename() {
		return this.codename;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	public String getDomainId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return Returns the formula.
	 */
	public String getFormula() {
		return this.formula;
	}

	/**
	 * @return Returns the granularity.
	 */
	public String getGranularity() {
		return this.granularity;
	}

	public String getId() {
		return this.id;
	}

	public long getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the norm.
	 */
	public String getNorm() {
		return this.norm;
	}

	/**
	 * @return Returns the rangehi.
	 */
	public String getRangehi() {
		return this.rangehi;
	}

	/**
	 * @return Returns the rangelo.
	 */
	public String getRangelo() {
		return this.rangelo;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return this.unit;
	}

	/**
	 * @return Returns the valueType.
	 */
	public String getValueType() {
		return this.valueType;
	}

	/**
	 * @param codename
	 *            The codename to set.
	 */
	public void setCodename(String codename) {
		this.changed = true;
		this.codename = codename;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.changed = true;
		this.description = description;
	}

	/**
	 * @param formula
	 *            The formula to set.
	 */
	public void setFormula(String formula) {
		this.changed = true;
		this.formula = formula;
	}

	/**
	 * @param granularity
	 *            The granularity to set.
	 */
	public void setGranularity(String granularity) {
		this.changed = true;
		this.granularity = granularity;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.changed = true;
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.codename = this.transferable.codename;
		this.description = this.transferable.description;
		this.norm = this.transferable.norm;
		this.valueType = this.transferable.value_type;
		this.unit = this.transferable.unit;
		this.granularity = this.transferable.granularity;
		this.rangehi = this.transferable.rangehi;
		this.rangelo = this.transferable.rangelo;
		this.formula = this.transferable.formula;

		this.modified = this.transferable.modified;
		this.changed = false;
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.changed = true;
		this.modified = modified;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param norm
	 *            The norm to set.
	 */
	public void setNorm(String norm) {
		this.changed = true;
		this.norm = norm;
	}

	/**
	 * @param rangehi
	 *            The rangehi to set.
	 */
	public void setRangehi(String rangehi) {
		this.changed = true;
		this.rangehi = rangehi;
	}

	/**
	 * @param rangelo
	 *            The rangelo to set.
	 */
	public void setRangelo(String rangelo) {
		this.changed = true;
		this.rangelo = rangelo;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.codename = this.codename;
		this.transferable.description = this.description;
		this.transferable.norm = this.norm;
		this.transferable.value_type = this.valueType;
		this.transferable.unit = this.unit;
		this.transferable.granularity = this.granularity;
		this.transferable.rangehi = this.rangehi;
		this.transferable.rangelo = this.rangelo;
		this.transferable.formula = this.formula;

		this.transferable.modified = this.modified;
		this.changed = false;
	}

	/**
	 * @param unit
	 *            The unit to set.
	 */
	public void setUnit(String unit) {
		this.changed = true;
		this.unit = unit;
	}

	/**
	 * @param valueType
	 *            The valueType to set.
	 */
	public void setValueType(String valueType) {
		this.changed = true;
		this.valueType = valueType;
	}

	public void updateLocalFromTransferable() {
		// nothing
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.codename = (String) in.readObject();
		this.description = (String) in.readObject();
		this.norm = (String) in.readObject();
		this.valueType = (String) in.readObject();
		this.unit = (String) in.readObject();
		this.granularity = (String) in.readObject();
		this.rangehi = (String) in.readObject();
		this.rangelo = (String) in.readObject();
		this.formula = (String) in.readObject();
		this.modified = in.readLong();

		this.transferable = new GlobalParameterType_Transferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeObject(this.codename);
		out.writeObject(this.description);
		out.writeObject(this.norm);
		out.writeObject(this.valueType);
		out.writeObject(this.unit);
		out.writeObject(this.granularity);
		out.writeObject(this.rangehi);
		out.writeObject(this.rangelo);
		out.writeObject(this.formula);
		out.writeLong(this.modified);
		this.changed = false;
	}
	
	
	public ObjectResourceModel getModel() {
		throw new UnsupportedOperationException();
	}	
	
	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}	
	
	public boolean isChanged() {
		return this.changed;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}

