/*-
 * $Id: SchemeImportCommitCommand.java,v 1.9 2005/10/30 15:20:54 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.util.Log;

public class SchemeImportCommitCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public SchemeImportCommitCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	@Override
	public void execute() {
		try {
			Identifier userId = LoginManager.getUserId();
			StorableObjectPool.flush(ObjectEntities.EQUIPMENT_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.PORT_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.LINK_CODE, userId, false);
			
			StorableObjectPool.flush(ObjectEntities.PROTOEQUIPMENT_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.CABLELINK_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.LINK_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.PORT_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.CABLETHREAD_TYPE_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, false);
			
			// TODO save by hierarchy
			StorableObjectPool.flush(ObjectEntities.SCHEMEPROTOGROUP_CODE, userId, false);
			StorableObjectPool.flush(ObjectEntities.SCHEMEPROTOELEMENT_CODE, userId, false);
//			LinkedIdsCondition condition = new LinkedIdsCondition(Identifier.VOID_IDENTIFIER, 
//					ObjectEntities.SCHEMEPROTOGROUP_CODE);
//			Set<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition, false);
//			groups.remove(SchemeObjectsFactory.stubProtoGroup);
//
//			for (SchemeProtoGroup group : groups) {
//				Set<Identifiable> ids = group.getReverseDependencies(false);
//				StorableObjectPool.flush(ids, userId, false);
//			}
			LocalXmlIdentifierPool.flush();
			
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled("Menu.import.commit", false);
			aModel.fireModelChanged();
			
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					LangModelScheme.getString("Message.information.import_saved"), //$NON-NLS-1$
					LangModelScheme.getString("Message.information"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
		}
	}
}
