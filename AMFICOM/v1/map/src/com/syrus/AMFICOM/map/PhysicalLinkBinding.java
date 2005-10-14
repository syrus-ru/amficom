/*-
 * $Id: PhysicalLinkBinding.java,v 1.17 2005/10/14 11:57:19 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ������ �������� ������� � �������. ����������� ������������� �������.
 * �������� ����� ������ �������, ������� �������� �� ������� �������,
 * � ������� ���������� ������� �� ������ �������.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/10/14 11:57:19 $
 * @module map
 */
public final class PhysicalLinkBinding implements Serializable {
	private static final long serialVersionUID = -6384653393124814845L;

	private Set<PipeBlock> pipeBlocks;

	/** ������ �������, ����������� �� ������� ������� */
	private transient ArrayList<Object> bindObjects = new ArrayList<Object>();

	private void initialize() {
		if(this.bindObjects == null) {
			this.bindObjects = new ArrayList<Object>();
		}		
	}

	/**
	 * �����������.
	 */
	public PhysicalLinkBinding(
			Collection<PipeBlock> pipeBlocks) {
		this.pipeBlocks = new HashSet(pipeBlocks);
	}

	/**
	 * �������� ������ � �������.
	 * 
	 * @param object
	 *        ������ (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void add(final Object object) {
		this.initialize();
		final int index = this.bindObjects.indexOf(object);
		if (index == -1)
			this.bindObjects.add(object);
	}
	
	/**
	 * ������� ������ �� �������.
	 * @param object ������ (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void remove(final Object object) {
		this.initialize();
		if (object != null) {
			this.bindObjects.remove(object);
			for(PipeBlock pipeBlock : this.pipeBlocks) {
				pipeBlock.remove(object);
			}
		}
	}

	/**
	 * ������� ��� ������ �� �������.
	 */
	public void clear() {
		this.initialize();
		this.bindObjects.clear();

		for(PipeBlock pipeBlock : this.pipeBlocks) {
			pipeBlock.clear();
		}
	}
	
	/**
	 * �������� ������ �������.
	 * 
	 * @return ������ �������
	 */
	public List<Object> getBindObjects() {
		this.initialize();
		return new LinkedList(this.bindObjects);
	}
	
	/**
	 * ���������, ���������� �� ����� ����������� ������ � �������.
	 * 
	 * @param object
	 *        ������
	 * @return <code>true</code>, ���� ����� ������ ����������,
	 *         <code>false</code> �����
	 */
	public boolean isBound(final Object object) {
		this.initialize();
		final int index = this.bindObjects.indexOf(object);
		return (index >= 0);
	}

	/**
	 * G������� ���������� �����, �� ������� �������� ������.
	 * 
	 * @param object
	 *        ������
	 * @return ���������� ����������� ������, ��� <code>null</code>, ���� �����
	 *         ������ �� ������
	 */
	public PipeBlock getPipeBlock(final Object object) {
		this.initialize();
		final int index = this.bindObjects.indexOf(object);
		if (index == -1) {
			return null;
		}
		for(PipeBlock pipeBlock : this.pipeBlocks) {
			if(pipeBlock.isBound(object)) {
				return pipeBlock;
			}
		}
		return null;
	}

	public void addPipeBlock(PipeBlock pipeBlock) {
		this.pipeBlocks.add(pipeBlock);
	}

	public Set<PipeBlock> getPipeBlocks() {
		return this.pipeBlocks;
	}

	public void setPipeBlocks(Set<PipeBlock> pipeBlocks) {
		this.pipeBlocks = pipeBlocks;
	}

	public void removePipeBlock(PipeBlock block) {
		this.pipeBlocks.remove(block);
	}
}
