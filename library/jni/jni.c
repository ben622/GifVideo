#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <inttypes.h>
#include <stdlib.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
int videoOnJNILoad(JavaVM *vm, JNIEnv *env);
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
	JNIEnv *env = 0;

	if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}
    if (videoOnJNILoad(vm, env) != JNI_TRUE) {
        return -1;
    }
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
}
