// provides C <-> Java conversion:
//	 ModelF - com.syrus.AMFICOM.analysis.dadara.ModelFunction
//   EventP - com.syrus.AMFICOM.analysis.dadara.EventParams
// implements native methods

#ifndef _JAVA_BRIDGE_H
#define _JAVA_BRIDGE_H

#include <jni.h>

#include "../Common/ModelF.h"
#include "../Common/EventP.h"

// J ModelFunction -> C ModelF
// returns 0 if ok, returns not 0 if error, assertion fails if JNI problem
int ModelF_J2C(JNIEnv *env, jobject obj, ModelF &out);

// C ModelF -> J ModelFunction
// assertion fails if not success
jobject ModelF_C2J(JNIEnv *env, ModelF &mf);

// updates: C ModelF -> existing JModelFunction
// assertion fails if not success
void ModelF_C2J_update(JNIEnv *env, ModelF &mf, jobject mf_obj);

// J ReflectogramEvent -> C EventP
// returns 0 if success
//int EventP_J2C(JNIEnv *env, jobject obj, EventP &out);

// J ReflectogramEvent[] -> C EventP[]
// returns null if array is empty
// assertion fails if some problem
// please remember to delete[] the resulting array
//EventP *EventP_J2Cnew_arr(JNIEnv *env, jobjectArray array);

// C EventP -> J ReflectogramEvent
// assertion fails if not success
jobject EventP_C2J(JNIEnv *env, EventP &ep);

// C EventP[] -> J ReflectogramEvent[]
// assertion fails if not success
jobjectArray EventP_C2J_arr(JNIEnv *env, EventP *ep, int number, FILE *logf);

#endif

