//
// Created by Zhenxi on 2022/8/15.
//

#ifndef HUNTER_JVMTIWRAPPER_H
#define HUNTER_JVMTIWRAPPER_H

#include "jni.h"
#include "jvmti.h"

namespace ZhenxiRuntime{
    //
    // Created by Zhenxi on 2023/8/15
    //
    class JvmTi {
        public:
            static jvmtiEnv* init(JavaVM *vm);
            static jobjectArray Choose(JNIEnv *env,jclass clazz);
    };
}

#endif //HUNTER_JVMTIWRAPPER_H
