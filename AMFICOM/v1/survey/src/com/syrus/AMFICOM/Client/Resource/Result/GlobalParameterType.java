package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

import java.io.*;

public class GlobalParameterType extends ObjectResource implements Serializable {

	private static final long					serialVersionUID	= 01L;
	public static final String					typ					= "globalparametertype";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								codename			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								description			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								formula				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								granularity			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long									modified			= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								name				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								norm				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								rangehi				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								rangelo				= "";

	private GlobalParameterType_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								unit				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								value_type			= "";

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
		return codename;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the formula.
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @return Returns the granularity.
	 */
	public String getGranularity() {
		return granularity;
	}

	public String getId() {
		return id;
	}

	public long getModified() {
		return modified;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the norm.
	 */
	public String getNorm() {
		return norm;
	}

	/**
	 * @return Returns the rangehi.
	 */
	public String getRangehi() {
		return rangehi;
	}

	/**
	 * @return Returns the rangelo.
	 */
	public String getRangelo() {
		return rangelo;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @return Returns the valueType.
	 */
	public String getValueType() {
		return value_type;
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
		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		description = transferable.description;
		norm = transferable.norm;
		value_type = transferable.value_type;
		unit = transferable.unit;
		granularity = transferable.granularity;
		rangehi = transferable.rangehi;
		rangelo = transferable.rangelo;
		formula = transferable.formula;

		modified = transferable.modified;
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
		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
		transferable.description = description;
		transferable.norm = norm;
		transferable.value_type = value_type;
		transferable.unit = unit;
		transferable.granularity = granularity;
		transferable.rangehi = rangehi;
		transferable.rangelo = rangelo;
		transferable.formula = formula;

		transferable.modified = modified;
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
		this.value_type = valueType;
	}

	public void updateLocalFromTransferable() {
		// nothing
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		name = (String) in.readObject();
		codename = (String) in.readObject();
		description = (String) in.readObject();
		norm = (String) in.readObject();
		value_type = (String) in.readObject();
		unit = (String) in.readObject();
		granularity = (String) in.readObject();
		rangehi = (String) in.readObject();
		rangelo = (String) in.readObject();
		formula = (String) in.readObject();
		modified = in.readLong();

		transferable = new GlobalParameterType_Transferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(description);
		out.writeObject(norm);
		out.writeObject(value_type);
		out.writeObject(unit);
		out.writeObject(granularity);
		out.writeObject(rangehi);
		out.writeObject(rangelo);
		out.writeObject(formula);
		out.writeLong(modified);
		this.changed = false;
	}
}

