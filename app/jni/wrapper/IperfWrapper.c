#include "cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper.h"
#include <stdio.h>
#include <sys/select.h>
#include <iperf_api.h>
#include <android/log.h>
#include <iperf.h>
#include <iperf_util.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "IPERF", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "IPERF", __VA_ARGS__)

typedef struct iperf_totals
{
    size_t total_sent;
    size_t total_recv;
    double    total_time;
}iperf_totals;

iperf_totals gITotals;
struct iperf_test *gTest = NULL;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env;
    if ((*vm)->GetEnv(vm, (void**)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    //  Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.

    return JNI_VERSION_1_6;
}

void
iperf_get_results(struct iperf_test *test)
{
    int total_packets = 0, lost_packets = 0;
    struct iperf_stream *sp = NULL;
    iperf_size_t bytes = 0, bytes_sent = 0, bytes_received = 0;
    iperf_size_t total_sent = 0, total_received = 0;
    double start_time, end_time, avg_jitter;
    struct iperf_interval_results *ip = NULL;

    start_time = 0.;
    sp = SLIST_FIRST(&test->streams);
    end_time = timeval_diff(&sp->result->start_time, &sp->result->end_time);
    SLIST_FOREACH(sp, &test->streams, streams) {
        bytes_sent = sp->result->bytes_sent;
        bytes_received = sp->result->bytes_received;
        total_sent += bytes_sent;
        total_received += bytes_received;
    }

    gITotals.total_sent = (size_t) total_sent;
    gITotals.total_recv = (size_t) total_received;
    gITotals.total_time = end_time - start_time;
}

JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getUploadedBytes
  (JNIEnv * jenv, jobject jobj)
{
    LOGI("total sent : %zu\n", gITotals.total_sent);
	return gITotals.total_sent;
}


JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getDownloadedBytes
  (JNIEnv * jenv, jobject jobj)
{
    LOGI("total recv : %zu\n", gITotals.total_recv);
	return gITotals.total_recv;
}

JNIEXPORT jdouble JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getTimeTaken
  (JNIEnv * jenv, jobject jobj)
{
	return gITotals.total_time;
}

JNIEXPORT jdoubleArray JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getHostCpuUtilization
  (JNIEnv * jenv, jobject jobj, jlong ref)
{
    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    jdoubleArray result;
	if (test) {
        int len = sizeof(test->cpu_util) / sizeof(test->cpu_util[0]);
        result = (*jenv)->NewDoubleArray(jenv, len);

        (*jenv)->SetDoubleArrayRegion(jenv, result, 0, len, test->cpu_util);
    }
    return result;
}

JNIEXPORT jdoubleArray JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getServerCpuUtilization
  (JNIEnv * jenv, jobject jobj, jlong ref)
{
    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    jdoubleArray result;

    if (!test) {
        int len = sizeof(test->remote_cpu_util) / sizeof(test->remote_cpu_util[0]);
        result = (*jenv)->NewDoubleArray(jenv, len);

        (*jenv)->SetDoubleArrayRegion(jenv, result, 0, len, test->remote_cpu_util);
    }
	return result;
}


JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_newTestImpl
( JNIEnv* env, jobject thiz )
{

    struct iperf_test *test;
    test = iperf_new_test();
    LOGI("new test %lld", (long long)test);

    return (jlong)(intptr_t)test;

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_freeTestImpl
( JNIEnv* env, jobject thiz, jlong ref)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    iperf_free_test(test);

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_testRoleImpl
( JNIEnv* env, jobject thiz, jlong ref, jchar role)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    LOGI("set role %c", (char)role);

    iperf_set_test_role(test, (char)role );

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_defaultsImpl
( JNIEnv* env, jobject thiz, jlong ref)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    iperf_defaults(test);

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_hostnameImpl
( JNIEnv* env, jobject thiz, jlong ref, jstring j_hostname)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    const char *hostname = (*env)->GetStringUTFChars(env, j_hostname, 0);

    iperf_set_test_server_hostname(test, hostname);

    (*env)->ReleaseStringUTFChars(env, j_hostname, hostname);


}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_tempFileTemplateImpl
( JNIEnv* env, jobject thiz, jlong ref, jstring j_template)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    const char *template = (*env)->GetStringUTFChars(env, j_template, 0);

    iperf_set_test_template(test, template);

    (*env)->ReleaseStringUTFChars(env, j_template, template);


}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_durationImpl
( JNIEnv* env, jobject thiz, jlong ref, jint duration)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    iperf_set_test_duration(test, duration);


}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_runClientImpl
( JNIEnv* env, jobject thiz, jlong ref)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    test->outfile = fopen(test->logfile, "a+");
    iperf_run_client(test);
    iperf_get_results(test);

}


JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_outputJsonImpl
( JNIEnv* env, jobject thiz, jlong ref, jboolean useJson)
{
    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    iperf_set_test_json_output(test, (int)useJson);
}

/*
 * Command line options
 */


// OPT_LOG
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_logFileImpl
( JNIEnv* env, jobject thiz, jlong ref, jstring j_logfile)
{

    struct iperf_test *test;
    test = (struct iperf_test *)(intptr_t)ref;

    const char *logfile = (*env)->GetStringUTFChars(env, j_logfile, 0);

    test->logfile = strdup(logfile);

    (*env)->ReleaseStringUTFChars(env, j_logfile, logfile);
}
