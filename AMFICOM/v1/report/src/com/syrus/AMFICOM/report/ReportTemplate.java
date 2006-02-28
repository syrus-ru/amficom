/*
 * $Id: ReportTemplate.java,v 1.28 2006/02/28 15:19:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTIMAGE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTABLEDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
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
 * @author $Author: arseniy $
 * @version $Revision: 1.28 $, $Date: 2006/02/28 15:19:59 $
 * @module report
 */
public class ReportTemplate extends StorableObject<ReportTemplate> implements Namable, Describable, ReverseDependencyContainer,
		Cloneable {
	private static final long serialVersionUID = 6270406142449624592L;

	public enum Orientation {
		PORTRAIT, LANDSCAPE
	}

	public static final int STANDART_MARGIN_SIZE = 60;

	//Это хранимое поле	
	private String name;
	//Это хранимое поле	
	private String description;

	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private SheetSize sheetSize;
	/**
	 * Размер шаблона (его ширина)
	 */
	private Orientation orientation;
	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private int marginSize;

	//Это хранимое поле
	/**
	 * Принадлежность шаблона к модулю
	 */
	private String destinationModule;

	private transient Set<StorableElement> storableElementsToRemove;

	private transient boolean isNew = false;

	private transient LinkedIdsCondition	attTextCondition;
	private transient LinkedIdsCondition	imageCondition;
	private transient LinkedIdsCondition	dataCondition;
	private transient LinkedIdsCondition	tableDataCondition;

	//Приходится хранить элементы в разных списках, несмотря на то, что они
	//все наследуют StorableElement, поскольку при загрузке (импорте) важно,
	//чтобы сначала были подгружены элементы хранения данных, а уже потом -
	//надписи к ним привязанные.
	//Это хранимое поле	
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

	public static ReportTemplate createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String destinationModule) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert destinationModule != null && destinationModule.length() != 0 : NON_EMPTY_EXPECTED;		
		try {
			final Date created = new Date();
			final ReportTemplate reportTemplate = new ReportTemplate(
					IdentifierPool.getGeneratedIdentifier(REPORTTEMPLATE_CODE),
					created,
					created,
					creatorId,
					creatorId,
					INITIAL_VERSION,
					name,
					description,
					SheetSize.A4,
					Orientation.PORTRAIT,
					STANDART_MARGIN_SIZE,
					destinationModule);
			StorableObjectPool.putStorableObject(reportTemplate);
			return reportTemplate;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"AttachedTextStorableElement.createInstance() | cannot generate identifier ", ige);
		}
		catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"AttachedTextStorableElement.createInstance() | error while putting in Pool ", ioee);
		}		
	}

	public ReportTemplate(final IdlReportTemplate transferable) {
		fromTransferable(transferable);
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) {
		final IdlReportTemplate irt = (IdlReportTemplate) transferable;
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
	public IdlStorableObject getIdlTransferable(final ORB orb) {
		return IdlReportTemplateHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
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
	public AbstractDataStorableElement findStorableElementForName(final String rteName) {
		TypicalCondition typicalCondition = new TypicalCondition(rteName,
				OPERATION_EQUALS,
				REPORTDATA_CODE,
				StorableObjectWrapper.COLUMN_NAME);
		LinkedIdsCondition linkedCondition = new LinkedIdsCondition(this.getId(), REPORTDATA_CODE);
		CompoundCondition condition = new CompoundCondition(typicalCondition, AND, linkedCondition);
		Set<AbstractDataStorableElement> abstractDataElements = null;
		try {
			abstractDataElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
		if (abstractDataElements.size() > 1) {
			Log.errorMessage("Error: to many of dataStorableElements");
		}
		if (!abstractDataElements.isEmpty()) {
			return abstractDataElements.iterator().next();
		}
		return null;
	}

	/**
	 * @param abstractDataStorableElement Объект отображения данных
	 * @return Список надписей, привязанных к данному объекту отображения
	 */
	public Set<AttachedTextStorableElement> getAttachedTextStorableElements(final AbstractDataStorableElement abstractDataStorableElement) {
		final LinkedIdsCondition condition1 = new LinkedIdsCondition(this.getId(), ATTACHEDTEXT_CODE);
		final LinkedIdsCondition condition2 = new LinkedIdsCondition(abstractDataStorableElement.getId(), ATTACHEDTEXT_CODE);
		final CompoundCondition condition = new CompoundCondition(condition1, AND, condition2);
		try {
			return StorableObjectPool.getStorableObjectsByCondition(condition, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return Collections.emptySet();
		}
	}

	/**
	 * @return Возвращает границы прямоугольника, в который
	 * вписывается схематичное изображение элемента шаблона и
	 * привязанные к нему надписи.
	 * @throws ApplicationException 
	 */
	public Rectangle getElementClasterBounds(final AbstractDataStorableElement<?> abstractDataStorableElement)
			throws ApplicationException {
		if (abstractDataStorableElement == null) {
			throw new AssertionError("The claster bounds can't be calculated for the null element!");
		}

		int x1 = abstractDataStorableElement.getLocation().x;
		int x2 = x1 + abstractDataStorableElement.getWidth();
		int y1 = abstractDataStorableElement.getLocation().y;
		int y2 = y1 + abstractDataStorableElement.getHeight();

		final Set<AttachedTextStorableElement> textStorableElements = getAttachedTextStorableElements();
		if (textStorableElements.isEmpty()) {
			return new Rectangle(x1, y1, x2 - x1, y2 - y1);
		}

		for (final AttachedTextStorableElement textStorableElement : textStorableElements) {
			final AbstractDataStorableElement vertAttacher = textStorableElement.getVerticalAttacher();
			final AbstractDataStorableElement horizAttacher = textStorableElement.getHorizontalAttacher();
			if (!((vertAttacher != null) && vertAttacher.equals(abstractDataStorableElement) || (horizAttacher != null)
					&& horizAttacher.equals(abstractDataStorableElement))) {
				continue;
			}

			final int labelX = textStorableElement.getLocation().x;
			final int labelY = textStorableElement.getLocation().y;
			final int labelWidth = textStorableElement.getWidth();
			final int labelHeight = textStorableElement.getHeight();

			if (labelX < x1) {
				x1 = labelX;
			}

			if (labelX + labelWidth > x2) {
				x2 = labelX + labelWidth;
			}

			if (labelY < y1) {
				y1 = labelY;
			}

			if (labelY + labelHeight > y2) {
				y2 = labelY + labelHeight;
			}
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
	public boolean clasterContainsPoint(final AbstractDataStorableElement dataStorableElement, final int x, final int y)
			throws ApplicationException {
		final Rectangle bounds = this.getElementClasterBounds(dataStorableElement);
		return bounds.contains(x, y);
	}

	public AbstractDataStorableElement getDataRElement(final Identifier dreId) {
		try {
			return StorableObjectPool.getStorableObject(dreId, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Set<AbstractDataStorableElement> getDataStorableElements() throws ApplicationException {
		return this.getDataStorableElements(true);
	}

	public Set<AbstractDataStorableElement> getDataStorableElements(final boolean usePool) throws ApplicationException {
		if (this.dataCondition == null) {
			this.dataCondition = new LinkedIdsCondition(this.getId(), REPORTDATA_CODE);
		}
		if (this.tableDataCondition == null) {
			this.tableDataCondition = new LinkedIdsCondition(this.getId(), REPORTTABLEDATA_CODE);
		}
		final Set<AbstractDataStorableElement> dataSet = new HashSet<AbstractDataStorableElement>();
		dataSet.addAll(StorableObjectPool.<DataStorableElement> getStorableObjectsByCondition(this.dataCondition, usePool));
		dataSet.addAll(StorableObjectPool.<TableDataStorableElement> getStorableObjectsByCondition(this.tableDataCondition, usePool));
		return dataSet;
	}

	public Set<ImageStorableElement> getImageStorableElements() throws ApplicationException {
		return this.getImageStorableElements(true);
	}

	public Set<ImageStorableElement> getImageStorableElements(final boolean usePool) throws ApplicationException {
		if (this.imageCondition == null) {
			this.imageCondition = new LinkedIdsCondition(this.getId(), REPORTIMAGE_CODE);
		}
		return StorableObjectPool.getStorableObjectsByCondition(this.imageCondition, usePool);
	}

	public Set<AttachedTextStorableElement> getAttachedTextStorableElements() throws ApplicationException {
		return this.getAttachedTextStorableElements(true);
	}

	public Set<AttachedTextStorableElement> getAttachedTextStorableElements(final boolean usePool) throws ApplicationException {
		if (this.attTextCondition == null) {
			this.attTextCondition = new LinkedIdsCondition(this.getId(), ATTACHEDTEXT_CODE);
		}
		return StorableObjectPool.getStorableObjectsByCondition(this.attTextCondition, usePool);
	}

	public String getName() {
		return this.name;
	}

	public IntDimension getSize() {
		return this.sheetSize.getSize();
	}

	public String getDestinationModule() {
		return this.destinationModule;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public int getMarginSize() {
		return this.marginSize;
	}

	public void setMarginSize(final int marginSize) {
		this.marginSize = marginSize;
		super.markAsChanged();
	}

	public boolean doObjectsIntersect() {
		return false;
	}

	public void setDestinationModule(final String destinationModule) {
		this.destinationModule = destinationModule;
		super.markAsChanged();
	}

	public void setSize(final SheetSize sheetSize) {
		this.sheetSize = sheetSize;
		super.markAsChanged();
	}

	public Orientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(final Orientation orientation) {
		this.orientation = orientation;
		super.markAsChanged();
	}

	public void addElement(final StorableElement element) {
		element.setReportTemplateId(this.getId());
		super.markAsChanged();
	}

	public void removeElement(final StorableElement element) {
		if (this.storableElementsToRemove == null) {
			this.storableElementsToRemove = new HashSet<StorableElement>();
		}
		this.storableElementsToRemove.add(element);
		element.setReportTemplateId(Identifier.VOID_IDENTIFIER);
		super.markAsChanged();
	}

	SheetSize getSheetSize() {
		return this.sheetSize;
	}

	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.addAll(getAttachedTextStorableElements(usePool));
		reverseDependencies.addAll(getDataStorableElements(usePool));
		reverseDependencies.addAll(getImageStorableElements(usePool));
		if (this.storableElementsToRemove != null) {
			reverseDependencies.addAll(this.storableElementsToRemove);
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(Identifier.VOID_IDENTIFIER);
		return reverseDependencies;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	public boolean isNew() {
		return this.isNew;
	}

	public void setNew(final boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ReportTemplateWrapper getWrapper() {
		return ReportTemplateWrapper.getInstance();
	}

	@Override
	public ReportTemplate clone() throws CloneNotSupportedException {
		final ReportTemplate clone = super.clone();
		clone.name = this.name;
		clone.description = this.description;
		clone.sheetSize = this.sheetSize;
		clone.orientation = this.orientation;
		clone.marginSize = this.marginSize;
		clone.destinationModule = this.destinationModule;

		clone.storableElementsToRemove = null;

		clone.isNew = false;

		clone.attTextCondition = null;
		clone.imageCondition = null;
		clone.dataCondition = null;
		clone.tableDataCondition = null;

		try {
			final Set<AttachedTextStorableElement> attText = this.getAttachedTextStorableElements(false);
			for (final AttachedTextStorableElement attTextElement : attText) {
				final AttachedTextStorableElement clonedAttText = attTextElement.clone();
				clone.addElement(clonedAttText);
				final AbstractDataStorableElement horizontalData = clonedAttText.getHorizontalAttacher();
				if (horizontalData != null) {
					clone.addElement(horizontalData);
				}

				final AbstractDataStorableElement verticalData = clonedAttText.getVerticalAttacher();
				if (verticalData != null) {
					clone.addElement(verticalData);
				}
			}

			final Set<ImageStorableElement> images = this.getImageStorableElements(false);
			for (final ImageStorableElement imageElement : images) {
				clone.addElement(imageElement.clone());
			}

			final Set<AttachedTextStorableElement> qwe1 = this.getAttachedTextStorableElements();
			final Set<AttachedTextStorableElement> qwe2 = clone.getAttachedTextStorableElements();
			System.out.println(qwe1);
			System.out.println(qwe2);

		} catch (ApplicationException e) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(e);
			throw cnse;
		}

		return clone;
	}
}
