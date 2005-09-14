/*-
 * $Id: SchemeImportCommitCommand.java,v 1.1 2005/09/14 10:32:34 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.util.Log;

public class SchemeImportCommitCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public SchemeImportCommitCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	@Override
	public void execute() {
		try {
			LocalXmlIdentifierPool.flush();
			
			Identifier userId = LoginManager.getUserId();
			StorableObjectPool.flush(ObjectEntities.EQUIPMENT_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.PORT_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.LINK_CODE, userId, false);
			
			StorableObjectPool.flush(ObjectEntities.EQUIPMENT_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.CABLELINK_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.LINK_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.PORT_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.CABLETHREAD_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, false);
			
			StorableObjectPool.flush(ObjectEntities.SCHEMEPROTOGROUP_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.SCHEMEPROTOELEMENT_CODE, userId, false);
			
//			StorableObjectPool.flush(ObjectEntities.SCHEME_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMEELEMENT_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMEDEVICE_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMECABLEPORT_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMEPORT_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMELINK_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMECABLELINK_CODE, userId, false);
//			StorableObjectPool.flush(ObjectEntities.SCHEMECABLETHREAD_CODE, userId, false);
			
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuSchemeImportCommit", false);
			aModel.fireModelChanged();
			
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					LangModelScheme.getString("Message.information.import_saved"), //$NON-NLS-1$
					LangModelScheme.getString("Message.information"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
	}
}
