#include "cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper.h"
#include <sys/select.h>
#include <stdio.h>
#include "iperf.h"
#include "iperf_api.h"
#include "iperf_util.h"
#include <android/log.h>

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
    LOGI("JNI Loaded");
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
    LOGI("Time Taken: %f", gITotals.total_time);
	return gITotals.total_time;
}

JNIEXPORT jdoubleArray JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getHostCpuUtilization
  (JNIEnv * jenv, jobject jobj)
{
    jdoubleArray result;
	if (gTest) {
        int len = sizeof(gTest->cpu_util) / sizeof(gTest->cpu_util[0]);
        result = (*jenv)->NewDoubleArray(jenv, len);

        (*jenv)->SetDoubleArrayRegion(jenv, result, 0, len, gTest->cpu_util);
    }
    return result;
}

JNIEXPORT jdoubleArray JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_getServerCpuUtilization
  (JNIEnv * jenv, jobject jobj)
{
    jdoubleArray result;

    if (gTest) {
        int len = sizeof(gTest->remote_cpu_util) / sizeof(gTest->remote_cpu_util[0]);
        result = (*jenv)->NewDoubleArray(jenv, len);

        (*jenv)->SetDoubleArrayRegion(jenv, result, 0, len, gTest->remote_cpu_util);
    }
	return result;
}


JNIEXPORT jboolean JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_initNativeIperfImpl
( JNIEnv* env, jobject thiz )
{
    LOGI("creating new IPERF Test");
    gTest = iperf_new_test();
    if(gTest == NULL)
    {
        LOGI("IPERF Test failed to initialize");
        return JNI_FALSE;
    }
    LOGI("IPERF Test initialization succeed");
    return JNI_TRUE;

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_cleanUp
( JNIEnv* env, jobject thiz)
{

    if(gTest == NULL)
    {
        return;
    }
    iperf_free_test(gTest);
    LOGI("IPERF Test run memory freed");
    gTest = NULL;
}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_testRoleImpl
( JNIEnv* env, jobject thiz, jchar role)
{
    if(gTest == NULL)
    {
        return;
    }

    LOGI("set role %c", (char)role);

    iperf_set_test_role(gTest, (char)role );

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_defaultsImpl
( JNIEnv* env, jobject thiz)
{
    if(gTest == NULL)
    {
        return;
    }

    iperf_defaults(gTest);
    LOGI("defaults set");

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_hostnameImpl
( JNIEnv* env, jobject thiz, jstring j_hostname)
{
    if(gTest == NULL)
    {
        return;
    }

    const char *hostname = (*env)->GetStringUTFChars(env, j_hostname, 0);

    iperf_set_test_server_hostname(gTest, hostname);
    LOGI("host name set: %s", hostname);
    (*env)->ReleaseStringUTFChars(env, j_hostname, hostname);
}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_tempFileTemplateImpl
( JNIEnv* env, jobject thiz, jstring j_template)
{
    if(gTest == NULL)
    {
        return;
    }

    const char *template = (*env)->GetStringUTFChars(env, j_template, 0);

    iperf_set_test_template(gTest, template);
    LOGI("temp file template set: %s", template);
    (*env)->ReleaseStringUTFChars(env, j_template, template);

}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_durationImpl
( JNIEnv* env, jobject thiz, jint duration)
{
    if(gTest == NULL)
    {
        return;
    }

    iperf_set_test_duration(gTest, duration);
    LOGI("Test duration set to : %i", duration);
}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_runClientImpl
( JNIEnv* env, jobject thiz)
{
    if(gTest == NULL)
    {
        return;
    }

    gTest->outfile = fopen(gTest->logfile, "a+");
    iperf_run_client(gTest);
    iperf_get_results(gTest);

}


JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_outputJsonImpl
( JNIEnv* env, jobject thiz, jboolean useJson)
{
    if(gTest == NULL)
    {
        return;
    }

    iperf_set_test_json_output(gTest, (int)useJson);
    LOGI("Test output  set to json");
}

/*
 * Command line options
 */


// OPT_LOG
JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_IperfWrapper_logFileImpl
( JNIEnv* env, jobject thiz, jstring j_logfile)
{
    if(gTest == NULL)
    {
        return;
    }

    const char *logfile = (*env)->GetStringUTFChars(env, j_logfile, 0);

    gTest->logfile = strdup(logfile);

    LOGI("log file path set to: %s", logfile);
    (*env)->ReleaseStringUTFChars(env, j_logfile, logfile);
}
