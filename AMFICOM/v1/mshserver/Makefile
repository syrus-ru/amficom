MODULE_NAME = mshserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mshserver/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/map/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/map.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/csbridge.jar


$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDLJ)

all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mshserver/corba/MSHServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mshserver/corba/MSHServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mshserver/corba/_MSHServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mshserver/corba/MSHServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mshserver/corba/MSHServerHolder.class
	$(CP) $@ $(LIBDIR)

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) \
		$(IDLDIR)/mshserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mshserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mshserver/*.class
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/map/*.class
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/scheme/*.class
	rm -f .mach

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/mshserver/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mshserver/corba
	rm -f .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/mshserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mshserver/*.java


lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)
