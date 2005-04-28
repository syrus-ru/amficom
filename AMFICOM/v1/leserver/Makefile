MODULE_NAME = leserver_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE := $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/event.jar:$(LIBDIR)/csbridge.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/leserver/*.java)

all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/_LoginServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/_EventServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHolder.class
	$(CP) $(LIBNAME_INTERFACE) $(LIBDIR)

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) idl/leserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/leserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE)
	$(CP) $^ $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		idl/leserver.idl \
		$(addprefix $(SRCDIR)/,$(SOURCES))

