/*
 * $Id: ListCondition.java,v 1.2 2005/03/25 10:29:31 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/25 10:29:31 $
 * @author $Author: max $
 * @module misc
 */
public class ListCondition {
	
	private int[] selectedIndices;
	private String[] linkedNames;
	
	public ListCondition(String[] linkedNames) {
		this.selectedIndices = new int[0];
		this.linkedNames = linkedNames;
	}
	
	public ListCondition(int[] selectedIndices, String[] linkedNames) {
		this.selectedIndices = selectedIndices;
		this.linkedNames = linkedNames;
	}
	
	public int[] getLinkedIndex() {
		return this.selectedIndices;
	}
	
	public void setSelectedIndices(int[] indices) {
		this.selectedIndices = indices;
	}
	
	public String[] getSelectedNames() {
		return this.linkedNames;
	}
}
