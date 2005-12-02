/*-
* $Id: PermissionAttributes.java,v 1.35 2005/12/02 11:24:11 bass Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributes;
import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributesHelper;
import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributesPackage.IdlModule;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;
import com.syrus.util.TransferableObject;


/**
 * @version $Revision: 1.35 $, $Date: 2005/12/02 11:24:11 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public final class PermissionAttributes extends StorableObject<PermissionAttributes> {
	private static final long serialVersionUID = -8967626134139619548L;

	public static enum Module implements TransferableObject<IdlModule> {
		ADMINISTRATION(true),
		SCHEME(true),
		ELEMENTS_EDITOR(true),
		MAP_EDITOR(true),
		OPTIMIZATION(false),
		MODELING(true),
		SCHEDULER(true),
		ANALYSIS(true),
		RESEARCH(true),
		EVALUATION(true),
		OBSERVER(true),
		PREDICTION(false),
		REPORT(true);

		private static final String KEY_ROOT = "Module.Description.";

		private static final Module[] VALUES = values();
		private static final List<Module> VALUE_LIST = 
			Collections.unmodifiableList(Arrays.asList(VALUES));

		private final boolean enable;
		private final String codename;

		private Module(final boolean enable) {
			this.enable = enable;
			this.codename = getJavaNamingStyleName(this.name());
		}

		public static final Module valueOf(final int ordinal) {			
			return VALUES[ordinal];
		} 

		public static final Module valueOf(final Integer ordinal) {
			return valueOf(ordinal.intValue());
		}

		public static Module valueOf(final IdlModule idlModule) {
			return VALUES[idlModule.value()];
		}

		public static final List<Module> getValueList() {
			return VALUE_LIST;
		}		

		public final IdlModule getTransferable() {
			return IdlModule.from_int(this.ordinal());
		}

		public IdlModule getTransferable(final ORB orb) {
			return this.getTransferable();
		}

		public final String getCodename() {
			return this.codename;
		}

		public final String getDescription() {
			return LangModelAdministation.getString(KEY_ROOT + this.codename);
		}

		public final boolean isEnable() {
			return this.enable;
		}
	}

	private interface SwitchableGroupNumber {
		Module getModule();
		boolean isEnable();
	}

	/**
	 * Administation permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Adminstration implements SwitchableGroupNumber {
		ENTER(true),

		CREATE_DOMAIN(true),
		CREATE_GROUP(false), // Создать группу
		CREATE_USER(true),
		CREATE_MEASUREMENT_MODULE(true),
		CREATE_SERVER(true),
		CREATE_WORKSTATION(true),

		CHANGE_DOMAIN(true),
		CHANGE_GROUP(false), //
		CHANGE_USER(true),
		CHANGE_MEASUREMENT_MODULE(true),
		CHANGE_SERVER(true),
		CHANGE_WORKSTATION(true),

		DELETE_DOMAIN(true),
		DELETE_GROUP(false), //
		DELETE_USER(true);


		private final boolean enable;

		private Adminstration(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.ADMINISTRATION;
		}
	}

	/**
	 * ElementsEditor permission enum
	 * 
	 *  <p><b>ОХНУНГ !!! Стасег и иво прадалжатили, Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum ElementsEditor implements SwitchableGroupNumber {		
		ENTER(true),
		CREATE_AND_EDIT(true),
		SAVING(true),
		CREATE_CHANGE_SAVE_TYPE(true);

		private final boolean enable;

		private ElementsEditor(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.ELEMENTS_EDITOR;
		}
	}

	/**
	 * Scheme permission enum
	 * 
	 *  <p><b>ОХНУНГ !!! Стасег и иво прадалжатили, Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Scheme implements SwitchableGroupNumber {		
		ENTER(true),
		CREATE_AND_EDIT(true),
		SAVING(true);

		private final boolean enable;

		private Scheme(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.SCHEME;
		}
	}

	/**
	 * MapEditor permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum MapEditor implements SwitchableGroupNumber {
		ENTER(true),

		EDIT_TOPOLOGICAL_SCHEME(true),
		SAVE_TOPOLOGICAL_SCHEME(true),
		SAVE_TOPOLOGICAL_VIEW(true),

		EDIT_BINDING(true),
		SAVE_BINDING(true);

		private final boolean enable;

		private MapEditor(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.MAP_EDITOR;
		}
	}

	/**
	 * Optimization permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Optimization implements SwitchableGroupNumber {
		ENTER(true),

		OPEN_MAP(true),
		OPEN_SCHEME(true),
		SET_OPTIMIZATION_OPTIONS(true),
		START_OPTIMIZATION(true),
		ABORT_OPTIMIZATION(true),
		SAVE_OPTIMIZATION_RESULT(true),
		SAVE_OPTIMIZATION_OPTIONS(true);

		private final boolean enable;

		private Optimization(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.OPTIMIZATION;
		}
	}

	/**
	 * Modeling permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Modeling implements SwitchableGroupNumber {
		ENTER(true),

		OPEN_MAP(true),
		OPEN_SCHEME(true),

		SET_MODELING_OPTIONS(true),
		SAVE_REFLECTOGRAM_MODEL(true);

		private final boolean enable;

		private Modeling(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.MODELING;
		}
	}

	/**
	 * Scheduler permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Scheduler implements SwitchableGroupNumber {
		ENTER(true),

//		Задать тестирование КИСа 
//		Задать тестирование всех КИСов

		CREATE_TEST(true),
		EDIT_TEST(true),
		SAVE_TEST(true);

		private final boolean enable;

		private Scheduler(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.SCHEDULER;
		}
	}

	/**
	 * Analysis permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Analysis implements SwitchableGroupNumber {
		ENTER(true),
		OPEN_REFLECTOGRAM_FILE(true),
		OPEN_REFLECTOGRAM(true);

		private final boolean enable;

		private Analysis(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.ANALYSIS;
		}
	}

	/**
	 * Research permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Research implements SwitchableGroupNumber {
		ENTER(true),
		OPEN_REFLECTOGRAM_FILE(true),
		OPEN_REFLECTOGRAM(true),
		SAVE_MEASUREMENT_SETUP(true),
		SAVE_REFLECTOGRAM_FILE(true),
		SAVE_SCHEME_BINDING(true);

		private final boolean enable;

		private Research(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.RESEARCH;
		}
	}

	/**
	 * Evaluation permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Evaluation implements SwitchableGroupNumber {
		ENTER(true),
		OPEN_REFLECTOGRAM(true),
		OPEN_REFLECTOGRAM_FILE(true),
		OPEN_ETALON_REFLECTOGRAM(true),
		SAVE_MEASUREMENT_SETUP(true),
		SAVE_REFLECTOGRAM_FILE(true),
		EDIT_ETALON(true);

		private final boolean enable;

		private Evaluation(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.EVALUATION;
		}
	}

	/**
	 * Observe permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Observer implements SwitchableGroupNumber {
		ENTER(true),
		OPEN_MAP(true),
		OPEN_SCHEME(true),
		OPEN_MEASUREMENT_ARCHIVE(true),
		QUICK_TASK(true),
		ALARM_MANAGE(true);

		private final boolean enable;

		private Observer(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.OBSERVER;
		}
	}

	/**
	 * Prediction permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Prediction implements SwitchableGroupNumber {
		ENTER(true),
		SAVE_PREDICTION_REFLECTOGRAM(true);

		private final boolean enable;

		private Prediction(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.PREDICTION;
		}
	}

	/**
	 * Report permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Report implements SwitchableGroupNumber {
		ENTER(true),
		CREATE_TEMPLATE(true),
		SAVE_TEMPLATE(true);

		private final boolean enable;

		private Report(final boolean enable) {
			this.enable = enable;			
		}

		public boolean isEnable() {
			return this.enable;
		}

		public final Module getModule() {
			return Module.REPORT;
		}
	}

	public static enum PermissionCodename {
		// Adminstration
		ADMINISTRATION_ENTER(Adminstration.ENTER),
		ADMINISTRATION_CREATE_DOMAIN(Adminstration.CREATE_DOMAIN),
		ADMINISTRATION_CREATE_GROUP(Adminstration.CREATE_GROUP),
		ADMINISTRATION_CREATE_USER(Adminstration.CREATE_USER),
		ADMINISTRATION_CREATE_MEASUREMENT_MODULE(Adminstration.CREATE_MEASUREMENT_MODULE),
		ADMINISTRATION_CREATE_SERVER(Adminstration.CREATE_SERVER),
		ADMINISTRATION_CREATE_WORKSTATION(Adminstration.CREATE_WORKSTATION),

		ADMINISTRATION_CHANGE_DOMAIN(Adminstration.CHANGE_DOMAIN),
		ADMINISTRATION_CHANGE_GROUP(Adminstration.CHANGE_GROUP),
		ADMINISTRATION_CHANGE_USER(Adminstration.CHANGE_USER),
		ADMINISTRATION_CHANGE_MEASUREMENT_MODULE(Adminstration.CHANGE_MEASUREMENT_MODULE),
		ADMINISTRATION_CHANGE_SERVER(Adminstration.CHANGE_SERVER),
		ADMINISTRATION_CHANGE_WORKSTATION(Adminstration.CHANGE_WORKSTATION),

		ADMINISTRATION_DELETE_DOMAIN(Adminstration.DELETE_DOMAIN),
		ADMINISTRATION_DELETE_GROUP(Adminstration.DELETE_GROUP),
		ADMINISTRATION_DELETE_USER(Adminstration.DELETE_USER),

		// ElementsEditor		
		ELEMENTS_EDITOR_ENTER(ElementsEditor.ENTER),
		ELEMENTS_EDITOR_CREATE_AND_EDIT(ElementsEditor.CREATE_AND_EDIT),
		ELEMENTS_EDITOR_SAVING(ElementsEditor.SAVING),
		ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE(ElementsEditor.CREATE_CHANGE_SAVE_TYPE),

		// Scheme
		SCHEME_ENTER(Scheme.ENTER),
		SCHEME_CREATE_AND_EDIT(Scheme.CREATE_AND_EDIT),
		SCHEME_SAVING(Scheme.SAVING),

		// MapEditor		
		MAP_EDITOR_ENTER(MapEditor.ENTER),

		MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME(MapEditor.EDIT_TOPOLOGICAL_SCHEME),
		MAP_EDITOR_SAVE_TOPOLOGICAL_SCHEME(MapEditor.SAVE_TOPOLOGICAL_SCHEME),
		MAP_EDITOR_SAVE_TOPOLOGICAL_VIEW(MapEditor.SAVE_TOPOLOGICAL_VIEW),

		MAP_EDITOR_EDIT_BINDING(MapEditor.EDIT_BINDING),
		MAP_EDITOR_SAVE_BINDING(MapEditor.SAVE_BINDING),

		// Optimization
		OPTIMIZATION_ENTER(Optimization.ENTER),

		OPTIMIZATION_OPEN_MAP(Optimization.OPEN_MAP),
		OPTIMIZATION_OPEN_SCHEME(Optimization.OPEN_SCHEME),
		OPTIMIZATION_SET_OPTIMIZATION_OPTIONS(Optimization.SET_OPTIMIZATION_OPTIONS),
		OPTIMIZATION_START_OPTIMIZATION(Optimization.START_OPTIMIZATION),
		OPTIMIZATION_ABORT_OPTIMIZATION(Optimization.ABORT_OPTIMIZATION),
		OPTIMIZATION_SAVE_OPTIMIZATION_RESULT(Optimization.SAVE_OPTIMIZATION_RESULT),
		OPTIMIZATION_SAVE_OPTIMIZATION_OPTIONS(Optimization.SAVE_OPTIMIZATION_OPTIONS),

		// Modeling
		MODELING_ENTER(Modeling.ENTER),

		MODELING_OPEN_MAP(Modeling.OPEN_MAP),
		MODELING_OPEN_SCHEME(Modeling.OPEN_SCHEME),

		MODELING_SET_MODELING_OPTIONS(Modeling.SET_MODELING_OPTIONS),
		MODELING_SAVE_REFLECTOGRAM_MODEL(Modeling.SAVE_REFLECTOGRAM_MODEL),

		// Scheduler
		SCHEDULER_ENTER(Scheduler.ENTER),

		SCHEDULER_CREATE_TEST(Scheduler.CREATE_TEST),
		SCHEDULER_EDIT_TEST(Scheduler.EDIT_TEST),
		SCHEDULER_SAVE_TEST(Scheduler.SAVE_TEST),

		// Analysis
		ANALYSIS_ENTER(Analysis.ENTER),
		ANALYSIS_OPEN_REFLECTOGRAM_FILE(Analysis.OPEN_REFLECTOGRAM_FILE),
		ANALYSIS_OPEN_REFLECTOGRAM(Analysis.OPEN_REFLECTOGRAM),

		// Research
		RESEARCH_ENTER(Research.ENTER),
		RESEARCH_OPEN_REFLECTOGRAM_FILE(Research.OPEN_REFLECTOGRAM_FILE),
		RESEARCH_OPEN_REFLECTOGRAM(Research.OPEN_REFLECTOGRAM),
		RESEARCH_SAVE_MEASUREMENT_SETUP(Research.SAVE_MEASUREMENT_SETUP),
		RESEARCH_SAVE_REFLECTOGRAM_FILE(Research.SAVE_REFLECTOGRAM_FILE),
		RESEARCH_SAVE_SCHEME_BINDING(Research.SAVE_SCHEME_BINDING),

		// Evaluation
		EVALUATION_ENTER(Evaluation.ENTER),
		EVALUATION_OPEN_REFLECTOGRAM(Evaluation.OPEN_REFLECTOGRAM),
		EVALUATION_OPEN_REFLECTOGRAM_FILE(Evaluation.OPEN_REFLECTOGRAM_FILE),
		EVALUATION_OPEN_ETALON_REFLECTOGRAM(Evaluation.OPEN_ETALON_REFLECTOGRAM),
		EVALUATION_SAVE_MEASUREMENT_SETUP(Evaluation.SAVE_MEASUREMENT_SETUP),
		EVALUATION_SAVE_REFLECTOGRAM_FILE(Evaluation.SAVE_REFLECTOGRAM_FILE),
		EVALUATION_EDIT_ETALON(Evaluation.EDIT_ETALON),

		// Observe
		OBSERVER_ENTER(Observer.ENTER),
		OBSERVER_OPEN_MAP(Observer.OPEN_MAP),
		OBSERVER_OPEN_SCHEME(Observer.OPEN_SCHEME),
		OBSERVER_OPEN_MEASUREMENT_ARCHIVE(Observer.OPEN_MEASUREMENT_ARCHIVE),
		OBSERVER_QUICK_TASK(Observer.QUICK_TASK),
		OBSERVER_ALARM_MANAGE(Observer.ALARM_MANAGE),

		// Prediction
		PREDICTION_ENTER(Prediction.ENTER),
		PREDICTION_SAVE_PROGNOSTICATION_REFLECTOGRAM(Prediction.SAVE_PREDICTION_REFLECTOGRAM),

		// Report
		REPORT_ENTER(Report.ENTER),
		REPORT_CREATE_TEMPLATE(Report.CREATE_TEMPLATE),
		REPORT_SAVE_TEMPLATE(Report.SAVE_TEMPLATE);

		private final Enum e;		

		private static final String KEY_ROOT = "PermissionAttributes.Description.";

		private String codename;

		private PermissionCodename(final SwitchableGroupNumber e) {
			this.e = (Enum) e;
			this.codename = e.getModule().getCodename()
				+ '.'
				+ getJavaNamingStyleName(this.e.name());
		}

		public final String getDescription() {
			return LangModelAdministation.getString(KEY_ROOT + this.codename);
		}

		public final String getCodename() {
			return this.codename;
		}

		final int getOrderInGroup() {
			return this.e.ordinal();
		}

		public final Module getModule() {
			return ((SwitchableGroupNumber) this.e).getModule();
		}

		public boolean isEnable() {
			return this.getModule().isEnable() && ((SwitchableGroupNumber) this.e).isEnable();
		}
	}

	private Identifier parentId;

	private Identifier domainId;

	private Module module;

	private BitSet permissions;

	private BitSet denyMask;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public PermissionAttributes(final IdlPermissionAttributes dt) throws CreateObjectException {
		try {
			this.fromTransferable(dt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	PermissionAttributes(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final Identifier parentId,
			final Module module,
			final BigInteger permissions,
			final BigInteger denyMask) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.domainId = domainId;
		this.parentId = parentId;
		this.module = module;

		this.permissions = new BitSet();
		this.setPermissions0(permissions);

		this.denyMask = new BitSet();
		this.setDenyMask0(denyMask);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPermissionAttributes pat = (IdlPermissionAttributes) transferable;
		super.fromTransferable(pat);
		this.domainId = new Identifier(pat.domainId);
		this.parentId = new Identifier(pat.userId);
		this.module = Module.valueOf(pat._module);
		if (this.permissions == null) {
			this.permissions = new BitSet();
		}

		this.setPermissionsByteArray0(pat.permissionMask);

		if (this.denyMask == null) {
			this.denyMask = new BitSet();
		}

		this.setDenyMaskByteArray0(pat.denyMask);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	public IdlPermissionAttributes getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return IdlPermissionAttributesHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.domainId.getTransferable(),
				this.parentId.getTransferable(),
				this.module.getTransferable(),
				this.getPermissionByteArray0(),
				this.getDenyMaskByteArray0());
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.parentId != null && !this.parentId.isVoid()
				&& this.domainId != null &&
					(this.parentId.getMajor() == ObjectEntities.ROLE_CODE || !this.domainId.isVoid())
				&& this.permissions != null;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param userId
	 * @return new instance for client
	 * @throws CreateObjectException
	 */
	public static PermissionAttributes createInstance(final Identifier creatorId,
			final Identifier domainId,
			final Identifier userId,
			final Module module) throws CreateObjectException {
		try {
			final PermissionAttributes permissionAttributes = new PermissionAttributes(
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERMATTR_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					domainId,
					userId,
					module,
					BigInteger.ZERO,
					BigInteger.ZERO);

			assert permissionAttributes.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			permissionAttributes.markAsChanged();

			return permissionAttributes;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final Identifier parentId,
			final Module module,
			final BigInteger permissions,
			final BigInteger denyMask) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.domainId = domainId;
		this.parentId = parentId;
		this.module = module;
		this.setPermissions0(permissions);
		this.setDenyMask0(denyMask);
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		if (!this.domainId.isVoid()) {
			dependencies.add(this.domainId);
		}
		if (!this.parentId.isVoid()) {
			dependencies.add(this.parentId);
		}
		return dependencies;
	}

	/**
	 * @param permissionCode permission codename
	 * @param enable
	 * 
	 * @throws IllegalArgumentException if permission is disabled 
	 * or permission belongs to other module
	 */
	public final void setPermissionEnable(final PermissionCodename permissionCode,
	                                      final boolean enable) {
		this.setSet(permissionCode, enable, this.permissions);
	}

	/**
	 * @param permissionCode permission codename
	 * @param enable
	 * 
	 * @throws IllegalArgumentException if permission is disabled 
	 * or permission belongs to other module
	 */
	public final void setDenidEnable(final PermissionCodename permissionCode,
	                                 final boolean enable) {
		this.setSet(permissionCode, enable, this.denyMask);
	}

	private final void setSet(final PermissionCodename permissionCode,
                              final boolean enable,
                              final BitSet bitSet) {
		if (!permissionCode.isEnable()) {
			throw new IllegalArgumentException("PermissionCode " + permissionCode.name() + " disabled.");
		}

		if (this.module != permissionCode.getModule()) {
			throw new IllegalArgumentException("This Permission doen't support " + permissionCode.name());
		}
		Log.debugMessage((enable ? "en" : "dis") + "able " 
				+ permissionCode.getOrderInGroup() 
				+ " bit in "
				+ this.module + " group" ,
			Log.DEBUGLEVEL10);
		bitSet.set(permissionCode.getOrderInGroup(), enable);
	}

	/**
	 * @param permissionCode
	 * @return true if permission is enabled for permissioncode 
	 * 
	 * @throws IllegalArgumentException if permission is disabled 
	 * or permission belongs to other module
	 */
	public final boolean isPermissionEnable(final PermissionCodename permissionCode) {
		return this.isSet(permissionCode, this.permissions);
	}

	/**
	 * @param permissionCode
	 * @return true if permission is denied for permissioncode 
	 * 
	 * @throws IllegalArgumentException if permission is disabled 
	 * or permission belongs to other module
	 */
	public final boolean isDenied(final PermissionCodename permissionCode) {
		return this.isSet(permissionCode, this.denyMask);
	}

	private final boolean isSet(final PermissionCodename permissionCode,
	                            final BitSet bitSet) {
		if (!permissionCode.isEnable()) {
			throw new IllegalArgumentException("PermissionCode " + permissionCode.name() + " disabled.");
		}

		if (this.module != permissionCode.getModule()) {
			throw new IllegalArgumentException("This Permission doen't support " 
				+ permissionCode.name() 
				+ ", this.module:" 
				+ this.module
				+ ", permissionCode.getModule():" + permissionCode.getModule());
		}

		return bitSet.get(permissionCode.getOrderInGroup());
	}

	/**
	 * @return permission digital form 
	 */
	public final BigInteger getPermissions() {
		return new BigInteger(this.getPermissionByteArray0());
	}

	/**
	 * @return permission byte array digital form
	 */
	final byte[] getPermissionByteArray0() {
		return this.getBitSetMaskByteArray0(this.permissions);
	}

	/**
	 * set permission from permission digital form
	 * @param permissions
	 */
	public final void setPermissions(final BigInteger permissions) {
		this.setPermissions0(permissions);
		super.markAsChanged();
	}

	final void setPermissions0(final BigInteger permissions) {
		this.setPermissionsByteArray0(permissions.toByteArray());
	}

	/**
	 * set permission from permission byte array digital form
	 * @param bytes permission byte array digital form
	 */
	final void setPermissionsByteArray0(final byte[] bytes) {
		this.setBitSetMaskByteArray0(bytes, this.permissions);
	}

	/**
	 * @return permission digital form 
	 */
	public final BigInteger getDenyMask() {
		return new BigInteger(this.getDenyMaskByteArray0());
	}

	/**
	 * @return permission byte array digital form
	 */
	final byte[] getDenyMaskByteArray0() {
		return this.getBitSetMaskByteArray0(this.denyMask);
	}

	private final byte[] getBitSetMaskByteArray0(final BitSet bitSet) {
		final byte[] bytes = new byte[bitSet.length()/8+1];
		for (int i=0; i<bitSet.length(); i++) {
		    if (bitSet.get(i)) {
		        bytes[bytes.length-i/8-1] |= 1<<(i%8);
		    }
		}
		return bytes;
	}

	/**
	 * set permission from deny mask digital form
	 * @param denyMask
	 */
	public final void setDenyMask(final BigInteger denyMask) {
		this.setDenyMask0(denyMask);
		super.markAsChanged();
	}

	final void setDenyMask0(final BigInteger denyMask) {
		this.setDenyMaskByteArray0(denyMask.toByteArray());
	}

	/**
	 * set permission from deny mask byte array digital form
	 * @param bytes deny mask byte array digital form
	 */
	final void setDenyMaskByteArray0(final byte[] bytes) {
		this.setBitSetMaskByteArray0(bytes, this.denyMask);
	}

	private final void setBitSetMaskByteArray0(final byte[] bytes,
			final BitSet bitSet) {
		bitSet.clear();
		for (int i=0; i<bytes.length*8; i++) {
		    if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
		        bitSet.set(i);
		    }
		}
	}

	public final Identifier getDomainId() {
		return this.domainId;
	}

	public final void setDomainId(final Identifier domainId) {
		this.domainId = domainId;
		super.markAsChanged();
	}

	public final Identifier getParentId() {
		return this.parentId;
	}

	public final void setParentId(final Identifier parentId) {
		this.parentId = parentId;
		super.markAsChanged();
	}

	public final Module getModule() {
		return this.module;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected PermissionAttributesWrapper getWrapper() {
		return PermissionAttributesWrapper.getInstance();
	}

	/**
	 * generate string  as javaNamingStyle from string like JAVA_NAMING_STYLE
	 */
	static String getJavaNamingStyleName(final String string) {
		final StringBuffer buffer = new StringBuffer();
		final String[] strings = string.split("_");
		for (int i = 0; i < strings.length; i++) {
			if (i == 0) {
				buffer.append(strings[i].toLowerCase());
			} else {
				buffer.append(strings[i].charAt(0));
				buffer.append(strings[i].substring(1).toLowerCase());
			}
		}
		return buffer.toString();
	}
}
