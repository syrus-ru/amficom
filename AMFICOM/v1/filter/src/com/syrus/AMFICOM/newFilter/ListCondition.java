/*
 * $Id: ListCondition.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class ListCondition {
	
	private int[] linkedIndex;
	private String[] linkedNames;
	
	
	public ListCondition(int[] linkedIndex, String[] linkedNames) {
		this.linkedIndex = linkedIndex;
		this.linkedNames = linkedNames;
	}
	
	public int[] getLinkedIndex() {
		return this.linkedIndex;
	}
	
	public void setLinkedIndex(int[] linkedIndex) {
		this.linkedIndex = linkedIndex;
	}
	
	public String[] getLinkedNames() {
		return this.linkedNames;
	}
	
	public void setLinkedNames(String[] linkedNames) {
		this.linkedNames = linkedNames;
	}
}
