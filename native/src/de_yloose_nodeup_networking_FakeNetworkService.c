#include "../include/de_yloose_nodeup_networking_NetworkService.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <errno.h>
#include <ctype.h>

#define BACKLOG 5
#define BUF_SIZE 4096

struct ieee80211_mgmt {
	uint16_t frame_control;
	uint16_t duration;
	uint8_t da[6];
	uint8_t sa[6];
	uint8_t bssid[6];
	uint16_t seq_ctrl;

	struct {
		uint8_t category;
		struct {
			uint8_t oui[3];
			uint8_t rand[4];
			uint8_t element_id;
			uint8_t length;
			uint8_t oui_vsc[3];
			uint8_t type;
			uint8_t version;
			uint8_t variable_data[250];
		} __attribute__((packed)) vendor_specific_esp_now;
	} __attribute__ ((packed)) action;

	uint32_t fcs;
} __attribute__ ((packed));

struct ieee80211_radiotap_hdr {
	uint8_t it_version;
	uint8_t it_pad;
	uint16_t it_len;
	uint32_t it_present;
} __attribute__ ((packed));


typedef struct {
    uint16_t temp;
    uint16_t humid;
    uint16_t press;
    uint8_t voltage;
}__attribute__((packed)) datapoint_t;

const char *socket_path = "/tmp/nodeup.socket";
int sfd;

JNIEXPORT jlong JNICALL Java_de_yloose_nodeup_networking_NetworkService_initNetworkInterface(
		JNIEnv *env, jobject thisObject, jstring device) {

	struct sockaddr_un addr;

	sfd = socket(AF_UNIX, SOCK_STREAM, 0);
	printf("Server socket fd = %d\n", sfd);

	memset(&addr, 0, sizeof(struct sockaddr_un));
	addr.sun_family = AF_UNIX;
	strncpy(addr.sun_path, socket_path, sizeof(addr.sun_path) - 1);

	if (remove(socket_path) == -1 && errno != ENOENT) {
		printf("Failed to remove existing socket file.\n");
	}

	if (bind(sfd, (struct sockaddr*) &addr, sizeof(struct sockaddr_un)) == -1) {
		printf("Failed bind to socket: %s\n", strerror(errno));
	}

	if (listen(sfd, BACKLOG) == -1) {
		printf("Failed to listen on socket.\n");
	}

	return 0;
}

uint8_t *build_datapoints(int datapoint_cnt) {

	uint8_t *data = malloc(sizeof(uint8_t) * 250);
	datapoint_t datapoints[35];
	memset(datapoints, 0, sizeof(datapoint_t) * 35);

	for (size_t i = 0; i < datapoint_cnt; i++) {
		datapoints[i].temp = 1;
		datapoints[i].humid = 1;
		datapoints[i].press = 1;
		datapoints[i].voltage = 1;
	}

	memcpy(data, datapoints, sizeof(datapoint_t) * 35);
	data[248] = 0;
	data[249] = 0;
	return data;
}

