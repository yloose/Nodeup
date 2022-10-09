#include "../include/de_yloose_nodeup_networking_NetworkService.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pcap.h>

typedef struct {
	JNIEnv *env;
	jobject thisObject;
} packet_handler_args_t;


JNIEXPORT jlong JNICALL Java_de_yloose_nodeup_networking_NetworkService_initNetworkInterface(
		JNIEnv *env, jobject thisObject, jstring device) {

	const char *dev = (*env)->GetStringUTFChars(env, device, NULL) ;
	char error_buffer[PCAP_ERRBUF_SIZE];
	struct bpf_program filter;
	char filter_exp[] = "link[0]=0xD0 and ether dst FF:FF:FF:FF:FF:FF";

	pcap_t *device_handle = malloc(sizeof(pcap_t *));
	device_handle = pcap_open_live(dev, BUFSIZ, 1, 1000, error_buffer);
	if (device_handle == NULL) {
		printf("Could not open %s - %s\n", dev, error_buffer);
		return -1;
	}

	if (pcap_compile(device_handle, &filter, filter_exp, 0,
			PCAP_NETMASK_UNKNOWN) == -1) {
		printf("Bad filter - %s\n", pcap_geterr(device_handle));
		return -1;
	}
	if (pcap_setfilter(device_handle, &filter) == -1) {
		printf("Error setting filter - %s\n", pcap_geterr(device_handle));
		return -1;
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

	pcap_t *device_handle = (pcap_t *) dev;
	packet_handler_args_t packet_handler_args = { env, thisObject };
	pcap_loop(device_handle, 0, packet_handler, (char*) &packet_handler_args);

	return 1;
}

JNIEXPORT jint JNICALL Java_de_yloose_nodeup_networking_NetworkService_sendPacket(
		JNIEnv *env, jobject thisObject, jlong dev, jbyteArray packetArray, jint length) {

	pcap_t *device_handle = (pcap_t *) dev;

	char packet[length];
	jboolean isCopy;
	uint8_t *temp = (uint8_t*) (*env)->GetByteArrayElements(env, packetArray,
			&isCopy);
	memcpy(packet, temp, length);
	if (isCopy) {
		(*env)->ReleaseByteArrayElements(env, packetArray, temp, JNI_ABORT);
	}

	if (device_handle == NULL) {
		// TODO: Handle error
		printf("NIC not intialized.\n");
	}

	int send_bytes = pcap_inject(device_handle, (const void*) packet, length);
	// printf("Length of packet: %d Send bytes : %d\n", length, send_bytes);
	if (send_bytes != length) {
		// TODO: Handle error
		printf("Failed to send data. Send bytes: %d - %s\n", send_bytes,
				pcap_geterr(device_handle));
	}

	fflush(stdout);

	return 0;
}
