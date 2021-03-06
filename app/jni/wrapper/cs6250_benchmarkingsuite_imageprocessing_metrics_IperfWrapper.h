/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper */

#ifndef _Included_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
#define _Included_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    initNativeIperfImpl
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_initNativeIperfImpl
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    cleanUp
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_cleanUp
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    testRoleImpl
 * Signature: (C)V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_testRoleImpl
  (JNIEnv *, jobject, jchar);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    defaultsImpl
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_defaultsImpl
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    hostnameImpl
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_hostnameImpl
  (JNIEnv *, jobject, jstring);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    tempFileTemplateImpl
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_tempFileTemplateImpl
  (JNIEnv *, jobject, jstring);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    durationImpl
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_durationImpl
  (JNIEnv *, jobject, jint);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    logFileImpl
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_logFileImpl
  (JNIEnv *, jobject, jstring);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    runClientImpl
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_runClientImpl
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    outputJsonImpl
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_outputJsonImpl
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    getUploadedBytes
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getUploadedBytes
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    getDownloadedBytes
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getDownloadedBytes
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    getTimeTaken
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getTimeTaken
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    getHostCpuUtilization
 * Signature: ()[D
 */
JNIEXPORT jdoubleArray JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getHostCpuUtilization
  (JNIEnv *, jobject);

/*
 * Class:     cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper
 * Method:    getServerCpuUtilization
 * Signature: ()[D
 */
JNIEXPORT jdoubleArray JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getServerCpuUtilization
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
