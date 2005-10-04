/*
 * $Id: ReportTemplate.java,v 1.11 2005/10/04 11:03:53 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.report.corba.IdlReportTemplate;
import com.syrus.AMFICOM.report.corba.IdlReportTemplateHelper;
import com.syrus.AMFICOM.report.corba.IdlReportTemplatePackage.IdlOrientation;
import com.syrus.AMFICOM.report.corba.IdlReportTemplatePackage.IdlSheetSize;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * <p>Класс шаблона отчёта - включает в себя списки элементов, надписей и
 * фильтров, используемых в нём, а также средства их сериализации и подготовки
 * к передаче по CORBA. Также включает ряд служебных функций.</p>
 * 
 * <p>Тип шаблона характеризует из какого модуля по нему можно построить
 * отчёт </p>
 * 
 * @author $Author: max $
 * @version $Revision: 1.11 $, $Date: 2005/10/04 11:03:53 $
 * @module generalclient_v1
 */
public class ReportTemplate extends StorableObject implements Namable, Describable {
	private static final long serialVersionUID = 6270406142449624592L;
	
//	public static final IntDimension A0 = new IntDimension (3360, 4760);
//	public static final IntDimension A1 = new IntDimension (3360, 2380);
//	public static final IntDimension A2 = new IntDimension (1680, 2380);
//	public static final IntDimension A3 = new IntDimension (1680, 1190);
//	public static final IntDimension A4 = new IntDimension (840, 1190);

	public enum Orientation {PORTRAIT,LANDSCAPE}
	
	public static final int STANDART_MARGIN_SIZE = 60;

	//Это хранимое поле	
	private String name = "";
	//Это хранимое поле	
	private String description = "";

	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private SheetSize sheetSize = SheetSize.A4;
	/**
	 * Размер шаблона (его ширина)
	 */
	private Orientation orientation = Orientation.PORTRAIT;
	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private int marginSize = ReportTemplate.STANDART_MARGIN_SIZE;
	
	//Это хранимое поле
	/**
	 * Принадлежность шаблона к модулю
	 */
	private String destinationModule = DestinationModules.UNKNOWN_MODULE;

	private LinkedIdsCondition	attTextCondition;

	private LinkedIdsCondition	imageCondition;

	private StorableObjectCondition	dataCondition;

	//Приходится хранить элементы в разных списках, несмотря на то, что они
	//все наследуют StorableElement, поскольку при загрузке (импорте) важно,
	//чтобы сначала были подгружены элементы хранения данных, а уже потом -
	//надписи к ним привязанные.
	//Это хранимое поле	
	/**
	 * Список всех элементов шаблона
	 */
//	private Set<Identifier> dataStorableElementIds = new HashSet<Identifier>();
//	//Это хранимое поле	
//	/**
//	 * Список фильтров использующихся в шаблоне
//	 */
//	private List objectResourceFilters = new ArrayList();
	//Это хранимое поле	
	/**
	 * Список надписей из шаблона
	 */
//	private Set<Identifier> textStorableElementIds = new HashSet<Identifier>();
	//Это хранимое поле
	/**
	 * Список картинок из шаблона
	 */
//	private Set<Identifier> imageStorableElementIds = new HashSet<Identifier>();

