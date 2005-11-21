package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client_.scheme.ui.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeWrapper;
import com.syrus.util.Log;

public class SchemeOpenCommand extends AbstractCommand {
	ApplicationContext aContext;

	public SchemeOpenCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}


	@Override
	public void execute()
	{
//		StorableObjectCondition condition1 = new EquivalentCondition(
//			ObjectEntities.SCHEME_CODE);
		Identifier domainId = LoginManager.getDomainId();
		StorableObjectCondition condition1 = new LinkedIdsCondition(
				domainId, ObjectEntities.SCHEME_CODE);
		
//		TypicalCondition condition2 = new TypicalCondition(
//				Identifier.VOID_IDENTIFIER.getIdentifierString(), 
//				OperationSort.OPERATION_EQUALS,
//				ObjectEntities.SCHEME_CODE, 
//				SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
		
//		CompoundCondition condition = new CompoundCondition(condition1, 
//				CompoundConditionSort.AND, condition2);
		
		try {
			Set<Scheme> schemes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
			
			for (Iterator<Scheme> it = schemes.iterator(); it.hasNext();) {
				if (it.next().getParentSchemeElement() != null) {
					it.remove();
				}
			}
			
			Scheme scheme = (Scheme)WrapperedTableChooserDialog.showChooserDialog(
					I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW),
					schemes,
					SchemeWrapper.getInstance(),
					new String[] {StorableObjectWrapper.COLUMN_NAME,
							StorableObjectWrapper.COLUMN_CREATED,
							StorableObjectWrapper.COLUMN_CREATOR_ID},
					false);
			
			if (scheme != null) {
				this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));				
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}
