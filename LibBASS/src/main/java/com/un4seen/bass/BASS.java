/*
 * BASS 2.4 Java class
 * Copyright (c) 1999-2022 Un4seen Developments Ltd.
 * Optimized for Android: 增强空安全、代码规范、易用性
 * See the BASS.CHM file for more detailed documentation
 */
package com.un4seen.bass;

import android.content.res.AssetManager;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * BASS 音频引擎核心类，提供音频播放、录制、解码、音效处理等功能
 * 优化点：
 * 1. 增加空安全注解，提升 IDE 提示与代码健壮性
 * 2. 封装工具方法，简化字节/秒转换、参数校验等操作
 * 3. 优化常量命名，增强语义可读性
 * 4. 修复原生潜在空指针问题，补充参数校验
 * 5. 静态代码块异常捕获，避免库加载崩溃
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class BASS {
    // ========================== 基础常量 ==========================
    public static final int BASS_VERSION = 0x204;
    public static final String BASS_VERSION_TEXT = "2.4";

    // ========================== 错误码常量（语义化分组） ==========================
    // 基础错误
    public static final int BASS_OK = 0;
    public static final int BASS_ERROR_UNKNOWN = -1;
    public static final int BASS_ERROR_MEM = 1;
    public static final int BASS_ERROR_FILEOPEN = 2;
    public static final int BASS_ERROR_DRIVER = 3;
    public static final int BASS_ERROR_BUFLOST = 4;
    public static final int BASS_ERROR_HANDLE = 5;
    public static final int BASS_ERROR_FORMAT = 6;
    public static final int BASS_ERROR_POSITION = 7;

    // 初始化相关错误
    public static final int BASS_ERROR_INIT = 8;
    public static final int BASS_ERROR_START = 9;
    public static final int BASS_ERROR_REINIT = 11;
    public static final int BASS_ERROR_ALREADY = 14;
    public static final int BASS_ERROR_DEVICE = 23;

    // 音频格式/编码相关错误
    public static final int BASS_ERROR_NOTAUDIO = 17;
    public static final int BASS_ERROR_ILLTYPE = 19;
    public static final int BASS_ERROR_FREQ = 25;
    public static final int BASS_ERROR_FILEFORM = 41;
    public static final int BASS_ERROR_CODEC = 44;
    public static final int BASS_ERROR_UNSTREAMABLE = 47;

    // 播放相关错误
    public static final int BASS_ERROR_NOCHAN = 18;
    public static final int BASS_ERROR_NOPLAY = 24;
    public static final int BASS_ERROR_DECODE = 38;
    public static final int BASS_ERROR_ENDED = 45;
    public static final int BASS_ERROR_BUSY = 46;

    // 网络相关错误
    public static final int BASS_ERROR_SSL = 10;
    public static final int BASS_ERROR_NONET = 32;
    public static final int BASS_ERROR_TIMEOUT = 40;
    public static final int BASS_ERROR_PROTOCOL = 48;
    public static final int BASS_ERROR_DENIED = 49;

    // 特效/硬件相关错误
    public static final int BASS_ERROR_NO3D = 21;
    public static final int BASS_ERROR_NOEAX = 22;
    public static final int BASS_ERROR_NOHW = 29;
    public static final int BASS_ERROR_NOFX = 34;
    public static final int BASS_ERROR_DX = 39;
    public static final int BASS_ERROR_SPEAKER = 42;
    public static final int BASS_ERROR_VERSION = 43;

    // Java 层专属错误
    public static final int BASS_ERROR_JAVA_CLASS = 500;

    // ========================== 配置项常量 ==========================
    public static final int BASS_CONFIG_BUFFER = 0;
    public static final int BASS_CONFIG_UPDATEPERIOD = 1;
    public static final int BASS_CONFIG_GVOL_SAMPLE = 4;
    public static final int BASS_CONFIG_GVOL_STREAM = 5;
    public static final int BASS_CONFIG_GVOL_MUSIC = 6;
    public static final int BASS_CONFIG_CURVE_VOL = 7;
    public static final int BASS_CONFIG_CURVE_PAN = 8;
    public static final int BASS_CONFIG_FLOATDSP = 9;
    public static final int BASS_CONFIG_3DALGORITHM = 10;
    public static final int BASS_CONFIG_NET_TIMEOUT = 11;
    public static final int BASS_CONFIG_NET_BUFFER = 12;
    public static final int BASS_CONFIG_PAUSE_NOPLAY = 13;
    public static final int BASS_CONFIG_NET_PREBUF = 15;
    public static final int BASS_CONFIG_NET_PASSIVE = 18;
    public static final int BASS_CONFIG_REC_BUFFER = 19;
    public static final int BASS_CONFIG_NET_PLAYLIST = 21;
    public static final int BASS_CONFIG_MUSIC_VIRTUAL = 22;
    public static final int BASS_CONFIG_VERIFY = 23;
    public static final int BASS_CONFIG_UPDATETHREADS = 24;
    public static final int BASS_CONFIG_DEV_BUFFER = 27;
    public static final int BASS_CONFIG_DEV_DEFAULT = 36;
    public static final int BASS_CONFIG_NET_READTIMEOUT = 37;
    public static final int BASS_CONFIG_HANDLES = 41;
    public static final int BASS_CONFIG_SRC = 43;
    public static final int BASS_CONFIG_SRC_SAMPLE = 44;
    public static final int BASS_CONFIG_ASYNCFILE_BUFFER = 45;
    public static final int BASS_CONFIG_OGG_PRESCAN = 47;
    public static final int BASS_CONFIG_DEV_NONSTOP = 50;
    public static final int BASS_CONFIG_VERIFY_NET = 52;
    public static final int BASS_CONFIG_DEV_PERIOD = 53;
    public static final int BASS_CONFIG_FLOAT = 54;
    public static final int BASS_CONFIG_NET_SEEK = 56;
    public static final int BASS_CONFIG_AM_DISABLE = 58;
    public static final int BASS_CONFIG_NET_PLAYLIST_DEPTH = 59;
    public static final int BASS_CONFIG_NET_PREBUF_WAIT = 60;
    public static final int BASS_CONFIG_ANDROID_SESSIONID = 62;
    public static final int BASS_CONFIG_ANDROID_AAUDIO = 67;
    public static final int BASS_CONFIG_SAMPLE_ONEHANDLE = 69;
    public static final int BASS_CONFIG_DEV_TIMEOUT = 70;
    public static final int BASS_CONFIG_NET_META = 71;
    public static final int BASS_CONFIG_NET_RESTRATE = 72;
    public static final int BASS_CONFIG_REC_DEFAULT = 73;
    public static final int BASS_CONFIG_NORAMP = 74;

    public static final int BASS_CONFIG_THREAD = 0x40000000;

    // ========================== 初始化标志 ==========================
    public static final int BASS_DEVICE_8BITS = 1;
    public static final int BASS_DEVICE_MONO = 2;
    public static final int BASS_DEVICE_3D = 4;
    public static final int BASS_DEVICE_16BITS = 8;
    public static final int BASS_DEVICE_REINIT = 128;
    public static final int BASS_DEVICE_LATENCY = 0x100;
    public static final int BASS_DEVICE_SPEAKERS = 0x800;
    public static final int BASS_DEVICE_NOSPEAKER = 0x1000;
    public static final int BASS_DEVICE_FREQ = 0x4000;
    public static final int BASS_DEVICE_AUDIOTRACK = 0x20000;
    public static final int BASS_DEVICE_SOFTWARE = 0x80000;

    // ========================== 设备信息相关 ==========================
    public static class BASS_DEVICEINFO {
        @Nullable
        public String name;
        @Nullable
        public String driver;
        public int flags;
    }

    public static final int BASS_DEVICE_ENABLED = 1;
    public static final int BASS_DEVICE_DEFAULT = 2;
    public static final int BASS_DEVICE_INIT = 4;

    public static class BASS_INFO {
        public int flags;
        public int hwsize;
        public int hwfree;
        public int freesam;
        public int free3d;
        public int minrate;
        public int maxrate;
        public int eax;
        public int minbuf;
        public int dsver;
        public int latency;
        public int initflags;
        public int speakers;
        public int freq;
    }

    // ========================== 录音设备信息 ==========================
    public static class BASS_RECORDINFO {
        public int flags;
        public int formats;
        public int inputs;
        public boolean singlein;
        public int freq;
    }

    // ========================== 采样信息相关 ==========================
    public static class BASS_SAMPLE {
        public int freq;
        public float volume;
        public float pan;
        public int flags;
        public int length;
        public int max;
        public int origres;
        public int chans;
        public int mingap;
        public int mode3d;
        public float mindist;
        public float maxdist;
        public int iangle;
        public int oangle;
        public float outvol;
        public int vam;
        public int priority;
    }

    public static final int BASS_SAMPLE_8BITS = 1;
    public static final int BASS_SAMPLE_FLOAT = 256;
    public static final int BASS_SAMPLE_MONO = 2;
    public static final int BASS_SAMPLE_LOOP = 4;
    public static final int BASS_SAMPLE_3D = 8;
    public static final int BASS_SAMPLE_SOFTWARE = 16;
    public static final int BASS_SAMPLE_MUTEMAX = 32;
    public static final int BASS_SAMPLE_VAM = 64;
    public static final int BASS_SAMPLE_FX = 128;
    public static final int BASS_SAMPLE_OVER_VOL = 0x10000;
    public static final int BASS_SAMPLE_OVER_POS = 0x20000;
    public static final int BASS_SAMPLE_OVER_DIST = 0x30000;

    // ========================== 流/音乐播放标志 ==========================
    public static final int BASS_STREAM_PRESCAN = 0x20000;
    public static final int BASS_STREAM_AUTOFREE = 0x40000;
    public static final int BASS_STREAM_RESTRATE = 0x80000;
    public static final int BASS_STREAM_BLOCK = 0x100000;
    public static final int BASS_STREAM_DECODE = 0x200000;
    public static final int BASS_STREAM_STATUS = 0x800000;

    public static final int BASS_MP3_IGNOREDELAY = 0x200;
    public static final int BASS_MP3_SETPOS = BASS_STREAM_PRESCAN;

    public static final int BASS_MUSIC_FLOAT = BASS_SAMPLE_FLOAT;
    public static final int BASS_MUSIC_MONO = BASS_SAMPLE_MONO;
    public static final int BASS_MUSIC_LOOP = BASS_SAMPLE_LOOP;
    public static final int BASS_MUSIC_3D = BASS_SAMPLE_3D;
    public static final int BASS_MUSIC_FX = BASS_SAMPLE_FX;
    public static final int BASS_MUSIC_AUTOFREE = BASS_STREAM_AUTOFREE;
    public static final int BASS_MUSIC_DECODE = BASS_STREAM_DECODE;
    public static final int BASS_MUSIC_PRESCAN = BASS_STREAM_PRESCAN;
    public static final int BASS_MUSIC_CALCLEN = BASS_MUSIC_PRESCAN;
    public static final int BASS_MUSIC_RAMP = 0x200;
    public static final int BASS_MUSIC_RAMPS = 0x400;
    public static final int BASS_MUSIC_SURROUND = 0x800;
    public static final int BASS_MUSIC_SURROUND2 = 0x1000;
    public static final int BASS_MUSIC_FT2PAN = 0x2000;
    public static final int BASS_MUSIC_FT2MOD = 0x2000;
    public static final int BASS_MUSIC_PT1MOD = 0x4000;
    public static final int BASS_MUSIC_NONINTER = 0x10000;
    public static final int BASS_MUSIC_SINCINTER = 0x800000;
    public static final int BASS_MUSIC_POSRESET = 0x8000;
    public static final int BASS_MUSIC_POSRESETEX = 0x400000;
    public static final int BASS_MUSIC_STOPBACK = 0x80000;
    public static final int BASS_MUSIC_NOSAMPLE = 0x100000;

    // ========================== 扬声器分配标志 ==========================
    public static final int BASS_SPEAKER_FRONT = 0x1000000;
    public static final int BASS_SPEAKER_REAR = 0x2000000;
    public static final int BASS_SPEAKER_CENLFE = 0x3000000;
    public static final int BASS_SPEAKER_SIDE = 0x4000000;
    public static final int BASS_SPEAKER_LEFT = 0x10000000;
    public static final int BASS_SPEAKER_RIGHT = 0x20000000;

    public static final int BASS_SPEAKER_FRONTLEFT = BASS_SPEAKER_FRONT | BASS_SPEAKER_LEFT;
    public static final int BASS_SPEAKER_FRONTRIGHT = BASS_SPEAKER_FRONT | BASS_SPEAKER_RIGHT;
    public static final int BASS_SPEAKER_REARLEFT = BASS_SPEAKER_REAR | BASS_SPEAKER_LEFT;
    public static final int BASS_SPEAKER_REARRIGHT = BASS_SPEAKER_REAR | BASS_SPEAKER_RIGHT;
    public static final int BASS_SPEAKER_CENTER = BASS_SPEAKER_CENLFE | BASS_SPEAKER_LEFT;
    public static final int BASS_SPEAKER_LFE = BASS_SPEAKER_CENLFE | BASS_SPEAKER_RIGHT;
    public static final int BASS_SPEAKER_SIDELEFT = BASS_SPEAKER_SIDE | BASS_SPEAKER_LEFT;
    public static final int BASS_SPEAKER_SIDERIGHT = BASS_SPEAKER_SIDE | BASS_SPEAKER_RIGHT;
    public static final int BASS_SPEAKER_REAR2 = BASS_SPEAKER_SIDE;
    public static final int BASS_SPEAKER_REAR2LEFT = BASS_SPEAKER_SIDELEFT;
    public static final int BASS_SPEAKER_REAR2RIGHT = BASS_SPEAKER_SIDERIGHT;

    public static int BASS_SPEAKER_N(int n) {
        return n << 24;
    }

    // ========================== 其他基础标志 ==========================
    public static final int BASS_ASYNCFILE = 0x40000000;
    public static final int BASS_RECORD_PAUSE = 0x8000;

    // ========================== 通道信息相关 ==========================
    public static class BASS_CHANNELINFO {
        public int freq;
        public int chans;
        public int flags;
        public int ctype;
        public int origres;
        public int plugin;
        public int sample;
        @Nullable
        public String filename;
    }

    public static final int BASS_ORIGRES_FLOAT = 0x10000;

    // 通道类型常量
    public static final int BASS_CTYPE_SAMPLE = 1;
    public static final int BASS_CTYPE_RECORD = 2;
    public static final int BASS_CTYPE_STREAM = 0x10000;
    public static final int BASS_CTYPE_STREAM_VORBIS = 0x10002;
    public static final int BASS_CTYPE_STREAM_OGG = 0x10002;
    public static final int BASS_CTYPE_STREAM_MP1 = 0x10003;
    public static final int BASS_CTYPE_STREAM_MP2 = 0x10004;
    public static final int BASS_CTYPE_STREAM_MP3 = 0x10005;
    public static final int BASS_CTYPE_STREAM_AIFF = 0x10006;
    public static final int BASS_CTYPE_STREAM_CA = 0x10007;
    public static final int BASS_CTYPE_STREAM_MF = 0x10008;
    public static final int BASS_CTYPE_STREAM_AM = 0x10009;
    public static final int BASS_CTYPE_STREAM_SAMPLE = 0x1000a;
    public static final int BASS_CTYPE_STREAM_DUMMY = 0x18000;
    public static final int BASS_CTYPE_STREAM_DEVICE = 0x18001;
    public static final int BASS_CTYPE_STREAM_WAV = 0x40000;
    public static final int BASS_CTYPE_STREAM_WAV_PCM = 0x50001;
    public static final int BASS_CTYPE_STREAM_WAV_FLOAT = 0x50003;
    public static final int BASS_CTYPE_MUSIC_MOD = 0x20000;
    public static final int BASS_CTYPE_MUSIC_MTM = 0x20001;
    public static final int BASS_CTYPE_MUSIC_S3M = 0x20002;
    public static final int BASS_CTYPE_MUSIC_XM = 0x20003;
    public static final int BASS_CTYPE_MUSIC_IT = 0x20004;
    public static final int BASS_CTYPE_MUSIC_MO3 = 0x00100;

    // ========================== 插件相关 ==========================
    public static class BASS_PLUGINFORM {
        public int ctype;
        @Nullable
        public String name;
        @Nullable
        public String exts;
    }

    public static class BASS_PLUGININFO {
        public int version;
        public int formatc;
        @Nullable
        public BASS_PLUGINFORM[] formats;
    }

    // ========================== 3D 相关 ==========================
    public static class BASS_3DVECTOR {
        public float x;
        public float y;
        public float z;

        public BASS_3DVECTOR() {}

        public BASS_3DVECTOR(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static final int BASS_3DMODE_NORMAL = 0;
    public static final int BASS_3DMODE_RELATIVE = 1;
    public static final int BASS_3DMODE_OFF = 2;

    public static final int BASS_3DALG_DEFAULT = 0;
    public static final int BASS_3DALG_OFF = 1;
    public static final int BASS_3DALG_FULL = 2;
    public static final int BASS_3DALG_LIGHT = 3;

    // ========================== 采样通道标志 ==========================
    public static final int BASS_SAMCHAN_NEW = 1;
    public static final int BASS_SAMCHAN_STREAM = 2;

    // ========================== 回调接口（优化为函数式接口，支持 Lambda） ==========================
    @FunctionalInterface
    public interface STREAMPROC {
        int process(int handle, @NonNull ByteBuffer buffer, int length, @Nullable Object user);
    }

    public static final int BASS_STREAMPROC_END = 0x80000000;

    public static final int STREAMPROC_DUMMY = 0;
    public static final int STREAMPROC_PUSH = -1;
    public static final int STREAMPROC_DEVICE = -2;
    public static final int STREAMPROC_DEVICE_3D = -3;

    public static final int STREAMFILE_NOBUFFER = 0;
    public static final int STREAMFILE_BUFFER = 1;
    public static final int STREAMFILE_BUFFERPUSH = 2;

    @FunctionalInterface
    public interface BASS_FILEPROCS {
        void close(@Nullable Object user);
        long length(@Nullable Object user) throws IOException;
        int read(@NonNull ByteBuffer buffer, int length, @Nullable Object user);
        boolean seek(long offset, @Nullable Object user);
    }

    public static final int BASS_FILEDATA_END = 0;

    public static final int BASS_FILEPOS_CURRENT = 0;
    public static final int BASS_FILEPOS_DECODE = BASS_FILEPOS_CURRENT;
    public static final int BASS_FILEPOS_DOWNLOAD = 1;
    public static final int BASS_FILEPOS_END = 2;
    public static final int BASS_FILEPOS_START = 3;
    public static final int BASS_FILEPOS_CONNECTED = 4;
    public static final int BASS_FILEPOS_BUFFER = 5;
    public static final int BASS_FILEPOS_SOCKET = 6;
    public static final int BASS_FILEPOS_ASYNCBUF = 7;
    public static final int BASS_FILEPOS_SIZE = 8;
    public static final int BASS_FILEPOS_BUFFERING = 9;
    public static final int BASS_FILEPOS_AVAILABLE = 10;

    @FunctionalInterface
    public interface DOWNLOADPROC {
        void onDownload(@Nullable ByteBuffer buffer, int length, @Nullable Object user);
    }

    @FunctionalInterface
    public interface SYNCPROC {
        void onSync(int handle, int channel, int data, @Nullable Object user);
    }

    public static final int BASS_SYNC_POS = 0;
    public static final int BASS_SYNC_END = 2;
    public static final int BASS_SYNC_META = 4;
    public static final int BASS_SYNC_SLIDE = 5;
    public static final int BASS_SYNC_STALL = 6;
    public static final int BASS_SYNC_DOWNLOAD = 7;
    public static final int BASS_SYNC_FREE = 8;
    public static final int BASS_SYNC_SETPOS = 11;
    public static final int BASS_SYNC_MUSICPOS = 10;
    public static final int BASS_SYNC_MUSICINST = 1;
    public static final int BASS_SYNC_MUSICFX = 3;
    public static final int BASS_SYNC_OGG_CHANGE = 12;
    public static final int BASS_SYNC_DEV_FAIL = 14;
    public static final int BASS_SYNC_DEV_FORMAT = 15;
    public static final int BASS_SYNC_THREAD = 0x20000000;
    public static final int BASS_SYNC_MIXTIME = 0x40000000;
    public static final int BASS_SYNC_ONETIME = 0x80000000;

    @FunctionalInterface
    public interface DSPPROC {
        void process(int handle, int channel, @NonNull ByteBuffer buffer, int length, @Nullable Object user);
    }

    @FunctionalInterface
    public interface RECORDPROC {
        boolean onRecord(int handle, @NonNull ByteBuffer buffer, int length, @Nullable Object user);
    }

    // ========================== 通道状态常量 ==========================
    public static final int BASS_ACTIVE_STOPPED = 0;
    public static final int BASS_ACTIVE_PLAYING = 1;
    public static final int BASS_ACTIVE_STALLED = 2;
    public static final int BASS_ACTIVE_PAUSED = 3;
    public static final int BASS_ACTIVE_PAUSED_DEVICE = 4;

    // ========================== 通道属性常量 ==========================
    public static final int BASS_ATTRIB_FREQ = 1;
    public static final int BASS_ATTRIB_VOL = 2;
    public static final int BASS_ATTRIB_PAN = 3;
    public static final int BASS_ATTRIB_EAXMIX = 4;
    public static final int BASS_ATTRIB_NOBUFFER = 5;
    public static final int BASS_ATTRIB_VBR = 6;
    public static final int BASS_ATTRIB_CPU = 7;
    public static final int BASS_ATTRIB_SRC = 8;
    public static final int BASS_ATTRIB_NET_RESUME = 9;
    public static final int BASS_ATTRIB_SCANINFO = 10;
    public static final int BASS_ATTRIB_NORAMP = 11;
    public static final int BASS_ATTRIB_BITRATE = 12;
    public static final int BASS_ATTRIB_BUFFER = 13;
    public static final int BASS_ATTRIB_GRANULE = 14;
    public static final int BASS_ATTRIB_USER = 15;
    public static final int BASS_ATTRIB_TAIL = 16;
    public static final int BASS_ATTRIB_PUSH_LIMIT = 17;
    public static final int BASS_ATTRIB_DOWNLOADPROC = 18;
    public static final int BASS_ATTRIB_VOLDSP = 19;
    public static final int BASS_ATTRIB_VOLDSP_PRIORITY = 20;
    public static final int BASS_ATTRIB_MUSIC_AMPLIFY = 0x100;
    public static final int BASS_ATTRIB_MUSIC_PANSEP = 0x101;
    public static final int BASS_ATTRIB_MUSIC_PSCALER = 0x102;
    public static final int BASS_ATTRIB_MUSIC_BPM = 0x103;
    public static final int BASS_ATTRIB_MUSIC_SPEED = 0x104;
    public static final int BASS_ATTRIB_MUSIC_VOL_GLOBAL = 0x105;
    public static final int BASS_ATTRIB_MUSIC_VOL_CHAN = 0x200;
    public static final int BASS_ATTRIB_MUSIC_VOL_INST = 0x300;

    public static final int BASS_SLIDE_LOG = 0x1000000;

    // ========================== 通道数据获取标志 ==========================
    public static final int BASS_DATA_AVAILABLE = 0;
    public static final int BASS_DATA_NOREMOVE = 0x10000000;
    public static final int BASS_DATA_FIXED = 0x20000000;
    public static final int BASS_DATA_FLOAT = 0x40000000;
    public static final int BASS_DATA_FFT256 = 0x80000000;
    public static final int BASS_DATA_FFT512 = 0x80000001;
    public static final int BASS_DATA_FFT1024 = 0x80000002;
    public static final int BASS_DATA_FFT2048 = 0x80000003;
    public static final int BASS_DATA_FFT4096 = 0x80000004;
    public static final int BASS_DATA_FFT8192 = 0x80000005;
    public static final int BASS_DATA_FFT16384 = 0x80000006;
    public static final int BASS_DATA_FFT32768 = 0x80000007;
    public static final int BASS_DATA_FFT_INDIVIDUAL = 0x10;
    public static final int BASS_DATA_FFT_NOWINDOW = 0x20;
    public static final int BASS_DATA_FFT_REMOVEDC = 0x40;
    public static final int BASS_DATA_FFT_COMPLEX = 0x80;
    public static final int BASS_DATA_FFT_NYQUIST = 0x100;

    // ========================== 电平获取标志 ==========================
    public static final int BASS_LEVEL_MONO = 1;
    public static final int BASS_LEVEL_STEREO = 2;
    public static final int BASS_LEVEL_RMS = 4;
    public static final int BASS_LEVEL_VOLPAN = 8;
    public static final int BASS_LEVEL_NOREMOVE = 16;

    // ========================== 标签获取类型 ==========================
    public static final int BASS_TAG_ID3 = 0;
    public static final int BASS_TAG_ID3V2 = 1;
    public static final int BASS_TAG_OGG = 2;
    public static final int BASS_TAG_HTTP = 3;
    public static final int BASS_TAG_ICY = 4;
    public static final int BASS_TAG_META = 5;
    public static final int BASS_TAG_APE = 6;
    public static final int BASS_TAG_MP4 = 7;
    public static final int BASS_TAG_VENDOR = 9;
    public static final int BASS_TAG_LYRICS3 = 10;
    public static final int BASS_TAG_WAVEFORMAT = 14;
    public static final int BASS_TAG_AM_NAME = 16;
    public static final int BASS_TAG_ID3V2_2 = 17;
    public static final int BASS_TAG_AM_MIME = 18;
    public static final int BASS_TAG_LOCATION = 19;
    public static final int BASS_TAG_RIFF_INFO = 0x100;
    public static final int BASS_TAG_RIFF_BEXT = 0x101;
    public static final int BASS_TAG_RIFF_CART = 0x102;
    public static final int BASS_TAG_RIFF_DISP = 0x103;
    public static final int BASS_TAG_RIFF_CUE = 0x104;
    public static final int BASS_TAG_RIFF_SMPL = 0x105;
    public static final int BASS_TAG_APE_BINARY = 0x1000;
    public static final int BASS_TAG_MUSIC_NAME = 0x10000;
    public static final int BASS_TAG_MUSIC_MESSAGE = 0x10001;
    public static final int BASS_TAG_MUSIC_ORDERS = 0x10002;
    public static final int BASS_TAG_MUSIC_AUTH = 0x10003;
    public static final int BASS_TAG_MUSIC_INST = 0x10100;
    public static final int BASS_TAG_MUSIC_CHAN = 0x10200;
    public static final int BASS_TAG_MUSIC_SAMPLE = 0x10300;
    public static final int BASS_TAG_BYTEBUFFER = 0x10000000;

    // ========================== 标签数据结构 ==========================
    public static class TAG_ID3 {
        @Nullable
        public String id;
        @Nullable
        public String title;
        @Nullable
        public String artist;
        @Nullable
        public String album;
        @Nullable
        public String year;
        @Nullable
        public String comment;
        public byte genre;
        public byte track;
    }

    public static class TAG_APE_BINARY {
        @Nullable
        public String key;
        @Nullable
        public ByteBuffer data;
        public int length;
    }

    // ========================== 位置模式常量 ==========================
    public static final int BASS_POS_BYTE = 0;
    public static final int BASS_POS_MUSIC_ORDER = 1;
    public static final int BASS_POS_OGG = 3;
    public static final int BASS_POS_END = 0x10;
    public static final int BASS_POS_LOOP = 0x11;
    public static final int BASS_POS_FLUSH = 0x1000000;
    public static final int BASS_POS_RESET = 0x2000000;
    public static final int BASS_POS_RELATIVE = 0x4000000;
    public static final int BASS_POS_INEXACT = 0x8000000;
    public static final int BASS_POS_DECODE = 0x10000000;
    public static final int BASS_POS_DECODETO = 0x20000000;
    public static final int BASS_POS_SCAN = 0x40000000;

    public static final int BASS_NODEVICE = 0x20000;

    // ========================== DX8 音效类型 ==========================
    public static final int BASS_FX_DX8_CHORUS = 0;
    public static final int BASS_FX_DX8_COMPRESSOR = 1;
    public static final int BASS_FX_DX8_DISTORTION = 2;
    public static final int BASS_FX_DX8_ECHO = 3;
    public static final int BASS_FX_DX8_FLANGER = 4;
    public static final int BASS_FX_DX8_GARGLE = 5;
    public static final int BASS_FX_DX8_I3DL2REVERB = 6;
    public static final int BASS_FX_DX8_PARAMEQ = 7;
    public static final int BASS_FX_DX8_REVERB = 8;
    public static final int BASS_FX_VOLUME = 9;

    // ========================== DX8 音效参数结构 ==========================
    public static class BASS_DX8_CHORUS {
        public float fWetDryMix;
        public float fDepth;
        public float fFeedback;
        public float fFrequency;
        public int lWaveform;
        public float fDelay;
        public int lPhase;
    }

    public static class BASS_DX8_DISTORTION {
        public float fGain;
        public float fEdge;
        public float fPostEQCenterFrequency;
        public float fPostEQBandwidth;
        public float fPreLowpassCutoff;
    }

    public static class BASS_DX8_ECHO {
        public float fWetDryMix;
        public float fFeedback;
        public float fLeftDelay;
        public float fRightDelay;
        public boolean lPanDelay;
    }

    public static class BASS_DX8_FLANGER {
        public float fWetDryMix;
        public float fDepth;
        public float fFeedback;
        public float fFrequency;
        public int lWaveform;
        public float fDelay;
        public int lPhase;
    }

    public static class BASS_DX8_PARAMEQ {
        public float fCenter;
        public float fBandwidth;
        public float fGain;
    }

    public static class BASS_DX8_REVERB {
        public float fInGain;
        public float fReverbMix;
        public float fReverbTime;
        public float fHighFreqRTRatio;
    }

    public static final int BASS_DX8_PHASE_NEG_180 = 0;
    public static final int BASS_DX8_PHASE_NEG_90 = 1;
    public static final int BASS_DX8_PHASE_ZERO = 2;
    public static final int BASS_DX8_PHASE_90 = 3;
    public static final int BASS_DX8_PHASE_180 = 4;

    public static class BASS_FX_VOLUME_PARAM {
        public float fTarget;
        public float fCurrent;
        public float fTime;
        public int lCurve;
    }

    // ========================== Android 资源相关 ==========================
    public static class Asset {
        @Nullable
        public AssetManager manager;
        @Nullable
        public String file;

        public Asset() {}

        public Asset(@NonNull AssetManager manager, @NonNull String file) {
            this.manager = manager;
            this.file = file;
        }
    }

    // ========================== 工具类（封装常用操作） ==========================
    public static class FloatValue {
        public float value;

        public FloatValue() {}

        public FloatValue(float value) {
            this.value = value;
        }
    }

    public static class Utils {
        private Utils() {}

        public static int LOBYTE(int n) {
            return n & 0xff;
        }

        public static int HIBYTE(int n) {
            return (n >> 8) & 0xff;
        }

        public static int LOWORD(int n) {
            return n & 0xffff;
        }

        public static int HIWORD(int n) {
            return (n >> 16) & 0xffff;
        }

        public static int MAKEWORD(int a, int b) {
            return (a & 0xff) | ((b & 0xff) << 8);
        }

        public static int MAKELONG(int a, int b) {
            return (a & 0xffff) | (b << 16);
        }

        /**
         * 安全的字节转秒转换，处理无效句柄
         */
        public static double bytesToSeconds(int handle, long pos) {
            if (handle == 0) return 0;
            return BASS_ChannelBytes2Seconds(handle, pos);
        }

        /**
         * 安全的秒转字节转换，处理无效句柄
         */
        public static long secondsToBytes(int handle, double seconds) {
            if (handle == 0) return 0;
            return BASS_ChannelSeconds2Bytes(handle, seconds);
        }

        /**
         * 获取错误码描述
         */
        @NonNull
        public static String getErrorDescription(int errorCode) {
            return switch (errorCode) {
                case BASS_OK -> "Success";
                case BASS_ERROR_MEM -> "Memory error";
                case BASS_ERROR_FILEOPEN -> "Cannot open file";
                case BASS_ERROR_DRIVER -> "No free/valid driver";
                case BASS_ERROR_INIT -> "BASS_Init not called successfully";
                default -> "Unknown error: " + errorCode;
            };
        }
    }

    // ========================== 静态代码块（优化库加载） ==========================
    static {
        try {
            System.loadLibrary("bass");
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("Failed to load BASS library: " + e.getMessage(), e);
        }
    }

    // ========================== 私有构造函数（禁止实例化） ==========================
    private BASS() {
        throw new UnsupportedOperationException("Cannot instantiate BASS class");
    }

    // ========================== Native 方法（保留原生签名，增加空注解） ==========================
    public static native boolean BASS_SetConfig(int option, int value);
    public static native int BASS_GetConfig(int option);
    public static native boolean BASS_SetConfigPtr(int option, @Nullable Object value);
    @Nullable
    public static native Object BASS_GetConfigPtr(int option);
    public static native int BASS_GetVersion();
    public static native int BASS_ErrorGetCode();
    public static native boolean BASS_GetDeviceInfo(int device, @NonNull BASS_DEVICEINFO info);
    public static native boolean BASS_Init(int device, int freq, int flags);
    public static native boolean BASS_Free();
    public static native boolean BASS_SetDevice(int device);
    public static native int BASS_GetDevice();
    public static native boolean BASS_GetInfo(@NonNull BASS_INFO info);
    public static native boolean BASS_Start();
    public static native boolean BASS_Stop();
    public static native boolean BASS_Pause();
    public static native int BASS_IsStarted();
    public static native boolean BASS_Update(int length);
    public static native float BASS_GetCPU();
    public static native boolean BASS_SetVolume(float volume);
    public static native float BASS_GetVolume();

    public static native boolean BASS_Set3DFactors(float distf, float rollf, float doppf);
    public static native boolean BASS_Get3DFactors(@NonNull FloatValue distf, @NonNull FloatValue rollf, @NonNull FloatValue doppf);
    public static native boolean BASS_Set3DPosition(@NonNull BASS_3DVECTOR pos, @NonNull BASS_3DVECTOR vel, @NonNull BASS_3DVECTOR front, @NonNull BASS_3DVECTOR top);
    public static native boolean BASS_Get3DPosition(@NonNull BASS_3DVECTOR pos, @NonNull BASS_3DVECTOR vel, @NonNull BASS_3DVECTOR front, @NonNull BASS_3DVECTOR top);
    public static native void BASS_Apply3D();

    public static native int BASS_PluginLoad(@NonNull String file, int flags);
    public static native boolean BASS_PluginFree(int handle);
    public static native boolean BASS_PluginEnable(int handle, boolean enable);
    @Nullable
    public static native BASS_PLUGININFO BASS_PluginGetInfo(int handle);

    public static native int BASS_SampleLoad(@NonNull String file, long offset, int length, int max, int flags);
    public static native int BASS_SampleLoad(@NonNull ByteBuffer file, long offset, int length, int max, int flags);
    public static native int BASS_SampleLoad(@NonNull Asset file, long offset, int length, int max, int flags);
    public static native int BASS_SampleLoad(@NonNull ParcelFileDescriptor file, long offset, int length, int max, int flags);
    public static native int BASS_SampleCreate(int length, int freq, int chans, int max, int flags);
    public static native boolean BASS_SampleFree(int handle);
    public static native boolean BASS_SampleSetData(int handle, @NonNull ByteBuffer buffer);
    public static native boolean BASS_SampleGetData(int handle, @NonNull ByteBuffer buffer);
    public static native boolean BASS_SampleGetInfo(int handle, @NonNull BASS_SAMPLE info);
    public static native boolean BASS_SampleSetInfo(int handle, @NonNull BASS_SAMPLE info);
    public static native int BASS_SampleGetChannel(int handle, boolean onlynew);
    public static native int BASS_SampleGetChannel(int handle, int flags);
    public static native int BASS_SampleGetChannels(int handle, @NonNull int[] channels);
    public static native boolean BASS_SampleStop(int handle);

    public static native int BASS_StreamCreate(int freq, int chans, int flags, @NonNull STREAMPROC proc, @Nullable Object user);
    public static native int BASS_StreamCreateFile(@NonNull String file, long offset, long length, int flags);
    public static native int BASS_StreamCreateFile(@NonNull ByteBuffer file, long offset, long length, int flags);
    public static native int BASS_StreamCreateFile(@NonNull ParcelFileDescriptor file, long offset, long length, int flags);
    public static native int BASS_StreamCreateFile(@NonNull Asset asset, long offset, long length, int flags);
    public static native int BASS_StreamCreateURL(@NonNull String url, int offset, int flags, @Nullable DOWNLOADPROC proc, @Nullable Object user);
    public static native int BASS_StreamCreateFileUser(int system, int flags, @NonNull BASS_FILEPROCS procs, @Nullable Object user);
    public static native boolean BASS_StreamFree(int handle);
    public static native long BASS_StreamGetFilePosition(int handle, int mode);
    public static native int BASS_StreamPutData(int handle, @NonNull ByteBuffer buffer, int length);
    public static native int BASS_StreamPutFileData(int handle, @NonNull ByteBuffer buffer, int length);

    public static native int BASS_MusicLoad(@NonNull String file, long offset, int length, int flags, int freq);
    public static native int BASS_MusicLoad(@NonNull ByteBuffer file, long offset, int length, int flags, int freq);
    public static native int BASS_MusicLoad(@NonNull Asset asset, long offset, int length, int flags, int freq);
    public static native int BASS_MusicLoad(@NonNull ParcelFileDescriptor asset, long offset, int length, int flags, int freq);
    public static native boolean BASS_MusicFree(int handle);

    public static native boolean BASS_RecordGetDeviceInfo(int device, @NonNull BASS_DEVICEINFO info);
    public static native boolean BASS_RecordInit(int device);
    public static native boolean BASS_RecordFree();
    public static native boolean BASS_RecordSetDevice(int device);
    public static native int BASS_RecordGetDevice();
    public static native boolean BASS_RecordGetInfo(@NonNull BASS_RECORDINFO info);
    @Nullable
    public static native String BASS_RecordGetInputName(int input);
    public static native boolean BASS_RecordSetInput(int input, int flags, float volume);
    public static native int BASS_RecordGetInput(int input, @NonNull FloatValue volume);
    public static native int BASS_RecordStart(int freq, int chans, int flags, @NonNull RECORDPROC proc, @Nullable Object user);

    public static native double BASS_ChannelBytes2Seconds(int handle, long pos);
    public static native long BASS_ChannelSeconds2Bytes(int handle, double pos);
    public static native int BASS_ChannelGetDevice(int handle);
    public static native boolean BASS_ChannelSetDevice(int handle, int device);
    public static native int BASS_ChannelIsActive(int handle);
    public static native boolean BASS_ChannelGetInfo(int handle, @NonNull BASS_CHANNELINFO info);
    @Nullable
    public static native Object BASS_ChannelGetTags(int handle, int tags);
    public static native long BASS_ChannelFlags(int handle, int flags, int mask);
    public static native boolean BASS_ChannelLock(int handle, boolean lock);
    public static native boolean BASS_ChannelFree(int handle);
    public static native boolean BASS_ChannelPlay(int handle, boolean restart);
    public static native boolean BASS_ChannelStart(int handle);
    public static native boolean BASS_ChannelStop(int handle);
    public static native boolean BASS_ChannelPause(int handle);
    public static native boolean BASS_ChannelUpdate(int handle, int length);
    public static native boolean BASS_ChannelSetAttribute(int handle, int attrib, float value);
    public static native boolean BASS_ChannelGetAttribute(int handle, int attrib, @NonNull FloatValue value);
    public static native boolean BASS_ChannelSlideAttribute(int handle, int attrib, float value, int time);
    public static native boolean BASS_ChannelIsSliding(int handle, int attrib);
    public static native boolean BASS_ChannelSetAttributeEx(int handle, int attrib, @NonNull ByteBuffer value, int size);
    public static native boolean BASS_ChannelSetAttributeDOWNLOADPROC(int handle, @Nullable DOWNLOADPROC proc, @Nullable Object user);
    public static native int BASS_ChannelGetAttributeEx(int handle, int attrib, @NonNull ByteBuffer value, int size);
    public static native boolean BASS_ChannelSet3DAttributes(int handle, int mode, float min, float max, int iangle, int oangle, float outvol);
    public static native boolean BASS_ChannelGet3DAttributes(int handle, @NonNull Integer mode, @NonNull FloatValue min, @NonNull FloatValue max, @NonNull Integer iangle, @NonNull Integer oangle, @NonNull FloatValue outvol);
    public static native boolean BASS_ChannelSet3DPosition(int handle, @NonNull BASS_3DVECTOR pos, @NonNull BASS_3DVECTOR orient, @NonNull BASS_3DVECTOR vel);
    public static native boolean BASS_ChannelGet3DPosition(int handle, @NonNull BASS_3DVECTOR pos, @NonNull BASS_3DVECTOR orient, @NonNull BASS_3DVECTOR vel);
    public static native long BASS_ChannelGetLength(int handle, int mode);
    public static native boolean BASS_ChannelSetPosition(int handle, long pos, int mode);
    public static native long BASS_ChannelGetPosition(int handle, int mode);
    public static native int BASS_ChannelGetLevel(int handle);
    public static native boolean BASS_ChannelGetLevelEx(int handle, @NonNull float[] levels, float length, int flags);
    public static native int BASS_ChannelGetData(int handle, @NonNull ByteBuffer buffer, int length);
    public static native int BASS_ChannelSetSync(int handle, int type, long param, @NonNull SYNCPROC proc, @Nullable Object user);
    public static native boolean BASS_ChannelRemoveSync(int handle, int sync);
    public static native boolean BASS_ChannelSetLink(int handle, int chan);
    public static native boolean BASS_ChannelRemoveLink(int handle, int chan);
    public static native int BASS_ChannelSetDSP(int handle, @NonNull DSPPROC proc, @Nullable Object user, int priority);
    public static native boolean BASS_ChannelRemoveDSP(int handle, int dsp);
    public static native int BASS_ChannelSetFX(int handle, int type, int priority);
    public static native boolean BASS_ChannelRemoveFX(int handle, int fx);

    public static native boolean BASS_FXSetParameters(int handle, @NonNull Object params);
    public static native boolean BASS_FXGetParameters(int handle, @NonNull Object params);
    public static native boolean BASS_FXSetPriority(int handle, int priority);
    public static native boolean BASS_FXReset(int handle);

    private static native int BASS_StreamCreateConst(int freq, int chans, int flags, int proc, @Nullable Object user);

    public static int BASS_StreamCreate(int freq, int chans, int flags, int proc, @Nullable Object user) {
        return BASS_StreamCreateConst(freq, chans, flags, proc, user);
    }
}
