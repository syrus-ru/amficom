/*-
 * $$Id: LineThicknessComboBox.java,v 1.2 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LineThicknessComboBox extends AComboBox {

	public static final int MIN_VALUE = 1;
	public static final int MAX_VALUE = 7;

	private static Integer[] values;
	
	static {
		values = new Integer[MAX_VALUE - MIN_VALUE + 1];
		for(int i = MIN_VALUE; i < MAX_VALUE; i++) {
			values[i] = new Integer(i);
		}
	}
	
	public LineThicknessComboBox() {
		super(AComboBox.SMALL_FONT);
		for(int i = MIN_VALUE; i < MAX_VALUE; i++) {
			this.addItem(values[i]);
		}
	}

	public int getSelectedValue() {
		Integer value = (Integer )super.getSelectedItem();
		return value.intValue();
	}

	public void setSelectedValue(int value) {
		int i = value;
		if(i > MAX_VALUE)
			i = MAX_VALUE;
		if(i < MIN_VALUE)
			i = MIN_VALUE;
		this.setSelectedItem(values[i]);
	}

}
