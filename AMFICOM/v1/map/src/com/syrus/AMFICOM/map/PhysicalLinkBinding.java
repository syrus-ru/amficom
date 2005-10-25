/*-
 * $Id: PhysicalLinkBinding.java,v 1.20 2005/10/25 07:46:27 krupenn Exp $
 *
 * Copyright њ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * ќбъект прив€зки кабелей к тоннелю. ѕринадлежит определенному тоннелю.
 * включает всеб€ список кабелей, которые проход€т по данному тоннелю,
 * и матрицу пролегани€ кабелей по трубам тоннел€.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/10/25 07:46:27 $
 * @module map
 */
public final class PhysicalLinkBinding implements Serializable {
	private static final long serialVersionUID = -6384653393124814845L;

	private Set<Identifier> pipeBlockIds;

	private transient SortedSet<PipeBlock> pipeBlocks;

	/** список кабелей, проложенных по данному тоннелю */
	private transient ArrayList<Object> bindObjects = new ArrayList<Object>();

	private void initialize() {
		if(this.bindObjects == null) {
			this.bindObjects = new ArrayList<Object>();
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
	 *  онструктор.
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
	 * ƒобавить кабель в тоннель.
	 * 
	 * @param object
	 *        кабель (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void add(final Object object) {
		this.initialize();
		final int index = this.bindObjects.indexOf(object);
		if (index == -1)
			this.bindObjects.add(object);
	}
	
	/**
	 * ”далить кабель из тоннел€.
	 * @param object кабель (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
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
	 * ”далить все кабели из тоннел€.
	 */
	public void clear() {
		this.initialize();
		this.bindObjects.clear();

		for(PipeBlock pipeBlock : this.pipeBlocks) {
			pipeBlock.clear();
		}
	}
	
	/**
	 * ѕолучить список кабелей.
	 * 
	 * @return список кабелей
	 */
	public List<Object> getBindObjects() {
		this.initialize();
		return new LinkedList(this.bindObjects);
	}
	
	/**
	 * ѕроверить, определено ли место прохождени€ кабел€ в тоннеле.
	 * 
	 * @param object
	 *        кабель
	 * @return <code>true</code>, если место кабел€ определено,
	 *         <code>false</code> иначе
	 */
	public boolean isBound(final Object object) {
		this.initialize();
		final int index = this.bindObjects.indexOf(object);
		return (index >= 0);
	}

	/**
	 * Gолучить координаты трубы, по которой проходит кабель.
	 * 
	 * @param object
	 *        кабель
	 * @return координаты прохождени€ кабел€, или <code>null</code>, если место
	 *         кабел€ не задано
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
