/**
 * $Id: NodeState.java,v 1.3 2005/01/17 15:05:41 bob Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

import java.util.HashMap;

/**
 * состояние элемента - узла 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/01/17 15:05:41 $
 * @module map_v1
 * @author $Author: bob $
 */
public class NodeState extends MapElementState
{
	public String name;
	public String description;
	public Identifier imageId;
	public DoublePoint location;
	public String optimizerAttribute;
	public java.util.Map attributes = new HashMap();
	
	public NodeState(AbstractNode mne)
	{
		super();
		this.name = mne.getName();
//		description = mne.getDescription();
		this.imageId = mne.getImageId();
		this.location = (DoublePoint)mne.getLocation().clone();
//		optimizerAttribute = mne.optimizerAttribute;

	}

	public boolean equals(Object object) {
		NodeState mnes = (NodeState)object;
		return (this.name.equals(mnes.name)
			&& this.description.equals(mnes.description)
			&& this.imageId.equals(mnes.imageId)
			&& this.location.equals(mnes.location)
			&& this.optimizerAttribute.equals(mnes.optimizerAttribute));
	}
	
	public int hashCode() {
		HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addObject(this.name);
		codeGenerator.addObject(this.description);
		codeGenerator.addObject(this.imageId);
		codeGenerator.addObject(this.location);
		codeGenerator.addObject(this.optimizerAttribute);
		codeGenerator.addObject(this.attributes);
		return codeGenerator.getResult();
	}
}
