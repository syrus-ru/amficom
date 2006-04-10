/*-
 * $Id: PrimaryParameters.java,v 1.6 2006/04/10 13:26:53 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.beans.*;
import java.text.DateFormat;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.io.BellcoreStructure;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2006/04/10 13:26:53 $
 * @module analysis
 */

public class PrimaryParameters {
	private String moduleId;
	private String wavelength;
	private String pulsewidth;
	private String groupindex;
	private String averages;
	private String resolution;
	private String range;
	private String date;
	private String time;
	private String backscatter;

	private List<PropertyChangeListener> propertyChangeListeners;

	public void init(BellcoreStructure bs) {
		setAverages(Integer.toString(bs.getAverages()));
		setBackscatter((Double.toString(bs.getBackscatter()) + " " 
				+ I18N.getString(AnalysisResourceKeys.TEXT_DB)));
		setDate(DateFormat.getDateInstance().format(bs.getDate()));
		setTime(DateFormat.getTimeInstance().format(bs.getDate()));
		setGroupindex(Double.toString(bs.getIOR()));
		setModuleId(bs.getOpticalModuleId());
		setPulsewidth(Integer.toString(bs.getPulsewidth()) + " " 
				+ I18N.getString(AnalysisResourceKeys.TEXT_NS));
		setRange(Long.toString(Math.round(bs.getRange())) + " " 
				+ I18N.getString(AnalysisResourceKeys.TEXT_KM));
		setResolution(Double.toString(MathRef.floatRound(bs.getResolution(), 3)) + " " 
				+ I18N.getString(AnalysisResourceKeys.TEXT_MT));
		setWavelength(Integer.toString(bs.getWavelength()) + " " 
				+ I18N.getString(AnalysisResourceKeys.TEXT_NM));
	}
	
	public String getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(String moduleId) {
		if (this.moduleId == null || !this.moduleId.equals(moduleId)) {
			String oldValue = this.moduleId;
			this.moduleId = moduleId;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_MODULE_ID, oldValue, moduleId));
		}
	}

	public String getAverages() {
		return this.averages;
	}
	
	public void setAverages(String averages) {
		if (this.averages == null || !this.averages.equals(averages)) {
			String oldValue = this.averages;
			this.averages = averages;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_AVERAGES, oldValue, averages));
		}
	}
	
	public String getBackscatter() {
		return this.backscatter;
	}
	
	public void setBackscatter(String backscatter) {
		if (this.backscatter == null || !this.backscatter.equals(backscatter)) {
			String oldValue = this.backscatter;
			this.backscatter = backscatter;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_BACKSCATTER, oldValue, backscatter));
		}
	}
	
	public String getDate() {
		return this.date;
	}
	
	public void setDate(String date) {
		if (this.date == null || !this.date.equals(date)) {
			String oldValue = this.date;
			this.date = date;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_DATE, oldValue, date));
		}
	}
	
	public String getGroupindex() {
		return this.groupindex;
	}
	
	public void setGroupindex(String groupindex) {
		if (this.groupindex == null || !this.groupindex.equals(groupindex)) {
			String oldValue = this.groupindex;
			this.groupindex = groupindex;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_GROUPINDEX, oldValue, groupindex));
		}
	}
	
	public String getPulsewidth() {
		return this.pulsewidth;
	}
	
	public void setPulsewidth(String pulsewidth) {
		if (this.pulsewidth == null || !this.pulsewidth.equals(pulsewidth)) {
			String oldValue = this.pulsewidth;
			this.pulsewidth = pulsewidth;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_PULSEWIDTH, oldValue, pulsewidth));
		}
	}
	
	public String getRange() {
		return this.range;
	}
	
	public void setRange(String range) {
		if (this.range == null || !this.range.equals(range)) {
			String oldValue = this.range;
			this.range = range;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_RANGE, oldValue, range));
		}
	}
	
	public String getResolution() {
		return this.resolution;
	}
	
	public void setResolution(String resolution) {
		if (this.resolution == null || !this.resolution.equals(resolution)) {
			String oldValue = this.resolution;
			this.resolution = resolution;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_RESOLUTION, oldValue, resolution));
		}
	}
	
	public String getTime() {
		return this.time;
	}
	
	public void setTime(String time) {
		if (this.time == null || !this.time.equals(time)) {
			String oldValue = this.time;
			this.time = time;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_TIME, oldValue, time));
		}
	}
	
	public String getWavelength() {
		return this.wavelength;
	}
	
	public void setWavelength(String wavelength) {
		if (this.wavelength == null || !this.wavelength.equals(wavelength)) {
			String oldValue = this.wavelength;
			this.wavelength = wavelength;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, PrimaryParametersWrapper.KEY_WAVELENGTH, oldValue, wavelength));
		}
	}
	
	private void firePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.isEmpty()) {
			for (Iterator<PropertyChangeListener> iterator = this.propertyChangeListeners.iterator(); iterator.hasNext();) {
				PropertyChangeListener listener = iterator.next();
				listener.propertyChange(event);
			}
		}
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new LinkedList<PropertyChangeListener>();
		}
		if (!this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.add(propertyChangeListener);
		}
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.remove(propertyChangeListener);
		}
	}
}


