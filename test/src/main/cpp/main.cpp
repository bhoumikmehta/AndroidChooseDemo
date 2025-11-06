#include <jni.h>
#include <syscall.h>
#include "jvmti.h"
#include "JvmTiWrapper.h"


static JavaVM *gVm = nullptr;

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_example_test_ChooseUtils_ChooseJvmZ(JNIEnv *env, jclass thiz,jclass value) {
    ZhenxiRuntime::JvmTi::init(gVm);
    return ZhenxiRuntime::JvmTi::Choose(env, value);
}

jint JNICALL JNI_OnLoad(JavaVM *_vm, void *) {
    gVm = _vm;
    return JNI_VERSION_1_6;
}