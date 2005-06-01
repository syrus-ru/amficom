MODULE_NAME = arserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/arserver/*.java)


all:	$(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/arserver/corba/ARServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/arserver/corba/ARServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/arserver/corba/_ARServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/arserver/corba/ARServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/arserver/corba/ARServerHolder.class
	$(CP) $@ $(LIBDIR)

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) \
		$(IDLDIR)/arserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/arserver/corba/*.java
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
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/arserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/arserver/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)
	$(CP) $^

### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/arserver.idl \
		$(SOURCES)

