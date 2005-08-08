/**
 * $Id: PhysicalLinkState.java,v 1.6 2005/08/08 11:35:11 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

import java.util.LinkedList;
import java.util.List;

/**
 * ��������� �����
 *
 *
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:35:11 $
 * @module map
 */
public class PhysicalLinkState extends MapElementState {
	public String name;

	public AbstractNode startNode;
	public AbstractNode endNode;

	public List<NodeLink> nodeLinks = new LinkedList<NodeLink>();
	public Identifier mapProtoId;
	public String description;

	public PhysicalLinkState(final PhysicalLink physicalLink) {
		super();
		this.name = physicalLink.getName();
		this.startNode = physicalLink.getStartNode();
		this.endNode = physicalLink.getEndNode();

		this.nodeLinks.addAll(physicalLink.getNodeLinks());
		this.mapProtoId = physicalLink.getType().getId();
		this.description = physicalLink.getDescription();
	}

	@Override
	public boolean equals(final Object object) {
		final PhysicalLinkState physicalLinkState = (PhysicalLinkState) object;
		return (this.name.equals(physicalLinkState.name)
				&& this.startNode.equals(physicalLinkState.startNode)
				&& this.endNode.equals(physicalLinkState.endNode)
				&& this.description.equals(physicalLinkState.description)
				&& this.mapProtoId.equals(physicalLinkState.mapProtoId) && this.nodeLinks.equals(physicalLinkState.nodeLinks));
	}

	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addObject(this.name);
		codeGenerator.addObject(this.startNode);
		codeGenerator.addObject(this.endNode);
		codeGenerator.addObjectArray(this.nodeLinks.toArray());
		codeGenerator.addObject(this.mapProtoId);
		codeGenerator.addObject(this.description);
		return codeGenerator.getResult();
	}
}
