MODULE_NAME := map

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/map.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)


LIBNAME_GENERALXML = generalxml.jar
LIBNAME_CONFIGURATIONXML = configurationxml.jar
LIBNAME_MAPXML = $(MODULE_NAME)xml.jar

DEPENDENCIES = \
configuration \
general \
administration \
resource \
util \
bugs

XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_MAPXML):$(LIBDIR)/$(LIBNAME_GENERALXML)

EXTCLASSPATH := $(TROVE4JCLASSPATH)

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/map/*.java)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/map


.mach: .corba .xml $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/map/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/map/corba/.cvsignore | grep .java)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) $(LIBNAME_MAPXML) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/map/corba/*.java
	touch $@


#### XML ####
.PHONY: xml
xml: .xml

.xml: $(LIBNAME_MAPXML)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_MAPXML) $(LIBDIR)

$(LIBNAME_MAPXML): 
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/configuration

.PHONY: clean
clean: cleanmach cleancorba cleanxml
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/map/corba
	$(RM) .corba

.PHONY: cleanxml xmlclean
cleanxml xmlclean:
	$(RM) $(LIBNAME_MAPXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/map/xml/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/map/xml && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/map/xml; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/map/xml
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

.PHONY: pmd
pmd:
	$(JAVA) -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/map text $(PMD_RULES) 
