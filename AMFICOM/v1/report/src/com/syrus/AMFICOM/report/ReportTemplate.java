/*
 * $Id: ReportTemplate.java,v 1.16 2005/10/10 05:45:22 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTEMPLATE_CODE;

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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
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
 * <p>����� ������� ������ - �������� � ���� ������ ���������, �������� �
 * ��������, ������������ � ��, � ����� �������� �� ������������ � ����������
 * � �������� �� CORBA. ����� �������� ��� ��������� �������.</p>
 * 
 * <p>��� ������� ������������� �� ������ ������ �� ���� ����� ���������
 * ����� </p>
 * 
 * @author $Author: peskovsky $
 * @version $Revision: 1.16 $, $Date: 2005/10/10 05:45:22 $
 * @module generalclient_v1
 */
public class ReportTemplate extends StorableObject implements Namable, Describable, ReverseDependencyContainer {
	private static final long serialVersionUID = 6270406142449624592L;
	
	public enum Orientation {PORTRAIT,LANDSCAPE}
	
	public static final int STANDART_MARGIN_SIZE = 60;

	//��� �������� ����	
	private String name;
	//��� �������� ����	
	private String description;

	//��� �������� ����
	/**
	 * ������ ������� (��� ������)
	 */
	private SheetSize sheetSize;
	/**
	 * ������ ������� (��� ������)
	 */
	private Orientation orientation;
	//��� �������� ����
	/**
	 * ������ ������� (��� ������)
	 */
	private int marginSize;
	
	//��� �������� ����
	/**
	 * �������������� ������� � ������
	 */
	private String destinationModule;

	private LinkedIdsCondition	attTextCondition;
	private LinkedIdsCondition	imageCondition;
	private LinkedIdsCondition	dataCondition;
	private LinkedIdsCondition	tableDataCondition;

	//���������� ������� �������� � ������ �������, �������� �� ��, ��� ���
	//��� ��������� StorableElement, ��������� ��� �������� (�������) �����,
	//����� ������� ���� ���������� �������� �������� ������, � ��� ����� -
	//������� � ��� �����������.
	//��� �������� ����	
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
	
	public static ReportTemplate createInstance(
			final Identifier creatorId, 
			final String name, 
			final String description,
			final String destinationModule) throws 
			CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
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
					StorableObjectVersion.createInitial(),
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
	 * @param rteName ������� ���
	 * @return ������� �������, ������������ ������� �����
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
	 * @param dataStorableElement ������ ����������� ������
	 * @return ������ ��������, ����������� � ������� ������� �����������
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
	 * @return ���������� ������� ��������������, � �������
	 * ����������� ����������� ����������� �������� ������� �
	 * ����������� � ���� �������.
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
	 * ���������: ����������� �� �������� (�������� ������� � ������������ ���������)
	 * ������ �����.
	 * @param x ���������� ����� �� x
	 * @param y ���������� ����� �� y
	 * @return true ���� �����, ����������� ��������
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
		super.markAsChanged();
	}

	public Set<DataStorableElement> getDataStorableElements() throws ApplicationException {
		if(this.dataCondition == null) {
			this.dataCondition = new LinkedIdsCondition(this.getId(), ObjectEntities.REPORTDATA_CODE);
		}
		if(this.tableDataCondition == null) {
			this.tableDataCondition = new LinkedIdsCondition(this.getId(), ObjectEntities.REPORTTABLEDATA_CODE);
		}
		Set<DataStorableElement> dataSet = new HashSet<DataStorableElement>();
		dataSet.addAll(StorableObjectPool.<DataStorableElement>getStorableObjectsByCondition(this.dataCondition, true));
		dataSet.addAll(StorableObjectPool.<TableDataStorableElement>getStorableObjectsByCondition(this.tableDataCondition, true));
		return dataSet;
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

	public IntDimension getSize() {
		return this.sheetSize.getSize();
	}

	public String getDestinationModule() {
		return this.destinationModule;
	}
	
	public void setName(String name) {
		this.name = name;
		super.markAsChanged();
	}

	public int getMarginSize() {
		return this.marginSize;
	}

	public void setMarginSize(int marginSize) {
		this.marginSize = marginSize;
		super.markAsChanged();
	}
	
	public boolean doObjectsIntersect(){
		return false;
	}

	public void setDestinationModule(String destinationModule) {
		this.destinationModule = destinationModule;
		super.markAsChanged();
	}

	public void setSize(SheetSize sheetSize) {
		this.sheetSize = sheetSize;
		super.markAsChanged();
	}

	public Orientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		super.markAsChanged();
	}
	
	public void addElement(StorableElement element) {
		element.setReportTemplateId(this.getId());
		super.markAsChanged();
	}

	public void removeElement(StorableElement element) {
		element.setReportTemplateId(Identifier.VOID_IDENTIFIER);
		super.markAsChanged();
	}

	SheetSize getSheetSize() {
		return this.sheetSize;
	}

	public Set<Identifiable> getReverseDependencies(boolean usePool) throws ApplicationException {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(getAttachedTextStorableElements());
		dependencies.addAll(getDataStorableElements());
		dependencies.addAll(getImageStorableElements());
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return dependencies;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}
}
