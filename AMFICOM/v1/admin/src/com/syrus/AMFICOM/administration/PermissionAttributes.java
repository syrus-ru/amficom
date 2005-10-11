/*-
* $Id: PermissionAttributes.java,v 1.14 2005/10/11 08:50:46 bob Exp $
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


/**
 * @version $Revision: 1.14 $, $Date: 2005/10/11 08:50:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class PermissionAttributes extends StorableObject {

	// TODO generate serialVersionUID when all enum will be made 

	public static enum Module {
		ADMINISTRATION,
		SCHEME;
		
		private static final String KEY_ROOT = "Module.Description.";
		
		private static Module[] values = values();
		private static List<Module> valueList = 
			Collections.unmodifiableList(Arrays.asList(values));
		
		private final String codename;
		
		private Module(){
			this.codename = getJavaNamingStyleName(this.name());
		}
		
		public static final Module valueOf(final int ordinal) {			
			return values[ordinal];
		} 
		
		public static final Module valueOf(final Integer ordinal) {
			return valueOf(ordinal.intValue());
		}

		public static Module valueOf(final IdlModule idlModule) {
			return values[idlModule.value()];
		}
		
		public static final List<Module> getValueList() {
			return valueList;
		}		
		
		public final IdlModule getTransferable() {
			return IdlModule.from_int(this.ordinal());
		}
		
		public final String getCodename() {
			return this.codename;
		}
		
		public final String getDescription() {
			return LangModelAdministation.getString(KEY_ROOT + this.codename);
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
		CREATE_DOMAIN(true);
		
		private final boolean	enable;

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
	 * Scheme permission enum
	 * 
	 *  <p><b>ОХНУНГ !!! Стасег и иво прадалжатили, Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Scheme implements SwitchableGroupNumber {
		ENTER(true),
		CREATE_AND_EDIT(true),
		SAVING(true);
		
		private final boolean	enable;
		
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
	
	public static enum PermissionCodename {
		ADMINSTRATION_ENTER(Adminstration.ENTER),
		ADMINSTRATION_CREATE_DOMAIN(Adminstration.CREATE_DOMAIN),
		
		SCHEME_ENTER(Scheme.ENTER),
		SCHEME_CREATE_AND_EDIT(Scheme.CREATE_AND_EDIT),
		SCHEME_SAVING(Scheme.SAVING);
		
		private final Enum	e;		

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
		
		final String getCodename() {
			return this.codename;
		}
		
		final int getOrderInGroup() {
			return this.e.ordinal();
		}
		
		public final Module getModule() {
			return ((SwitchableGroupNumber)this.e).getModule();
		}
		
		public boolean isEnable() {
			return ((SwitchableGroupNumber)this.e).isEnable();
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
		}
		catch (ApplicationException ae) {
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
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPermissionAttributes pat = (IdlPermissionAttributes)transferable;
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
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlPermissionAttributes getTransferable(final ORB orb) {
		assert this.isValid(): ErrorMessages.OBJECT_STATE_ILLEGAL;
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
				&& this.domainId != null && !this.domainId.isVoid()
				&& this.parentId != null && !this.parentId.isVoid() 
				&& this.permissions != null;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param userId
	 * @param permissions
	 * @param denyMask
	 * @return new instance for client
	 * @throws CreateObjectException
	 */
	public static PermissionAttributes createInstance(final Identifier creatorId,
	                                                  final Identifier domainId,
	                                                  final Identifier userId,
	                                                  final Module module,
	                                                  final BigInteger permissions,
	                                                  final BigInteger denyMask) 
	throws CreateObjectException {
		try {
			final PermissionAttributes permissionAttributes = 
				new PermissionAttributes(
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERMATTR_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					userId,
					module,
					permissions,
		            denyMask);

			assert permissionAttributes.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			permissionAttributes.markAsChanged();

			return permissionAttributes;
		}
		catch (IdentifierGenerationException ige) {
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
	 * <p><b>Clients must never explicitly call this method.</b></p>
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
		assert Log.debugMessage("Permissions.setPermissionEnable | " 
				+ (enable ? "en" : "dis") + "able " 
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
	public final BigInteger getPermissions(){
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
	public final BigInteger getDenyMask(){
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
	 * generate string  as javaNamingStyle from string
	 */
	static String getJavaNamingStyleName(final String string) {
        final StringBuffer buffer = new StringBuffer();
		String[] strings = string.split("_");
		for(int i = 0; i < strings.length; i++) {
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

