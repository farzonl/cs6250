#include "cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <getopt.h>
#include <errno.h>
#include <signal.h>
#include <unistd.h>
#ifdef HAVE_STDINT_H
#include <stdint.h>
#endif
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#ifdef HAVE_STDINT_H
#include <stdint.h>
#endif
#include <netinet/tcp.h>
//#include <stdbool.h>
#include "iperf.h"
#include "iperf_api.h"
//#include "iperf_client_api.h"
#include "units.h"
#include "iperf_locale.h"
#include "net.h"
#include "iperf_util.h"
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "test", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "test", __VA_ARGS__)

static int iperf_run(struct iperf_test *);
struct iperf_test* main_launch(int argc, char **argv);
struct iperf_test *gTest = NULL;

typedef struct iperf_totals
{
    iperf_size_t total_sent;
    iperf_size_t total_recv;
    double    total_time;
}iperf_totals;

iperf_totals iTotals;

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

    iTotals.total_sent = total_sent;
    iTotals.total_recv = total_received;
    iTotals.total_time = end_time - start_time;
}

void iperf_test* initClient(const jbyte *server_ip_j)
{
    char *server_ip = malloc(strlen(server_ip_j) + 1);
    strncpy(server_ip, server_ip_j, strlen(server_ip_j));
    server_ip[strlen(server_ip_j)] = '\0';
    LOGI("server ip from main: %s", server_ip);
    char *argv[] = {"iperf", "-c", server_ip};
    return main_launch(3, argv);

}


JNIEXPORT jboolean JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_launchBandwidthTest
  (JNIEnv *jenv, jobject jobj, jstring serverIp)
{
    if (gTest) {
        iperf_free_test(gTest);
    }
    gTest = NULL;
    jboolean is_copy;
    const jbyte *server_ip_j = (*jenv)->GetStringUTFChars(jenv, serverIp, &is_copy) ;

    char *server_ip = malloc(strlen(server_ip_j) + 1);
    strncpy(server_ip, server_ip_j, strlen(server_ip_j));
    server_ip[strlen(server_ip_j)] = '\0';
    LOGI("server ip from main: %s", server_ip);
    char *argv[] = {"iperf", "-c", server_ip};
	gTest = main_launch(3, argv);

    if (!gTest) {
        iperf_get_results(gTest);
        return 1;
    }
    return 0;
}

JNIEXPORT void JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_initFileTemplate
        (JNIEnv *env, jobject thiz, jstring j_template)
{
    const char *template = (*env)->GetStringUTFChars(env, j_template, 0);

    iperf_set_test_template(gTest, template);

    (*env)->ReleaseStringUTFChars(env, j_template, template);
}

JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_getUploadedBytes
  (JNIEnv * jenv, jobject jobj)
{
	if (!gTest)
		return -1;

    LOGI("total sent : %llu\n", iTotals.total_sent);
	return iTotals.total_sent;
}


JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_getDownloadedBytes
  (JNIEnv * jenv, jobject jobj)
{
	if (!gTest)
	{
		return -1;
	}
    LOGI("total recv : %llu\n", iTotals.total_recv);
	return iTotals.total_recv;
}

JNIEXPORT jdouble JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_getTimeTaken
  (JNIEnv * jenv, jobject jobj)
{
	if (!gTest)
	{
		return -1;
	}
	return iTotals.total_time;
}

JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_getHostCpuUtilization
  (JNIEnv * jenv, jobject jobj)
{
	if (!gTest)
	{
		return -1;
	}
	return gTest->cpu_util;
}

JNIEXPORT jlong JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_getServerCpuUtilization
  (JNIEnv * jenv, jobject jobj)
{
	if (!gTest)
		return -1;

	return gTest->remote_cpu_util;
}
JNIEXPORT jboolean JNICALL Java_cs6250_benchmarkingsuite_imageprocessing_metrics_BandwidthMeasurement_cleanup
                          (JNIEnv * jenv, jobject jobj)
{
    if (gTest)
    {
        iperf_free_test(gTest);
    }
}

/**************************************************************************/
struct iperf_test*
main_launch(int argc, char **argv)
{
    struct iperf_test *test;

    // XXX: Setting the process affinity requires root on most systems.
    //      Is this a feature we really need?
#ifdef TEST_PROC_AFFINITY
    /* didnt seem to work.... */
    /*
     * increasing the priority of the process to minimise packet generation
     * delay
     */
    int rc = setpriority(PRIO_PROCESS, 0, -15);

    if (rc < 0) {
        perror("setpriority:");
        fprintf(stderr, "setting priority to valid level\n");
        rc = setpriority(PRIO_PROCESS, 0, 0);
    }

    /* setting the affinity of the process  */
    cpu_set_t cpu_set;
    int affinity = -1;
    int ncores = 1;

    sched_getaffinity(0, sizeof(cpu_set_t), &cpu_set);
    if (errno)
        perror("couldn't get affinity:");

    if ((ncores = sysconf(_SC_NPROCESSORS_CONF)) <= 0)
        err("sysconf: couldn't get _SC_NPROCESSORS_CONF");

    CPU_ZERO(&cpu_set);
    CPU_SET(affinity, &cpu_set);
    if (sched_setaffinity(0, sizeof(cpu_set_t), &cpu_set) != 0)
        err("couldn't change CPU affinity");
#endif

    test = iperf_new_test();
    if (!test)
        iperf_errexit(NULL, "create new test error - %s", iperf_strerror(i_errno));
    iperf_defaults(test);	/* sets defaults */

    if (iperf_parse_arguments(test, argc, argv) < 0) {
        iperf_err(test, "parameter error - %s", iperf_strerror(i_errno));
        fprintf(stderr, "\n");
        usage_long();
        exit(1);
    }

    if (iperf_run(test) < 0)
        iperf_errexit(test, "error - %s", iperf_strerror(i_errno));

    return test;
}


static jmp_buf sigend_jmp_buf;

static void
sigend_handler(int sig)
{
    longjmp(sigend_jmp_buf, 1);
}

/**************************************************************************/
static int
iperf_run(struct iperf_test *test)
{
    /* Termination signals. */
    iperf_catch_sigend(sigend_handler);
    if (setjmp(sigend_jmp_buf))
        iperf_got_sigend(test);

    switch (test->role) {
        case 's':
            if (test->daemon) {
                int rc = daemon(0, 0);
                if (rc < 0) {
                    i_errno = IEDAEMON;
                    iperf_errexit(test, "error - %s", iperf_strerror(i_errno));
                }
            }
            if (iperf_create_pidfile(test) < 0) {
                i_errno = IEPIDFILE;
                iperf_errexit(test, "error - %s", iperf_strerror(i_errno));
            }
            for (;;) {
                int rc;
                rc = iperf_run_server(test);
                if (rc < 0) {
                    iperf_err(test, "error - %s", iperf_strerror(i_errno));
                    if (rc < -1) {
                        iperf_errexit(test, "exiting");
                        break;
                    }
                }
                iperf_reset_test(test);
                if (iperf_get_test_one_off(test))
                    break;
            }
            iperf_delete_pidfile(test);
            break;
        case 'c': {
            test->outfile = fopen(test->logfile, "a+");
            int clientPerfRunErrorCode = iperf_run_client(test);
            if (clientPerfRunErrorCode < 0) {
                LOGI("client iperf setup failed with error code: %i", clientPerfRunErrorCode);
                //iperf_errexit(test, "error - %s", iperf_strerror(i_errno));
            }
        }
            break;
        default:
            usage();
            break;
    }

    iperf_catch_sigend(SIG_DFL);

    return 0;
}