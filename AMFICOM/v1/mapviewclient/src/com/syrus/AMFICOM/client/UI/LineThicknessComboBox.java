package com.syrus.AMFICOM.client.UI;

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
