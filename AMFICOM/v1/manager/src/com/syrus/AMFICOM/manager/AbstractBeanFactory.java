/*-
* $Id: AbstractBeanFactory.java,v 1.10 2005/09/12 12:06:26 bob Exp $
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
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.10 $, $Date: 2005/09/12 12:06:26 $
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
	
	protected ManagerMainFrame graphText;
	
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
	
	public abstract AbstractBean createBean(Perspective perspective) 
	throws IllegalObjectEntityException, CreateObjectException;	

	public abstract AbstractBean createBean(String codename);	

	
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
	
	public final ManagerMainFrame getGraphText() {
		return this.graphText;
	}
	
	public final void setGraphText(ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	public abstract String getCodename();
	
	public final int getCount() {
		return this.count;
	}
}

