/*
 * $Id: ListCondition.java,v 1.5 2005/08/25 10:30:33 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/25 10:30:33 $
 * @author $Author: max $
 * @module filter
 */
public class ListCondition implements TemporalCondition {
	
	
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
	
	public boolean isEmpty() {
		if(this.selectedIndices.length == 0) {
			return true;
		}
		return false;
	}
}
