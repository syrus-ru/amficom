MODULE_NAME = mscharserver_v1

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
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDLJ)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/map.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar:$(EXTLIBDIR)/jdom.jar:$(EXTLIBDIR)/micsys.jar:$(EXTLIBDIR)/miutil.jar:$(EXTLIBDIR)/mxj.jar:$(EXTLIBDIR)/mxjgeom.jar



SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mscharserver/*.java)


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/_MscharServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServerHolder.class
	$(CP) $@ $(LIBDIR)

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) \
		$(IDLDIR)/mscharserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba/*.java
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
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mscharserver/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/mscharserver.idl \
		$(SOURCES)


lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)
