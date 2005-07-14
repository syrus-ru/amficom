/*-
* $Id: AbstractBeanFactory.java,v 1.1 2005/07/14 10:14:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jgraph.graph.DefaultEdge;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 10:14:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBeanFactory {

	protected Icon icon;
	
	protected Icon image;
	
	protected String nameKey;
	
	protected String shortNameKey;
	
	protected AbstractBeanFactory(final String nameKey, 
	                              final String shortNameKey,
	                              final String iconUrl,
	                              final String imageUrl) {
		this.nameKey = nameKey;
		this.shortNameKey = shortNameKey;
		
		URL resource = AbstractBeanFactory.class.getClassLoader().getResource(iconUrl);
		if (resource != null) {
			this.icon = new ImageIcon(resource);
		}
		
		resource = AbstractBeanFactory.class.getClassLoader().getResource(imageUrl);
		if (resource != null) {
			this.image = new ImageIcon(resource);
		}
	}
	
	public abstract AbstractBean createBean();	
	
	public final Icon getIcon() {
		return this.icon;
	}
	
	public final Icon getImage() {
		return this.image;
	}
	
	public String getName() {
		// TODO use i18n
		return this.nameKey;
	}
	
	public String getShortName() {
		// TODO use i18n
		return this.shortNameKey;
	}
}

