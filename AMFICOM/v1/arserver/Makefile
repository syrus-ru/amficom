MODULE_NAME = arserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/administration.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)

all: $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ $(wildcard -C $(CLASSDIR) com/syrus/AMFICOM/arserver/corba/*.class)	

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) $(wildcard $(SRCDIR)/com/syrus/AMFICOM/arserver/*.java)
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
	$(RM) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/arserver/*.class)
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/arserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/arserver/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE)
	$(CP) $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)

#### Archive ####
#.PHONY:	arch
#arch:
#	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/cmserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/*.java