char *buildFrame(int datapoint_cnt) {

	struct ieee80211_radiotap_hdr radiotap_header;
	uint8_t radiotap[10];
	struct ieee80211_mgmt mgmt;

	radiotap_header.it_version = 0;
	radiotap_header.it_pad = 0;
	radiotap_header.it_len = 18;
	radiotap_header.it_present= 388235264;

	radiotap[0] = 16;
	radiotap[1] = 2;
	radiotap[2] = 108;
	radiotap[3] = 9;
	radiotap[4] = 160;
	radiotap[5] = 0;
	radiotap[6] = 214;
	radiotap[7] = 0;
	radiotap[8] = 0;
	radiotap[9] = 0;

	mgmt.frame_control = 53248;
	mgmt.duration = 0;
	mgmt.da[0] = 255;
	mgmt.da[1] = 255;
	mgmt.da[2] = 255;
	mgmt.da[3] = 255;
	mgmt.da[4] = 255;
	mgmt.da[5] = 255;
	mgmt.sa[0] = 17;
	mgmt.sa[1] = 34;
	mgmt.sa[2] = 51;
	mgmt.sa[3] = 68;
	mgmt.sa[4] = 85;
	mgmt.sa[5] = 102;
	mgmt.bssid[0] = 255;
	mgmt.bssid[1] = 255;
	mgmt.bssid[2] = 255;
	mgmt.bssid[3] = 255;
	mgmt.bssid[4] = 255;
	mgmt.bssid[5] = 255;
	mgmt.seq_ctrl = 8192;
	mgmt.action.category = 127;
	mgmt.action.vendor_specific_esp_now.oui[0] = 24;
	mgmt.action.vendor_specific_esp_now.oui[1] = 254;
	mgmt.action.vendor_specific_esp_now.oui[2] = 52;
	mgmt.action.vendor_specific_esp_now.element_id = 221;
	mgmt.action.vendor_specific_esp_now.length = 255;
	mgmt.action.vendor_specific_esp_now.oui_vsc[0] = 24;
	mgmt.action.vendor_specific_esp_now.oui_vsc[1] = 254;
	mgmt.action.vendor_specific_esp_now.oui_vsc[2] = 52;
	mgmt.action.vendor_specific_esp_now.type = 4;
	mgmt.action.vendor_specific_esp_now.version = 1;
	char *v_data = build_datapoints(datapoint_cnt);
	memcpy(mgmt.action.vendor_specific_esp_now.variable_data, v_data, 250);
	free(v_data);
	mgmt.fcs = 0;

	char *ret = malloc(sizeof(uint8_t) * 18 + sizeof(struct ieee80211_mgmt));

	memcpy(ret, (uint8_t *) &radiotap_header, 8);
	memcpy(ret + 8, (uint8_t *) radiotap, 10);
	memcpy(ret + 18, (uint8_t *)&mgmt, sizeof(struct ieee80211_mgmt));

	return ret;
}


void send_packets_to_server(JNIEnv *env, jobject thisObject, char* data, int size) {


	jbyteArray ret_packet = (*env)->NewByteArray(env, size);

	void *temp = (*env)->GetByteArrayElements(env, (jarray) ret_packet, 0);
	memcpy(temp, data, size);
	(*env)->ReleaseByteArrayElements(env, ret_packet, temp, 0);

	jclass networkHandler = (*env)->FindClass(env,
			"de/yloose/nodeup/networking/NetworkService");
	jmethodID packet_callback = (*env)->GetMethodID(env, networkHandler,
			"nativeRecvPacketCallback", "([B)V");


	(*env)->CallVoidMethod(env, thisObject, packet_callback, ret_packet);
}

void fake_packets_to_server(JNIEnv *env, jobject thisObject, int cnt) {
	char *packet = buildFrame(cnt);
	int packet_size = sizeof(uint8_t) * 18 + sizeof(struct ieee80211_mgmt);

	send_packets_to_server(env, thisObject, packet, packet_size);

	free(packet);
}


int digits_only(const char *s) {
	while (*s != '\n') {
		if (isdigit(*s++) == 0)
			return 0;
	}
	return 1;
}

JNIEXPORT jint JNICALL Java_de_yloose_nodeup_networking_NetworkService_startListeningLoop(
		JNIEnv *env, jobject thisObject, jlong dev) {
	ssize_t numRead;

	char buf[BUF_SIZE];
	for (;;) {

		int cfd = accept(sfd, NULL, NULL);
		// printf("Accepted socket fd = %d\n", cfd);

		while ((numRead = read(cfd, buf, BUF_SIZE)) > 0) {

			if (!digits_only(buf)) {
				send(cfd, "Only integers are accepted.\n", 28, 0);
				continue;
			}

			if (numRead > 800) {
				// Send data is interpreted as a packet
				send_packets_to_server(env, thisObject, buf, numRead);

			} else {
				// Send data is interpreted as a number of datapoints to fake
				int datapoint_cnt;
				sscanf(buf, "%d", &datapoint_cnt);
				if (datapoint_cnt > 35) {
					send(cfd, "Maximum of 35.\n", 15, 0);
					continue;
				}
				fake_packets_to_server(env, thisObject, datapoint_cnt);

				char send_buf[50];
				snprintf(send_buf, 50, "Successfully faked %d datapoints.\n", datapoint_cnt);
				send(cfd, send_buf, strlen(send_buf), 0);
			}
		}

		if (numRead == -1) {
			printf("Failed to read bytes");
		}
	}
}

JNIEXPORT jint JNICALL Java_de_yloose_nodeup_networking_NetworkService_sendPacket(
		JNIEnv *env, jobject thisObject, jlong dev, jbyteArray packetArray,
		jint length) {
	return 0;
}
