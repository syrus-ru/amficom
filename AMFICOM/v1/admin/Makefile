MODULE_NAME = administration_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/general.jar:$(LIBDIR)/util.jar

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)


SOURCES := com/syrus/AMFICOM/administration/DatabaseAdministrationObjectLoader.java \
		com/syrus/AMFICOM/administration/AdministrationObjectLoader.java \
		com/syrus/AMFICOM/administration/AdministrationStorableObjectPool.java \
		com/syrus/AMFICOM/administration/AdministrationDatabaseContext.java \
		com/syrus/AMFICOM/administration/User.java \
		com/syrus/AMFICOM/administration/UserDatabase.java \
		com/syrus/AMFICOM/administration/Domain.java \
		com/syrus/AMFICOM/administration/DomainDatabase.java \
		com/syrus/AMFICOM/administration/DomainMember.java \
		com/syrus/AMFICOM/administration/Server.java \
		com/syrus/AMFICOM/administration/ServerDatabase.java \
		com/syrus/AMFICOM/administration/MCM.java \
		com/syrus/AMFICOM/administration/MCMDatabase.java \
		com/syrus/AMFICOM/administration/StringFieldCondition.java \
		com/syrus/AMFICOM/administration/LinkedIdsConditionImpl.java \
		com/syrus/AMFICOM/administration/DomainCondition.java


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(addprefix $(SRCDIR)/,$(SOURCES))
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/administration.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/administration/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/administration/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/administration/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/administration.idl \
		$(SRCDIR)/com/syrus/AMFICOM/administration/*.java

