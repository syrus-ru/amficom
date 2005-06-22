MODULE_NAME = mapview_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

JAVAC=javac

JAVAC=javac

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)


APPCLASSPATH = $(LIBDIR)/resource.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/generalxml.jar:$(LIBDIR)/mapxml.jar:$(LIBDIR)/map.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mapview/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach


#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES)
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/map text $(PMD_RULES) 

