#include <jni.h>
#include "im_hero_log.h"
#include "com_im_hero_diffpatch_BsDiff.h"
#include "../cpp/bsdiff.h"
#include "../cpp/bspatch.h"

JNIEXPORT jint JNICALL
BsDiff_bsdiff(JNIEnv *env, jclass type, jstring oldFile_, jstring newFile_, jstring patchFile_){

    const char *oldFile = (*env)->GetStringUTFChars(env,oldFile_,0);
    const char *newFile = (*env)->GetStringUTFChars(env,newFile_,0);
    const char *patchFile = (*env)->GetStringUTFChars(env,patchFile_,0);

    int argc=4;
    const char * argv[argc]
    argv[0] = "bspatch"
    argv[1] = oldFile;
    argv[2] = newFile;
    argv[3] = patchFile;
    LOGI("bsdiff start");
    int ret = bsdiff(argc,argv);
    LOGI("bsdiff over");

    (*env)->ReleaseStringUTFChars(env,oldFile_,oldFile);
    (*env)->ReleaseStringUTFChars(env,newFile_,newFile);
    (*env)->ReleaseStringUTFChars(env,patchFile_,patchFile);
    return ret;
}
JNIEXPORT jint JNICALL
BsDiff.bspatch(JNIEnv *env,jclass type,jstring oldFile_,jstring newFile_,jstring patchFile_){
    const char *oldFile = (*env)->GetStringUTFChars(env, oldFile_,0)
    const char *newFile = (*env)->GetStringUTFChars(env,newFile_,0);
    const char *patchFile = (*env)->GetStringUTFChars(env,patchFile_,0);

       int argc = 4;
        const char * argv[argc];
        argv[0] = "bspatch";
        argv[1] = oldFile;
        argv[2] = newFile;
        argv[3] = patchFile;
        LOGI("bspatch start");
        int ret = bspatch(argc, argv);
        LOGI("bspatch over");

        (*env)->ReleaseStringUTFChars(env, oldFile_, oldFile);
        (*env)->ReleaseStringUTFChars(env, newFile_, newFile);
        (*env)->ReleaseStringUTFChars(env, patchFile_, patchFile);
        return ret;
}
