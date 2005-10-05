/*
 * $Id: TableDataStorableElement.java,v 1.8 2005/10/05 09:39:36 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTABLEDATA_CODE;

import java.awt.Font;
import java.io.Serializable;
import java.util.Date;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.report.corba.IdlTableData;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
/**
 * Класс для отображения данных в табличном виде
 * @author $Author: peskovsky $
 * @version $Revision: 1.8 $, $Date: 2005/10/05 09:39:36 $
 * @module report_v1
 */
public final class TableDataStorableElement extends DataStorableElement implements Serializable {
	private static final long serialVersionUID = -2699698026579054587L;
	
	public static Font DEFAULT_FONT = new Font("Times New Roman",Font.PLAIN,12);	
	/**
	 * Число вертикальных разбиений таблицы (применяется для длинных и узких
	 * таблиц)
	 */
	private int verticalDivisionsCount;
	
	private Font font = DEFAULT_FONT;
	
	TableDataStorableElement (final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId,
			final String reportName,
			final String modelClassName,
			final int verticalDivisionsCount) {
		super(id, created, modified, creatorId, modifierId, version, location, size, reportTemplateId, reportName, modelClassName);
		this.verticalDivisionsCount = verticalDivisionsCount;
	}
	
	public static DataStorableElement createInstance (Identifier creatorId, String reportName, String modelClassName, int verticalDivisionsCount, IntPoint location) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert reportName != null : NON_NULL_EXPECTED;
		assert modelClassName != null: NON_NULL_EXPECTED;
		assert location != null: NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			return new TableDataStorableElement(
					IdentifierPool.getGeneratedIdentifier(REPORTTABLEDATA_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					location,
					new IntDimension(),
					VOID_IDENTIFIER,
					reportName,
					modelClassName,
					verticalDivisionsCount);
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"DataStorableElement.createInstance() | cannot generate identifier ", ige);
		}
	}
	
	public TableDataStorableElement(IdlTableData transferable) {
		super(transferable);
		this.verticalDivisionsCount = transferable.verticalDivisionCount;
	}
	
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
			final String moduleClassName,
			final int verticalDivisionCount) {
		super.setAttributes(created, modified, creatorId, modifierId, version,
				locationX, locationY, width, height, reportTemplateId, reportName, moduleClassName);
		this.verticalDivisionsCount = verticalDivisionCount;
	}
	
	public int getVerticalDivisionsCount() {
		return this.verticalDivisionsCount;
	}

	public void setVerticalDivisionsCount(int verticalDivisionsCount) {
		this.verticalDivisionsCount = verticalDivisionsCount;
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}
