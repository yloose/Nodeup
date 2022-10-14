#include "../include/de_yloose_nodeup_networking_NetworkService.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pcap.h>

typedef struct {
	JNIEnv *env;
	jobject thisObject;
} packet_handler_args_t;

int split_string(char *input, char **res) {
	const char token = ',';
	int cnt = 0;

	char *pt = strtok(input, &token);
	while (pt != NULL) {
		res[cnt] = malloc(sizeof(char) * (strlen(pt) + 1));
		memcpy(res[cnt], pt, strlen(pt) + 1);
		cnt++;
		pt = strtok(NULL, &token);
	}

	return cnt;
}

int enable_monitor_mode(const char *iface_name) {
	char cmd[200];
	snprintf(cmd, sizeof(cmd), "%s%s%s%s%s%s%s%s%s",
			"ip link set ", iface_name, " down && ",
			"iw ", iface_name, " set monitor none && ",
			"ip link set ", iface_name, " up");

	FILE *fp = popen(cmd, "r");

	if (pclose(fp) != 0)
		return -1;

	return 0;
}

int check_monitor_mode(const char *iface_name) {
	char cmd[100];
	snprintf(cmd, sizeof(cmd), "%s%s%s", "iw ", iface_name,
			" info | awk '/type/' | sed 's/\\stype//'");
	FILE *fp = popen(cmd, "r");
	char cmd_out[50];
	fgets(cmd_out, 50, fp);
	if (pclose(fp) != 0)
		return -1;

	if (strcmp("monitor", cmd_out) == 0)
		return 0;

	return 1;
}

int initialize_network_interface(const char *iface_name) {
	int ret = 0;

	// Get all wireless interfaces and check if iface_name exists
	FILE *fp = popen("iw dev | awk '/Interface/' | sed 's/\\s*Interface\\s*//' | tr '\n' ','", "r");

	char cmd_out[100];
	fgets(cmd_out, 100, fp);

	if (pclose(fp) != 0)
		return -1;

	if (strstr(cmd_out, "command not found") != NULL)
		return -3;

	char *interfaces[20];
	int count = split_string(cmd_out, interfaces);

	for (size_t i = 0; i < count; i++) {
		if (strcmp(iface_name, interfaces[i]) == 0) {
			// Check if monitor mode is enabled on interface
			switch (check_monitor_mode(iface_name)) {
			case 0:
				// Monitor mode is enabled return success
				goto rtrn;
			case 1:
				// Monitor mode is not enabled try to enable it
				if (enable_monitor_mode(iface_name) == 0)
					goto rtrn;
				// Failed to enable monitor mode
				ret = -1;
				goto rtrn;
			case -1:
				// Process returned error code
				ret = -1;
				goto rtrn;
			}
		}
	}

	// Interface not found
	ret = 1;
	goto rtrn;

	rtrn: for (size_t i = 0; i < count; i++) {
		free(interfaces[i]);
	}
	return ret;
}

JNIEXPORT jlong JNICALL Java_de_yloose_nodeup_networking_NetworkService_initNetworkInterface(
		JNIEnv *env, jobject thisObject, jstring device) {

	const char *dev = (*env)->GetStringUTFChars(env, device, NULL);
	int init_iface_ret = initialize_network_interface(dev);
	if (init_iface_ret != 0)
		return (long) init_iface_ret;

	char error_buffer[PCAP_ERRBUF_SIZE];
	struct bpf_program filter;
	char filter_exp[] = "link[0]=0xD0 and ether dst FF:FF:FF:FF:FF:FF";

	pcap_t *device_handle = malloc(sizeof(pcap_t*));
	device_handle = pcap_open_live(dev, BUFSIZ, 1, 1000, error_buffer);
	if (device_handle == NULL) {
		printf("Could not open %s - %s\n", dev, error_buffer);
		return -2;
	}

	if (pcap_compile(device_handle, &filter, filter_exp, 0,
			PCAP_NETMASK_UNKNOWN) == -1) {
		printf("Bad filter - %s\n", pcap_geterr(device_handle));
		return -2;
	}
	if (pcap_setfilter(device_handle, &filter) == -1) {
		printf("Error setting filter - %s\n", pcap_geterr(device_handle));
		return -2;
	}

	return (long) device_handle;
}

void packet_handler(u_char *args, const struct pcap_pkthdr *header,
		const u_char *packet) {

	packet_handler_args_t *jni_env = (packet_handler_args_t*) args;

	jbyteArray ret_packet = (*(jni_env->env))->NewByteArray(jni_env->env,
			header->caplen);
	if (ret_packet == NULL) {
		// TODO: Handle error
	}
	void *temp = (*(jni_env->env))->GetByteArrayElements(jni_env->env,
			(jarray) ret_packet, 0);
	memcpy(temp, packet, header->caplen);
	(*(jni_env->env))->ReleaseByteArrayElements(jni_env->env, ret_packet, temp,
			0);

	jclass networkHandler = (*(jni_env->env))->FindClass(jni_env->env,
			"de/yloose/nodeup/networking/NetworkService");
	jmethodID packet_callback = (*(jni_env->env))->GetMethodID(jni_env->env,
			networkHandler, "nativeRecvPacketCallback", "([B)V");

	(*(jni_env->env))->CallVoidMethod(jni_env->env, jni_env->thisObject,
			packet_callback, ret_packet);

}

JNIEXPORT jint JNICALL Java_de_yloose_nodeup_networking_NetworkService_startListeningLoop(
		JNIEnv *env, jobject thisObject, jlong dev) {

	pcap_t *device_handle = (pcap_t*) dev;
	if (dev == NULL)
		return -1;

	packet_handler_args_t packet_handler_args = { env, thisObject };
	pcap_loop(device_handle, 0, packet_handler, (char*) &packet_handler_args);

	return 1;
}

JNIEXPORT jint JNICALL Java_de_yloose_nodeup_networking_NetworkService_sendPacket(
		JNIEnv *env, jobject thisObject, jlong dev, jbyteArray packetArray,
		jint length) {

	pcap_t *device_handle = (pcap_t*) dev;

	char packet[length];
	jboolean isCopy;
	uint8_t *temp = (uint8_t*) (*env)->GetByteArrayElements(env, packetArray,
			&isCopy);
	memcpy(packet, temp, length);
	if (isCopy) {
		(*env)->ReleaseByteArrayElements(env, packetArray, temp, JNI_ABORT);
	}

	if (device_handle == NULL)
		return -1;

	int send_bytes = pcap_inject(device_handle, (const void*) packet, length);
	// printf("Length of packet: %d Send bytes : %d\n", length, send_bytes);
	if (send_bytes != length) {
		printf("Failed to send data. Send bytes: %d - %s\n", send_bytes,
				pcap_geterr(device_handle));
		return -2;
	}

	fflush(stdout);

	return 0;
}
