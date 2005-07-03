/**
 * $Id: SiteNodeState.java,v 1.3 2005/05/18 11:48:20 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * ��������� ����
 *
 * @version $Revision: 1.3 $, $Date: 2005/05/18 11:48:20 $
 * @module map_v1
 * @author $Author: bass $
 */
public class SiteNodeState extends NodeState
{
	public Identifier mapProtoId;
	
	public SiteNodeState(SiteNode msne)
	{
		super(msne);

		this.mapProtoId = msne.getType().getId();
	}

	public boolean equals(Object obj)
	{
		SiteNodeState msnes = (SiteNodeState)obj;
		return super.equals(obj)
			&& this.mapProtoId.equals(msnes.mapProtoId);
	}
	
	public int hashCode() {
		HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.mapProtoId);
		return codeGenerator.getResult();
	}
}
