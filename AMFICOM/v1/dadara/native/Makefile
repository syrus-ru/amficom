include Makefile.inc

.PHONY:	clean common analyse fit an2 JNI thresh BreakL wavelet head
.PHONY: all

all: $(OUT)

$(OUT):	head common analyse fit an2 JNI thresh BreakL wavelet
	$(LD) $(LDFLAGS) \
	$(COMMON_DIR)/*.$(OBJ_EXT) \
	$(ANALYSE_DIR)/*.$(OBJ_EXT) \
	$(AN2_DIR)/*.$(OBJ_EXT) \
	$(FIT_DIR)/*.$(OBJ_EXT) \
	$(JNI_DIR)/*.$(OBJ_EXT) \
	$(BREAKL_DIR)/*.$(OBJ_EXT) \
	$(THRESH_DIR)/*.$(OBJ_EXT) \
	$(WAVELET_DIR)/*.$(OBJ_EXT) \
	$(LD_OUTPUT_FLAG)$@ \
	$(LDLIBS)

common:
	$(MAKE) -C ./common

analyse:
	$(MAKE) -C ./analyse

fit:
	$(MAKE) -C ./fit

an2:
	$(MAKE) -C ./an2

JNI:
	$(MAKE) -C ./JNI

BreakL:
	$(MAKE) -C ./BreakL

thresh:
	$(MAKE) -C ./thresh

wavelet:
	$(MAKE) -C ./wavelet

clean:
	$(MAKE) -C common clean
	$(MAKE) -C analyse clean
	$(MAKE) -C fit clean
	$(MAKE) -C an2 clean
	$(MAKE) -C JNI clean
	$(MAKE) -C BreakL clean
	$(MAKE) -C thresh clean
	$(MAKE) -C wavelet clean
	rm -f $(OUT) dadara.exp dadara.lib 

head:
	$(JAVAH) -classpath $(CLASS_DIR) -d common com.syrus.AMFICOM.analysis.dadara.ModelFunction
	$(JAVAH) -classpath $(CLASS_DIR) -d common com.syrus.AMFICOM.analysis.CoreAnalysisManager
	$(JAVAH) -classpath $(CLASS_DIR) -d common com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent
	$(JAVAH) -classpath $(CLASS_DIR) -d common com.syrus.AMFICOM.analysis.dadara.Wavelet
