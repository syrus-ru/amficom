MODULE_NAME = event_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/util.jar


SOURCES := com/syrus/AMFICOM/event/EventType.java \
	com/syrus/AMFICOM/event/EventTypeDatabase.java \
	com/syrus/AMFICOM/event/Event.java \
	com/syrus/AMFICOM/event/EventDatabase.java \
	com/syrus/AMFICOM/event/EventParameter.java


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/event

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(JDBCCLASSPATH):$(CLASSDIR) \
		$(addprefix $(SRCDIR)/,$(SOURCES))
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/event.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/event/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/event/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/event/corba
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
		$(IDLDIR)/event.idl \
		$(addprefix $(SRCDIR)/,$(SOURCES))
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/event text $(PMD_RULES) 

