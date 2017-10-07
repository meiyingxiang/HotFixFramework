#include <jni.h>
#include <string>
#include "dalvik.h"

typedef Object *(*FindObject)(void *thread, jobject jobject1);

typedef void *(*FindThread)();

FindObject findObject;
FindThread findThread;
extern "C" {
JNIEXPORT void JNICALL
Java_com_frank_1ghost_fixframework_DexManager_replace(JNIEnv *env, jobject instance, jint sdk,
                                                      jobject wrongMethod, jobject rightMeth) {
    // 找到虚拟机对应的的Method结构体
    Method *wrong = (Method *) env->FromReflectedMethod(wrongMethod);
    Method *right = (Method *) env->FromReflectedMethod(rightMeth);
    //下一步把right对应的object  第一个成员变量ClassObject status
    void *dvm_hand = dlopen("libdvm.so", RTLD_NOW);
    //SDK 10以后方法名变了
    findObject = (FindObject) dlsym(dvm_hand, sdk > 10 ?
                                              "_Z20dvmDecodeIndirectRefP6ThreadP8_jobject" :
                                              "dvmDecodeIndirectRef");
    findThread = (FindThread) dlsym(dvm_hand, sdk > 10 ?
                                              "_Z20dvmDecodeIndirectRefP6ThreadP8_jobject" :
                                              "dvmThreadSelf");
    //Method所声明的Class
    jclass methodClazz = env->FindClass("java/lang/reflect/Method");
    jmethodID rieghtMethodId = env->GetMethodID(methodClazz, "getDeclaringClass",
                                                "()Ljava/lang/Class;");
    jobject ndkObject = env->CallObjectMethod(rightMeth, rieghtMethodId);
    ClassObject *firstField = (ClassObject *) findObject(findThread(), ndkObject);
    firstField->status = CLASS_INITIALIZED;
    wrong->accessFlags |= ACC_PUBLIC;
    wrong->methodIndex = right->methodIndex;
    wrong->jniArgInfo = right->jniArgInfo;
    wrong->registersSize = right->registersSize;
    wrong->outsSize = right->outsSize;
    wrong->prototype = right->prototype;
    wrong->insns = right->insns;
    wrong->nativeFunc = right->nativeFunc;

}
}

JNIEXPORT jstring JNICALL
Java_com_frank_1ghost_fixframework_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
