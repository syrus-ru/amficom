MODULE_NAME := resource_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar

JAVACFLAGS = -source 1.4 -target 1.4
include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)



$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/resource/*.java)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/resource.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/resource/*.class)
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/resource/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/resource/corba
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
		$(IDLDIR)/resource.idl \
		$(SRCDIR)/com/syrus/AMFICOM/resource/*.java

