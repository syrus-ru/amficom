/**
 * $Id: NodeState.java,v 1.1 2004/12/20 12:36:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import java.util.HashMap;

/**
 * состояние элемента - узла 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/20 12:36:01 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		name = mne.getName();
//		description = mne.getDescription();
		imageId = mne.getImageId();
		location = (DoublePoint)mne.getLocation().clone();
//		optimizerAttribute = mne.optimizerAttribute;

	}

	public boolean equals(Object obj)
	{
		NodeState mnes = (NodeState)obj;
		return (this.name.equals(mnes.name)
			&& this.description.equals(mnes.description)
			&& this.imageId.equals(mnes.imageId)
			&& this.location.equals(mnes.location)
			&& this.optimizerAttribute.equals(mnes.optimizerAttribute));
	}
}
