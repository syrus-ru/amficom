package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTDATA_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.report.corba.IdlData;
import com.syrus.AMFICOM.report.corba.IdlDataHelper;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * <p>Title: </p>
 * <p>Description: Элемент шаблона - не табличный. Для хранения табличных
 * элементов шаблона используется TableDataStorableElement</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 * @module report
 */

public class DataStorableElement extends AbstractDataStorableElement<DataStorableElement> {
	private static final long	serialVersionUID	= 3501681877580290043L;

	DataStorableElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId,
			final String reportName,
			final String modelClassName) {
		super(id, created, modified, creatorId, modifierId, version, location, size, reportTemplateId, reportName, modelClassName);
	}
	
	public static DataStorableElement createInstance (Identifier creatorId, String reportName, String modelClassName, IntPoint location) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert reportName != null : NON_NULL_EXPECTED;
		assert modelClassName != null: NON_NULL_EXPECTED;
		assert location != null : NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			DataStorableElement data = new DataStorableElement(
					IdentifierPool.getGeneratedIdentifier(REPORTDATA_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					location,
					new IntDimension(),
					VOID_IDENTIFIER,
					reportName,
					modelClassName);
			data.markAsChanged();
			return data;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"DataStorableElement.createInstance() | cannot generate identifier ", ige);
		}
	}
	
	public DataStorableElement(IdlData transferable) {
		fromTransferable(transferable);
	}
	
	@Override
	protected synchronized void fromTransferable(IdlStorableObject transferable) {
		IdlData idlData = (IdlData) transferable;  
		try {
			super.fromTransferable(idlData);
		} catch (ApplicationException e) {
			// Never can happen
			assert false;
		}
	}
	
	@Override
	synchronized void setAttributes(Date created, 
			Date modified, 
			Identifier creatorId, 
			Identifier modifierId, 
			StorableObjectVersion version,
			final int locationX, 
			final int locationY, 
			final int width, 
			final int height,
			final Identifier reportTemplateId,
			final String reportName,
			final String moduleClassName) {
		super.setAttributes(created, modified, creatorId, modifierId, version,
				locationX, locationY, width, height, reportTemplateId, reportName, moduleClassName);
		
	}
	
	@Override
	public IdlStorableObject getTransferable(ORB orb) {
		return IdlDataHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.location.getX(),
				this.location.getY(),
				this.size.getWidth(),
				this.size.getHeight(),
				this.reportTemplateId.getTransferable(),
				this.reportName,
				this.modelClassName);
	}
	
	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}
	
	
	@Override
	protected DataWrapper getWrapper() {
		return DataWrapper.getInstance();
	}
	
	@Override
	protected DataStorableElement clone() throws CloneNotSupportedException {
		DataStorableElement clone = super.clone();
		return clone;
	}
}
