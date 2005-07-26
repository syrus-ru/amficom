/**
 * $Id: NodeState.java,v 1.5 2005/07/26 10:21:09 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

import java.util.HashMap;

/**
 * ��������� �������� - ����
 *
 *
 *
 * @version $Revision: 1.5 $, $Date: 2005/07/26 10:21:09 $
 * @module map_v1
 * @author $Author: arseniy $
 */
public class NodeState extends MapElementState {
	public String name;
	public String description;
	public Identifier imageId;
	public DoublePoint location;
	public String optimizerAttribute;
	public java.util.Map attributes = new HashMap();

	public NodeState(final AbstractNode mne) {
		super();
		this.name = mne.getName();
		// description = mne.getDescription();
		this.imageId = mne.getImageId();
		this.location = (DoublePoint) mne.getLocation().clone();
		// optimizerAttribute = mne.optimizerAttribute;

	}

	@Override
	public boolean equals(final Object object) {
		final NodeState mnes = (NodeState) object;
		return (this.name.equals(mnes.name)
				&& this.description.equals(mnes.description)
				&& this.imageId.equals(mnes.imageId)
				&& this.location.equals(mnes.location)
				&& this.optimizerAttribute.equals(mnes.optimizerAttribute));
	}
	
	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addObject(this.name);
		codeGenerator.addObject(this.description);
		codeGenerator.addObject(this.imageId);
		codeGenerator.addObject(this.location);
		codeGenerator.addObject(this.optimizerAttribute);
		codeGenerator.addObject(this.attributes);
		return codeGenerator.getResult();
	}
}
