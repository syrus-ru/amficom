include Makefile.inc

.PHONY:	clean common analyse fit an2 JNI BreakL head

$(OUT):	common analyse fit an2 JNI BreakL
	$(LD) $(LDFLAGS) \
	$(COMMON_DIR)/*.$(OBJ_EXT) \
	$(ANALYSE_DIR)/*.$(OBJ_EXT) \
	$(AN2_DIR)/*.$(OBJ_EXT) \
	$(FIT_DIR)/*.$(OBJ_EXT) \
	$(JNI_DIR)/*.$(OBJ_EXT) \
	$(BREAKL_DIR)/*.$(OBJ_EXT) \
	$(LD_OUTPUT_FLAG)$@

common:
	make -C ./common

analyse:
	make -C ./analyse

fit:
	make -C ./fit

an2:
	make -C ./an2

JNI:
	make -C ./JNI

BreakL:
	make -C ./BreakL

clean:
	make -C common clean
	make -C analyse clean
	make -C fit clean
	make -C an2 clean
	make -C JNI clean
	make -C BreakL clean
	rm -f dadara.dll dadara.exp dadara.lib 

head:
	$(JAVAH) -classpath $(CLASS_DIR) -d Common com.syrus.AMFICOM.analysis.dadara.ModelFunction
	$(JAVAH) -classpath $(CLASS_DIR) -d Common com.syrus.AMFICOM.analysis.CoreAnalysisManager
	$(JAVAH) -classpath $(CLASS_DIR) -d Common com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent
