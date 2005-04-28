MODULE_NAME = loginserver_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE := $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/event.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/eventserver_interface.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/loginserver/*.java)

all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/loginserver/corba/LoginServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/loginserver/corba/LoginServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/loginserver/corba/_LoginServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/loginserver/corba/LoginServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/loginserver/corba/LoginServerHolder.class
	$(CP) $(LIBNAME_INTERFACE) $(LIBDIR)

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) idl/loginserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/loginserver/corba/*.java
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
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/loginserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/loginserver/corba
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
		idl/loginserver.idl \
		$(addprefix $(SRCDIR)/,$(SOURCES))

