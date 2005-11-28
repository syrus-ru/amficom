MODULE_NAME := configuration

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/configuration.idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)


LIBNAME_GENERALXML = generalxml.jar
LIBNAME_CONFIGURATIONXML = $(MODULE_NAME)xml.jar

DEPENDENCIES = \
administration \
general \
util \
bugs

XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_CONFIGURATIONXML):$(LIBDIR)/$(LIBNAME_GENERALXML)
EXTCLASSPATH := $(TROVE4JCLASSPATH)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/configuration/*.java)


$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/configuration/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/configuration/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: .corba .xml $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/configuration/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/configuration/corba/.cvsignore | grep .java)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl .xml
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/corba/*.java
	touch $@

.PHONY: xml
xml: .xml

.xml: $(LIBNAME_CONFIGURATIONXML)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_CONFIGURATIONXML) $(LIBDIR)

$(LIBNAME_CONFIGURATIONXML):
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/general

.PHONY: clean
clean: cleanmach cleancorba xmlclean
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) .corba

.PHONY: xmlclean cleanxml
xmlclean cleanxml:
	$(RM) $(LIBNAME_CONFIGURATIONXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/configuration/xml/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/configuration/xml && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/configuration/xml; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/configuration/xml
	$(RM) .xml

#### Install ####
.PHONY: install
install: xmlinstall $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES) \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig
