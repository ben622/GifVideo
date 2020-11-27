
MY_LOCAL_PATH := $(call my-dir)
LOCAL_PATH := $(MY_LOCAL_PATH)

LOCAL_SHORT_COMMANDS := true

LOCAL_MODULE    := avutil
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES := ./ffmpeg/armv7-a/libavutil.a
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES := ./ffmpeg/arm64/libavutil.a
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES := ./ffmpeg/i686/libavutil.a
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_SRC_FILES := ./ffmpeg/x86_64/libavutil.a
endif
include $(PREBUILT_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := avformat
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES := ./ffmpeg/armv7-a/libavformat.a
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES := ./ffmpeg/arm64/libavformat.a
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES := ./ffmpeg/i686/libavformat.a
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_SRC_FILES := ./ffmpeg/x86_64/libavformat.a
endif

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := avcodec
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES := ./ffmpeg/armv7-a/libavcodec.a
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES := ./ffmpeg/arm64/libavcodec.a
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES := ./ffmpeg/i686/libavcodec.a
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_SRC_FILES := ./ffmpeg/x86_64/libavcodec.a
endif
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := avresample
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES := ./ffmpeg/armv7-a/libavresample.a
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES := ./ffmpeg/arm64/libavresample.a
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES := ./ffmpeg/i686/libavresample.a
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_SRC_FILES := ./ffmpeg/x86_64/libavresample.a
endif

include $(PREBUILT_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := swscale
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES := ./ffmpeg/armv7-a/libswscale.a
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES := ./ffmpeg/arm64/libswscale.a
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES := ./ffmpeg/i686/libswscale.a
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_SRC_FILES := ./ffmpeg/x86_64/libswscale.a
endif
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_PRELINK_MODULE := false
LOCAL_MODULE 	:= gifvideo
LOCAL_CFLAGS 	:= -w -std=c11 -Os -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1
LOCAL_CFLAGS 	+= -Drestrict='' -D__EMX__ -DOPUS_BUILD -DFIXED_POINT -DUSE_ALLOCA -DHAVE_LRINT -DHAVE_LRINTF -fno-math-errno
LOCAL_CFLAGS 	+= -DANDROID_NDK -DDISABLE_IMPORTGL -fno-strict-aliasing -fprefetch-loop-arrays -DAVOID_TABLES -DANDROID_TILE_BASED_DECODE -DANDROID_ARMV6_IDCT -ffast-math -D__STDC_CONSTANT_MACROS
LOCAL_CPPFLAGS 	:= -DBSD=1 -ffast-math -Os -funroll-loops -std=c++11
LOCAL_LDLIBS 	:= -ljnigraphics -llog -lz -latomic -lEGL -lGLESv2 -landroid
LOCAL_STATIC_LIBRARIES :=swscale avformat avcodec avresample avutil

ifeq ($(TARGET_ARCH_ABI),$(filter $(TARGET_ARCH_ABI),armeabi-v7a arm64-v8a))
    LOCAL_ARM_MODE := arm
    LOCAL_CPPFLAGS += -DLIBYUV_NEON
    LOCAL_CFLAGS += -DLIBYUV_NEON
    LOCAL_CFLAGS += -DOPUS_HAVE_RTCD -DOPUS_ARM_ASM

else
	ifeq ($(TARGET_ARCH_ABI),x86)
	    LOCAL_CFLAGS += -Dx86fix
 	    LOCAL_CPPFLAGS += -Dx86fix
	    LOCAL_ARM_MODE  := arm
    endif
endif

LOCAL_C_INCLUDES    := \
./jni/libyuv/include \
./jni/ffmpeg/include

LOCAL_SRC_FILES     := \
./libyuv/source/compare_common.cc \
./libyuv/source/compare_gcc.cc \
./libyuv/source/compare_neon64.cc \
./libyuv/source/compare_win.cc \
./libyuv/source/compare.cc \
./libyuv/source/convert_argb.cc \
./libyuv/source/convert_from_argb.cc \
./libyuv/source/convert_from.cc \
./libyuv/source/convert_jpeg.cc \
./libyuv/source/convert_to_argb.cc \
./libyuv/source/convert_to_i420.cc \
./libyuv/source/convert.cc \
./libyuv/source/cpu_id.cc \
./libyuv/source/mjpeg_decoder.cc \
./libyuv/source/mjpeg_validate.cc \
./libyuv/source/planar_functions.cc \
./libyuv/source/rotate_any.cc \
./libyuv/source/rotate_argb.cc \
./libyuv/source/rotate_common.cc \
./libyuv/source/rotate_gcc.cc \
./libyuv/source/rotate_mips.cc \
./libyuv/source/rotate_neon64.cc \
./libyuv/source/rotate_win.cc \
./libyuv/source/rotate.cc \
./libyuv/source/row_any.cc \
./libyuv/source/row_common.cc \
./libyuv/source/row_gcc.cc \
./libyuv/source/row_mips.cc \
./libyuv/source/row_neon64.cc \
./libyuv/source/row_win.cc \
./libyuv/source/scale_any.cc \
./libyuv/source/scale_argb.cc \
./libyuv/source/scale_common.cc \
./libyuv/source/scale_gcc.cc \
./libyuv/source/scale_mips.cc \
./libyuv/source/scale_neon64.cc \
./libyuv/source/scale_win.cc \
./libyuv/source/scale.cc \
./libyuv/source/video_common.cc

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_CFLAGS += -DLIBYUV_NEON
    LOCAL_SRC_FILES += \
        ./libyuv/source/compare_neon.cc.neon    \
        ./libyuv/source/rotate_neon.cc.neon     \
        ./libyuv/source/row_neon.cc.neon        \
        ./libyuv/source/scale_neon.cc.neon
endif

LOCAL_SRC_FILES     += \
./jni.c \
./gifvideo.cpp

include $(BUILD_SHARED_LIBRARY)