	ReportTemplate(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final SheetSize size,
			final Orientation orientation,
			final int marginSize,
			final String destinationModule) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.sheetSize = size;
		this.orientation = orientation;
		this.marginSize = marginSize;
		this.destinationModule = destinationModule;
	}
	
	public static ReportTemplate createInstance(Identifier creatorId) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		try {
			final Date created = new Date();
			final ReportTemplate reportTemplate = new ReportTemplate(
					IdentifierPool.getGeneratedIdentifier(ATTACHEDTEXT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					"",
					"",
					SheetSize.A4,
					Orientation.PORTRAIT,
					STANDART_MARGIN_SIZE,
					DestinationModules.UNKNOWN_MODULE);
			reportTemplate.markAsChanged();
			return reportTemplate;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"AttachedTextStorableElement.createInstance() | cannot generate identifier ", ige);
		}
	}
	
	public ReportTemplate(IdlReportTemplate transferable) {
		fromTransferable(transferable);
	}
	
	@Override
	protected synchronized void fromTransferable(IdlStorableObject transferable) {
		IdlReportTemplate irt = (IdlReportTemplate) transferable;
		try {
			super.fromTransferable(transferable);
		} catch (ApplicationException e) {
			// this shit cann't happen
			assert false;
		}
		this.name = irt.name;
		this.description = irt.description;
		this.sheetSize = SheetSize.values()[irt.sheetSize.value()];
		this.orientation = Orientation.values()[irt.idlOrientation.value()];
		this.marginSize = irt.marginSize;
		this.destinationModule = irt.destinationModule;
	}
	
	@Override
	public IdlStorableObject getTransferable(ORB orb) {
		return IdlReportTemplateHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				IdlSheetSize.from_int(this.sheetSize.ordinal()),
				IdlOrientation.from_int(this.orientation.ordinal()),
				this.marginSize,
				this.destinationModule);
	}
	
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final SheetSize size,
			final Orientation orientation,
			final int marginSize,
			final String destinationModule) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.sheetSize = size;
		this.orientation = orientation;
		this.marginSize = marginSize;
		this.destinationModule = destinationModule;
	}
	
	/**
	 * @param rteName искомое имя
	 * @return элемент шаблона, отображающий искомый отчёт
	 */
	public DataStorableElement findStorableElementForName (String rteName) {
		TypicalCondition typicalCondition = new TypicalCondition(rteName, OperationSort.OPERATION_EQUALS, ObjectEntities.REPORTDATA_CODE, StorableObjectWrapper.COLUMN_NAME);
		LinkedIdsCondition linkedCondition = new LinkedIdsCondition(this.getId(), ObjectEntities.REPORTDATA_CODE);
		CompoundCondition condition = new CompoundCondition(typicalCondition, CompoundConditionSort.AND, linkedCondition);
		Set<DataStorableElement> dataElements = null;
		try {
			dataElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
		if (dataElements.size() > 1) {
			Log.errorMessage("ReportTemplate.findStorableElementForName | Error: to many of dataStorableElements");
		}
		if (!dataElements.isEmpty()) {
			return dataElements.iterator().next();
		}
		return null;
	}

	/**
	 * @param dataStorableElement Объект отображения данных
	 * @return Список надписей, привязанных к данному объекту отображения
	 */
	public Set<AttachedTextStorableElement> getAttachedTextStorableElements (DataStorableElement dataStorableElement)
	{
		LinkedIdsCondition condition1 = new LinkedIdsCondition(this.getId(), ObjectEntities.ATTACHEDTEXT_CODE);
		LinkedIdsCondition condition2 = new LinkedIdsCondition(dataStorableElement.getId(), ObjectEntities.ATTACHEDTEXT_CODE);
		CompoundCondition condition = new CompoundCondition(condition1, CompoundConditionSort.AND, condition2);
		try {
			return StorableObjectPool.getStorableObjectsByCondition(condition, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return Collections.emptySet();
		}
	}
	
	/**
	 * @return Возвращает границы прямоугольника, в который
	 * вписывается схематичное изображение элемента шаблона и
	 * привязанные к нему надписи.
	 * @throws ApplicationException 
	 */
	public Rectangle getElementClasterBounds(DataStorableElement dataStorableElement) throws ApplicationException
	{
		if (dataStorableElement == null)
			throw new AssertionError("The claster bounds can't be calculated for the null element!");
		
		int x1 = dataStorableElement.getLocation().x;
		int x2 = x1 + dataStorableElement.getWidth();		
		int y1 = dataStorableElement.getLocation().y;
		int y2 = y1 + dataStorableElement.getHeight();

		Set<AttachedTextStorableElement> textStorableElements = getAttachedTextStorableElements();
		if (textStorableElements.isEmpty()) {
			return new Rectangle(x1,y1,x2 - x1,y2 - y1);
		}
		
		for (AttachedTextStorableElement textStorableElement : textStorableElements) {
			DataStorableElement vertAttacher = textStorableElement.getVerticalAttacher();
			DataStorableElement horizAttacher = textStorableElement.getHorizontalAttacher();			
			if (!		(	(vertAttacher != null)
						&&	vertAttacher.equals(dataStorableElement)
					||	(horizAttacher != null)
						&&	horizAttacher.equals(dataStorableElement)))
				continue;

			int labelX = textStorableElement.getLocation().x;
			int labelY = textStorableElement.getLocation().y;
			int labelWidth = textStorableElement.getWidth();
			int labelHeight = textStorableElement.getHeight();
			
			if (labelX < x1)
				x1 = labelX;

			if (labelX + labelWidth > x2)
				x2 = labelX + labelWidth;

			if (labelY < y1)
				y1 = labelY;

			if (labelY + labelHeight > y2)
				y2 = labelY + labelHeight;
		}

		return new Rectangle(x1,y1,x2 - x1,y2 - y1);
	}

	/**
	 * Проверяет: принадлежит ли кластеру (элементу шаблона с привязанными надписями)
	 * данная точка.
	 * @param x координата точки по x
	 * @param y координата точки по y
	 * @return true если точка, принадлежит кластеру
	 * @throws ApplicationException 
	 */
	public boolean clasterContainsPoint (DataStorableElement dataStorableElement,int x, int y) throws ApplicationException
	{
		Rectangle bounds = this.getElementClasterBounds(dataStorableElement);
		return bounds.contains(x,y);
	}
	
	public DataStorableElement getDataRElement(Identifier dreId)
	{
		try {
			return StorableObjectPool.getStorableObject(dreId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<DataStorableElement> getDataStorableElements() throws ApplicationException {
		if(this.dataCondition == null) {
			this.dataCondition = new LinkedIdsCondition(this.getId(), ObjectEntities.REPORTDATA_CODE);
		}
		return StorableObjectPool.getStorableObjectsByCondition(this.dataCondition, true);
	}

	public Set<ImageStorableElement> getImageStorableElements() throws ApplicationException {
		if(this.imageCondition == null) {
			this.imageCondition = new LinkedIdsCondition(this.getId(), ObjectEntities.REPORTIMAGE_CODE);
		}
		return StorableObjectPool.getStorableObjectsByCondition(this.imageCondition, true);
	}

	public Set<AttachedTextStorableElement> getAttachedTextStorableElements() throws ApplicationException {
		if(this.attTextCondition == null) {
			this.attTextCondition = new LinkedIdsCondition(this.getId(), ObjectEntities.ATTACHEDTEXT_CODE);
		}
		return StorableObjectPool.getStorableObjectsByCondition(this.attTextCondition, true);
	}

	public String getName() {
		return this.name;
	}

//	public List getObjectResourceFilters() {
//		return this.objectResourceFilters;
//	}

	public IntDimension getSize() {
		return this.sheetSize.getSize();
	}

	public String getDestinationModule() {
		return this.destinationModule;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getMarginSize() {
		return this.marginSize;
	}

	public void setMarginSize(int marginSize) {
		this.marginSize = marginSize;
	}
	
	public boolean doObjectsIntersect(){
		return false;
	}

	public void setDestinationModule(String destinationModule) {
		this.destinationModule = destinationModule;
	}

	public void setSize(SheetSize sheetSize) {
		this.sheetSize = sheetSize;
	}

	public Orientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	public void addElement(StorableElement element) {
		element.setReportTemplateId(this.getId());
	}

	public void removeElement(StorableElement element) {
		element.setReportTemplateId(Identifier.VOID_IDENTIFIER);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		// TODO Auto-generated method stub
		return null;
	}
	
	SheetSize getSheetSize() {
		return this.sheetSize;
	}
}
