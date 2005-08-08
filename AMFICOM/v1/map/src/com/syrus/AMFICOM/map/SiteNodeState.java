/**
 * $Id: SiteNodeState.java,v 1.5 2005/08/08 11:35:11 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * состояние узла
 *
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:35:11 $
 * @module map
 * @author $Author: arseniy $
 */
public class SiteNodeState extends NodeState {
	public Identifier mapProtoId;

	public SiteNodeState(final SiteNode msne) {
		super(msne);

		this.mapProtoId = msne.getType().getId();
	}

	@Override
	public boolean equals(final Object obj) {
		final SiteNodeState msnes = (SiteNodeState) obj;
		return super.equals(obj) && this.mapProtoId.equals(msnes.mapProtoId);
	}

	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.mapProtoId);
		return codeGenerator.getResult();
	}
}
