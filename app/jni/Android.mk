LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := launchbed
LOCAL_SRC_FILES := cjson.c iperf_error.c iperf_server_api.c iperf_util.c t_timer.c tcp_info.c units.c iperf_api.c iperf_locale.c iperf_tcp.c tcp_window_size.c iperf_client_api.c	iperf_sctp.c iperf_udp.c net.c timer.c
LOCAL_LDLIBS	:= -llog

include $(BUILD_SHARED_LIBRARY)
