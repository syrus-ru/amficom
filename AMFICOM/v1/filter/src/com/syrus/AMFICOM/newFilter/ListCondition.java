/*
 * $Id: ListCondition.java,v 1.4 2005/08/09 22:00:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/09 22:00:24 $
 * @author $Author: arseniy $
 * @module filter
 */
public class ListCondition {
	
	
	private int[] selectedIndices;
	private String[] linkedNames;
	
	public ListCondition() {
		this.selectedIndices = new int[0];		
	}
	
	public ListCondition(String[] linkedNames) {
		this.selectedIndices = new int[0];
		this.linkedNames = linkedNames;
	}
	
	public ListCondition(int[] selectedIndices, String[] linkedNames) {
		this.selectedIndices = selectedIndices;
		this.linkedNames = linkedNames;
	}
	
	public void setSelectedIndices(int[] indices) {
		this.selectedIndices = indices;
	}
	public String[] getSelectedNames() {
		return this.linkedNames;
	}
	public String[] getLinkedNames() {
		return this.linkedNames;
	}
	public void setLinkedNames(String[] linkedNames) {
		this.linkedNames = linkedNames;
	}
	public int[] getSelectedIndices() {
		return this.selectedIndices;
	}
}
