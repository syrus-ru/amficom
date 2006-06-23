/*-
 * $Id: PhysicalLinkBinding.java,v 1.22 2006/06/23 13:39:17 stas Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * ������ �������� ������� � �������. ����������� ������������� �������.
 * �������� ����� ������ �������, ������� �������� �� ������� �������,
 * � ������� ���������� ������� �� ������ �������.
 *
 * @author $Author: stas $
 * @version $Revision: 1.22 $, $Date: 2006/06/23 13:39:17 $
 * @module map
 */
public final class PhysicalLinkBinding implements Serializable {
	private static final long serialVersionUID = -6384653393124814845L;

	private Set<Identifier> pipeBlockIds;

	private transient SortedSet<PipeBlock> pipeBlocks;

	/** ������ �������, ����������� �� ������� ������� */
	private transient Set<MapElement> bindObjects;

	private void initialize() {
		if (this.bindObjects == null) {
			this.bindObjects  = new HashSet<MapElement>();
		}
		
		if(this.pipeBlocks == null) {
			this.pipeBlocks = new TreeSet<PipeBlock>();
			final Set<PipeBlock> pipeBlocksFromPool;
			try {
				pipeBlocksFromPool = StorableObjectPool.<PipeBlock>getStorableObjects(this.pipeBlockIds, true);
				this.pipeBlocks.addAll(pipeBlocksFromPool);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �����������.
	 */
	public PhysicalLinkBinding(
			Collection<PipeBlock> pipeBlocks) {
		this.pipeBlocks = new TreeSet<PipeBlock>();
		this.pipeBlockIds = new HashSet<Identifier>();
		if(pipeBlocks != null) {
			this.pipeBlocks.addAll(pipeBlocks);
			this.pipeBlockIds.addAll(Identifier.createIdentifiers(this.pipeBlocks));
		}
	}

	/**
	 * �������� ������ � �������.
	 * 
	 * @param object ������ (CablePath)
	 */
	public void add(final MapElement object) {
		this.initialize();
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
	public Set<MapElement> getBindObjects() {
		return Collections.unmodifiableSet(this.bindObjects);
	}
	
	/**
	 * ���������, ���������� �� ����� ����������� ������ � �������.
	 * 
	 * @param mapElement
	 *        ������
	 * @return <code>true</code>, ���� ����� ������ ����������,
	 *         <code>false</code> �����
	 */
	public boolean isBound(final MapElement mapElement) {
		return this.bindObjects.contains(mapElement);
	}

	/**
	 * G������� ���������� �����, �� ������� �������� ������.
	 * 
	 * @param mapElement ������
	 * @return ���������� ����������� ������, ��� <code>null</code>, ���� �����
	 *         ������ �� ������
	 */
	public PipeBlock getPipeBlock(final MapElement mapElement) {
		this.initialize();
		if (this.bindObjects.contains(mapElement)) {
			for(PipeBlock pipeBlock : this.pipeBlocks) {
				if(pipeBlock.isBound(mapElement)) {
					return pipeBlock;
				}
			}
		}
		return null;
	}

	public void addPipeBlock(PipeBlock pipeBlock) {
		pipeBlock.setNumber(this.pipeBlocks.size() + 1);
		this.pipeBlocks.add(pipeBlock);
		this.pipeBlockIds.add(pipeBlock.getId());
	}

	public SortedSet<PipeBlock> getPipeBlocks() {
		return Collections.unmodifiableSortedSet(this.pipeBlocks);
	}

	public void setPipeBlocks(Set<PipeBlock> pipeBlocks) {
		this.pipeBlockIds.clear();
		this.pipeBlocks.clear();
		if(pipeBlocks != null) {
			this.pipeBlocks.addAll(pipeBlocks);
			this.pipeBlockIds.addAll(Identifier.createIdentifiers(this.pipeBlocks));
		}
	}

	public void removePipeBlock(PipeBlock block) {
		final int removedNumber = block.getNumber();
		final SortedSet<PipeBlock> blocksToShift = this.pipeBlocks.tailSet(block);
		block.setNumber(-1);
		this.pipeBlocks.remove(block);
		this.pipeBlockIds.remove(block.getId());
		for(PipeBlock pipeBlock : blocksToShift) {
			final int number = pipeBlock.getNumber();
			if(number > removedNumber) {
				pipeBlock.setNumber(number - 1);
			}
		}
	}
}
