/*-
 * $Id: SchemeImportCommitCommand.java,v 1.13.4.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
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
			
			EquivalentCondition condition1 = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
			Set<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition1, false);
			groups.remove(SchemeObjectsFactory.getStubProtoGroup());
			StorableObjectPool.flush(groups, userId, false);

			EquivalentCondition condition2 = new EquivalentCondition(ObjectEntities.SCHEMEPROTOELEMENT_CODE);
			Set<SchemeProtoElement> protos = StorableObjectPool.getStorableObjectsByCondition(condition2, false);
			protos.remove(SchemeObjectsFactory.getStubProtoElement());

			for (SchemeProtoElement proto : protos) {
				Set<Identifiable> ids = proto.getReverseDependencies();
				StorableObjectPool.flush(ids, userId, false);
			}
			LocalXmlIdentifierPool.flush();
			
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled("Menu.import.commit", false);
			aModel.fireModelChanged();
			
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
					LangModelScheme.getString("Message.information.import_saved"), //$NON-NLS-1$
					LangModelScheme.getString("Message.information"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}
