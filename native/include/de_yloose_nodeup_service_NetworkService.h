/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_yloose_nodeup_service_NetworkService */

#ifndef _Included_de_yloose_nodeup_service_NetworkService
#define _Included_de_yloose_nodeup_service_NetworkService
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_yloose_nodeup_service_NetworkService
 * Method:    initNetworkInterface
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_de_yloose_nodeup_service_NetworkService_initNetworkInterface
  (JNIEnv *, jobject, jstring);

/*
 * Class:     de_yloose_nodeup_service_NetworkService
 * Method:    startListeningLoop
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_yloose_nodeup_service_NetworkService_startListeningLoop
  (JNIEnv *, jobject, jlong);

/*
 * Class:     de_yloose_nodeup_service_NetworkService
 * Method:    sendPacket
 * Signature: (J[BI)I
 */
JNIEXPORT jint JNICALL Java_de_yloose_nodeup_service_NetworkService_sendPacket
  (JNIEnv *, jobject, jlong, jbyteArray, jint);

#ifdef __cplusplus
}
#endif
#endif
