MODULE_NAME := resource

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/resource.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)


LIBNAME_GENERALXML=generalxml.jar

DEPENDENCIES = \
util \
general

EXTCLASSPATH := $(TROVE4JCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/resource/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/resource/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/resource/corba/.cvsignore | grep .java)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/corba/*.java
	touch $@

.PHONY: clean
clean: cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/resource/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/resource.idl \
		$(SOURCES)

