/*-
* $Id: DomainsPerspective.java,v 1.3 2005/09/04 11:31:23 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.LangModelManager;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.Perspective;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/04 11:31:23 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainsPerspective extends AbstractCommand {

	private final ManagerMainFrame graphText;
	private Perspective	perspective;
	
	public DomainsPerspective(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {

		this.graphText.currentPerspectiveLabel.setText(LangModelManager.getString("Label.DomainsLevel"));
	
		this.graphText.domainButton.setEnabled(true);
		this.graphText.netButton.setEnabled(true);
		
		this.graphText.userButton.setEnabled(false);

		this.graphText.armButton.setEnabled(false);

		this.graphText.rtuButton.setEnabled(false);

		this.graphText.serverButton.setEnabled(false);

		this.graphText.mcmButton.setEnabled(false);

		this.graphText.treeModel.setRoot(null);
		
		this.graphText.domainsButton.setEnabled(false);

		if (this.perspective == null) {
			this.perspective = new Perspective() {
				public String getPerspectiveName() {
					return "domains";
				}
			};
		}
		
		this.graphText.setPerspective(this.perspective);

		this.graphText.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
				ObjectEntities.DOMAIN});
		
		

	}
}

