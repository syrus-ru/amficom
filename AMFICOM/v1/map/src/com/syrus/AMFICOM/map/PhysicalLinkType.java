/**
 * $Id: PhysicalLinkType.java,v 1.16 2005/01/27 14:43:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Тип линии топологической схемы. Существует несколько предустановленных 
 * типов линий, которые определяются полем {@link #codename}, соответствующим
 * какому-либо значению {@link #TUNNEL}, {@link #COLLECTOR}, {@link #INDOOR}, 
 * {@link #SUBMARINE}, {@link #OVERHEAD}, {@link #UNBOUND}
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/01/27 14:43:37 $
 * @module map_v1
 */
public class PhysicalLinkType extends StorableObjectType implements Characterized {

	/** тоннель */
	public static final String TUNNEL = "tunnel";
	/** участок коллектора */
	public static final String COLLECTOR = "collector";
	/** внутренняя проводка */
	public static final String INDOOR = "indoor";
	/** подводная линия */
	public static final String SUBMARINE = "submarine";
	/** навесная линия */
	public static final String OVERHEAD = "overhead";
	/** непривязанный кабель */
	public static final String UNBOUND = "cable";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3690191057812271924L;

	private List					characteristics;
	

	private String					name;

	/**
	 * Размерность тоннеля.
	 * Для тоннеля обозначает размерность матрицы труб в разрезе,
	 * для участка коллектора - число полок и мест на полках
	 * @todo добавить сохранение в БД
	 */
	private IntDimension 			bindingDimension;

	private StorableObjectDatabase	physicalLinkTypeDatabase;
	
	public PhysicalLinkType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			this.physicalLinkTypeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLinkType(PhysicalLinkType_Transferable pltt) throws CreateObjectException {
		super(pltt.header, pltt.codename, pltt.description);
		this.name = pltt.name;

		try {
			this.characteristics = new ArrayList(pltt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(pltt.characteristicIds.length);
			for (int i = 0; i < pltt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(pltt.characteristicIds[i]));

			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PhysicalLinkType(final Identifier id,
						   final Identifier creatorId,
						   final String codename,
						   final String name,
						   final String description,
						   final IntDimension bindingDimension) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		this.name = name;
		this.description = description;
		this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
	}

	public void insert() throws CreateObjectException {
		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			if (this.physicalLinkTypeDatabase != null)
				this.physicalLinkTypeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static PhysicalLinkType createInstance(
			final Identifier creatorId,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension) throws CreateObjectException {
		
		if (creatorId == null || codename == null || name == null || description == null || bindingDimension == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE),
				creatorId,
				codename,
				name,
				description,
				bindingDimension);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("PhysicalLinkType.createInstance | cannot generate identifier ", e);
		}
	}

	public List getCharacteristics() {
		return  Collections.unmodifiableList(this.characteristics);
	}
	
	public void addCharacteristic(Characteristic characteristic){
		this.characteristics.add(characteristic);
		super.currentVersion = super.getNextVersion();
	}

	public void removeCharacteristic(Characteristic characteristic)
	{
		this.characteristics.remove(characteristic);
		super.currentVersion = super.getNextVersion();
	}

	public List getDependencies() {
		return Collections.unmodifiableList(this.characteristics);
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
		
		return new PhysicalLinkType_Transferable(super.getHeaderTransferable(), 
				this.codename,
				this.name,
				this.description,
				this.bindingDimension.getWidth(),
				this.bindingDimension.getHeight(),
				charIds);
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public String getDescription() {
		return this.description;
	}
	
	protected void setDescription0(String description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.setDescription0(description);
		super.currentVersion = super.getNextVersion();
	}

	public String getName() {
		return this.name;
	}
	
	protected void setName0(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.setName0(name);
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  String codename,
											  String name,
											  String description,
											  int width,
											  int height) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					codename,
					description);
			this.bindingDimension = new IntDimension(width, height);
			this.name = name;
		}
	
	protected void setBindingDimension0(IntDimension bindingDimension){
		this.bindingDimension = new IntDimension(
				bindingDimension.getWidth(), 
				bindingDimension.getHeight());
	}
	
	public void setBindingDimension(IntDimension bindingDimension){
		this.setBindingDimension0(bindingDimension);
		super.currentVersion = super.getNextVersion();
	}


	public IntDimension getBindingDimension(){
		return new IntDimension(this.bindingDimension);
	}

	protected void setCodename0(String codename){
		super.setCodename0(codename);
	}
}
