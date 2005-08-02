/*-
* $Id: AbstractBeanFactory.java,v 1.4 2005/08/02 14:42:06 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;


/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 14:42:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBeanFactory {

	protected Icon icon;
	
	protected Icon image;
	
	protected String nameKey;
	
	protected String shortNameKey;
	
	protected int count = 0;
	
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
	
	public abstract AbstractBean createBean() 
	throws IllegalObjectEntityException, CreateObjectException;	
	
	public final Icon getIcon() {
		return this.icon;
	}
	
	public final Icon getImage() {
		return this.image;
	}
	
	public String getName() {
		return LangModelManager.getString(this.nameKey);
	}
	
	public String getShortName() {
		return LangModelManager.getString(this.shortNameKey);
	}
}

