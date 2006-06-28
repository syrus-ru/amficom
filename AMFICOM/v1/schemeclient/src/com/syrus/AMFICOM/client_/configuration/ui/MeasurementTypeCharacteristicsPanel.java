/*
 * $Id: MeasurementTypeCharacteristicsPanel.java,v 1.10 2006/06/06 12:42:06 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;


/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2006/06/06 12:42:06 $
 * @module schemeclient
 */
/*
public class MeasurementTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected MeasurementType type;

	protected MeasurementTypeCharacteristicsPanel() {
		super();
	}

	protected MeasurementTypeCharacteristicsPanel(final MeasurementType measurementType) {
		this();
		this.setObject(measurementType);
	}

	public StorableObject getObject() {
		return this.type;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT_TYPE);
	}

	public void setObject(final Identifiable or) {
		this.type = (MeasurementType) or;
		super.showNoSelection();
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.type != null) {
			super.save();
		}
	}
}
*/