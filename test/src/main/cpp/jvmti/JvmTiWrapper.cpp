//
// Created by Zhenxi on 2022/8/15.
//

#include "../JvmTiWrapper.h"
#include "xdl.h"
#include "logging.h"
#include "adapter.h"
#include "ZhenxiLog.h"

using PluginInitializationFunction = bool (*)();
static const char *PLUGIN_INITIALIZATION_FUNCTION_NAME = "ArtPlugin_Initialize";
static jvmtiEnv *gJvmTi = nullptr;
static bool isInit  = false;

jvmtiEnv *ZhenxiRuntime::JvmTi::init(JavaVM *vm) {
    if (isInit) return gJvmTi;
    auto handler =
            xdl_open("libopenjdkjvmti.so", XDL_TRY_FORCE_LOAD);
    if (handler == nullptr) {
        return nullptr;
    }
    auto sym = xdl_sym(handler,
                       PLUGIN_INITIALIZATION_FUNCTION_NAME, nullptr);
    if (sym == nullptr) {
        sym = xdl_dsym(handler,
                       PLUGIN_INITIALIZATION_FUNCTION_NAME, nullptr);
    }
    if(sym == nullptr){
        LOG(INFO)<< "JvmTi::init ArtPlugin_Initialize == null " ;
        return nullptr;
    }
    auto init = reinterpret_cast<PluginInitializationFunction>(sym);
    if (init != nullptr) {
        auto init_ret = init();
        if (init_ret) {
            LOG(INFO) << "ZhenxiRuntime::JvmTi::init success ! ";
        } else {
            LOG(ERROR) << "ZhenxiRuntime::JvmTi::init fail ! ";
        }
    }

    jvmtiEnv *jvmti = nullptr;
    if (vm->GetEnv((void **) &jvmti, 0x30010200) != JNI_OK) {
        if (vm->GetEnv((void **) &jvmti, 0x70010200) != JNI_OK) {
            return nullptr;
        }
    }
    if (jvmti != nullptr) {
        gJvmTi = jvmti;
        const jvmtiCapabilities REQUIRED_CAPABILITIES = {
                .can_tag_objects = 1,
        };
        if (jvmti->AddCapabilities(&REQUIRED_CAPABILITIES) != JVMTI_ERROR_NONE) {
            jvmti->DisposeEnvironment();
            return nullptr;
        }
        isInit = true;
    }
    return gJvmTi;
}

jlong myTag = 0x12345;

jvmtiIterationControl callback_1(jlong class_tag, jlong size, jlong* tag_ptr, void* user_data)
{
    *tag_ptr = myTag;
    return JVMTI_ITERATION_CONTINUE;
}

jint callback_2(jlong class_tag, jlong size, jlong* tag_ptr, jint length, void* user_data)
{
    *tag_ptr = myTag;
    return JVMTI_VISIT_OBJECTS;
}
jobjectArray ZhenxiRuntime::JvmTi::Choose(JNIEnv *env, jclass clazz) {
    if (clazz == nullptr) {
        return nullptr;
    }
    jvmtiEnv *jvmTiPtr = JvmTi::init(mVm);
    if (jvmTiPtr == nullptr) {
        return nullptr;
    }

    jvmtiHeapCallbacks callbacks;
    (void)memset(&callbacks, 0, sizeof(callbacks));
    callbacks.heap_iteration_callback = &callback_2;
    jvmtiError e = jvmTiPtr->IterateThroughHeap(0, clazz, &callbacks, nullptr);
    if (e != JVMTI_ERROR_NONE) {
        return nullptr;
    }

    jint objectCount = 0;
    jobject *results = nullptr;
    jlong *tags = nullptr;
    auto jvmtiErrorObj = jvmTiPtr->GetObjectsWithTags(
            1, &myTag, &objectCount, &results, &tags);
    if (jvmtiErrorObj != JVMTI_ERROR_NONE) {
        return nullptr;
    }

    jobjectArray objectArray = env->NewObjectArray(
            objectCount, clazz, nullptr);
    for (int i = 0; i < objectCount; i++) {
        env->SetObjectArrayElement(objectArray, i, results[i]);
    }

    if (results)
        jvmTiPtr->Deallocate((unsigned char *) results);
    if (tags)
        jvmTiPtr->Deallocate((unsigned char *) tags);
    return objectArray;
}
