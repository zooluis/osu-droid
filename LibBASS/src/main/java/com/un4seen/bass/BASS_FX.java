/*===========================================================================
 BASS_FX 2.4 - Copyright (c) 2002-2019 (: JOBnik! :) [Arthur Aminov, ISRAEL]
 [http://www.jobnik.org]
 Bugs/Suggestions/Questions:
 Forum  : http://www.un4seen.com/forum/?board=1 | http://www.jobnik.org/forums
 E-mail : bass_fx@jobnik.org
 NOTE: Works only with BASS_FX 2.4.12+ & BASS 2.4
===========================================================================*/
package com.un4seen.bass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * BASS_FX 音效扩展库 - 基于 BASS 2.4 的音频 DSP 处理、变速、反转、BPM 检测工具类
 * 核心功能：多通道音效处理、音频变速不变调、反向播放、节拍检测
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class BASS_FX {
    // ========================== 通道类型常量 ==========================
    /** 变速处理流通道类型 */
    public static final int BASS_CTYPE_STREAM_TEMPO = 0x1f200;
    /** 反向播放流通道类型 */
    public static final int BASS_CTYPE_STREAM_REVERSE = 0x1f201;

    // ========================== 全局功能标志 ==========================
    /** 释放音效句柄时，同时释放源音频通道句柄 */
    public static final int BASS_FX_FREESOURCE = 0x10000;

    // ========================== 多通道映射规则 ==========================
    // 3ch: 左前、右前、中置
    // 4ch: 左前、右前、左后/侧、右后/侧
    // 5ch: 左前、右前、中置、左后/侧、右后/侧
    // 6ch(5.1): 左前、右前、中置、LFE、左后/侧、右后/侧
    // 8ch(7.1): 左前、右前、中置、LFE、左后/侧、右后/侧、左后中、右后中

    /** 所有通道同时生效（默认） */
    public static final int BASS_BFX_CHANALL = -1;
    /** 禁用所有通道的音效 */
    public static final int BASS_BFX_CHANNONE = 0;
    /** 左前通道 */
    public static final int BASS_BFX_CHAN1 = 1;
    /** 右前通道 */
    public static final int BASS_BFX_CHAN2 = 2;
    /** 中置通道（3/5/6/8ch） */
    public static final int BASS_BFX_CHAN3 = 4;
    /** 左后/侧通道（4/5/6/8ch） */
    public static final int BASS_BFX_CHAN4 = 8;
    /** 右后/侧通道（4/5/6/8ch） */
    public static final int BASS_BFX_CHAN5 = 16;
    /** LFE 低频通道（6/8ch） */
    public static final int BASS_BFX_CHAN6 = 32;
    /** 左后中通道（8ch） */
    public static final int BASS_BFX_CHAN7 = 64;
    /** 右后中通道（8ch） */
    public static final int BASS_BFX_CHAN8 = 128;

    // ========================== DSP 音效类型常量 ==========================
    /** 声道音量乒乓切换（多通道） */
    public static final int BASS_FX_BFX_ROTATE = 0x10000;
    /** 音量调节（多通道） */
    public static final int BASS_FX_BFX_VOLUME = 0x10003;
    /** 峰值均衡器（多通道） */
    public static final int BASS_FX_BFX_PEAKEQ = 0x10004;
    /** 声道混合/重映射（多通道） */
    public static final int BASS_FX_BFX_MIX = 0x10007;
    /** 动态增益（多通道） */
    public static final int BASS_FX_BFX_DAMP = 0x10008;
    /** 自动哇音（多通道） */
    public static final int BASS_FX_BFX_AUTOWAH = 0x10009;
    /** 移相器（多通道） */
    public static final int BASS_FX_BFX_PHASER = 0x1000b;
    /** 合唱/镶边（多通道，替代废弃的 FLANGER） */
    public static final int BASS_FX_BFX_CHORUS = 0x1000d;
    /** 失真（多通道） */
    public static final int BASS_FX_BFX_DISTORTION = 0x10010;
    /** 压缩器2代（多通道，替代废弃的 COMPRESSOR） */
    public static final int BASS_FX_BFX_COMPRESSOR2 = 0x10011;
    /** 音量包络（多通道） */
    public static final int BASS_FX_BFX_VOLUME_ENV = 0x10012;
    /** 双二阶滤波器（多通道，替代废弃的 LPF/APF） */
    public static final int BASS_FX_BFX_BQF = 0x10013;
    /** 回声4代（多通道，替代废弃的 ECHO/ECHO2/ECHO3） */
    public static final int BASS_FX_BFX_ECHO4 = 0x10014;
    /** 音调偏移（基于FFT，移动端不可用） */
    public static final int BASS_FX_BFX_PITCHSHIFT = 0x10015;
    /** 混响（基于Freeverb算法，多通道，替代废弃的 REVERB） */
    public static final int BASS_FX_BFX_FREEVERB = 0x10016;

    // ========================== 双二阶滤波器类型 ==========================
    /** 低通滤波器 */
    public static final int BASS_BFX_BQF_LOWPASS = 0;
    /** 高通滤波器 */
    public static final int BASS_BFX_BQF_HIGHPASS = 1;
    /** 带通滤波器（0dB峰值增益） */
    public static final int BASS_BFX_BQF_BANDPASS = 2;
    /** 带通滤波器（恒定边带增益，峰值增益=Q值） */
    public static final int BASS_BFX_BQF_BANDPASS_Q = 3;
    /** 陷波滤波器 */
    public static final int BASS_BFX_BQF_NOTCH = 4;
    /** 全通滤波器 */
    public static final int BASS_BFX_BQF_ALLPASS = 5;
    /** 峰值均衡滤波器 */
    public static final int BASS_BFX_BQF_PEAKINGEQ = 6;
    /** 低架滤波器 */
    public static final int BASS_BFX_BQF_LOWSHELF = 7;
    /** 高架滤波器 */
    public static final int BASS_BFX_BQF_HIGHSHELF = 8;

    // ========================== Freeverb 混响模式 ==========================
    /** 冻结混响模式 */
    public static final int BASS_BFX_FREEVERB_MODE_FREEZE = 1;

    // ========================== 变速属性常量 ==========================
    /** 变速倍率属性（通过 BASS_ChannelSet/GetAttribute 访问） */
    public static final int BASS_ATTRIB_TEMPO = 0x10000;
    /** 音调偏移属性 */
    public static final int BASS_ATTRIB_TEMPO_PITCH = 0x10001;
    /** 采样率偏移属性 */
    public static final int BASS_ATTRIB_TEMPO_FREQ = 0x10002;

    /** 变速算法：线性插值（性能高，音质一般） */
    public static final int BASS_FX_TEMPO_ALGO_LINEAR = 0x200;
    /** 变速算法：立方插值（默认，音质平衡） */
    public static final int BASS_FX_TEMPO_ALGO_CUBIC = 0x400;
    /** 变速算法：香农插值（音质高，性能消耗大） */
    public static final int BASS_FX_TEMPO_ALGO_SHANNON = 0x800;

    // ========================== 反向播放属性常量 ==========================
    /** 播放方向属性（通过 BASS_ChannelSet/GetAttribute 访问） */
    public static final int BASS_ATTRIB_REVERSE_DIR = 0x11000;
    /** 反向播放 */
    public static final int BASS_FX_RVS_REVERSE = -1;
    /** 正向播放 */
    public static final int BASS_FX_RVS_FORWARD = 1;

    // ========================== BPM 检测标志 ==========================
    /** 后台检测 BPM（仅 Windows 平台有效） */
    public static final int BASS_FX_BPM_BKGRND = 1;
    /** 自动将 BPM 值加倍（当检测值 < minBPM*2 时） */
    public static final int BASS_FX_BPM_MULT2 = 2;

    // ========================== 私有构造函数：禁止实例化 ==========================
    private BASS_FX() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    // ========================== 基础方法 ==========================
    /**
     * 获取 BASS_FX 版本号
     * @return 版本号（十六进制格式，如 0x20412 对应 2.4.12）
     */
    public static native int BASS_FX_GetVersion();

    // ========================== 工具方法：分贝与线性音量转换 ==========================
    /**
     * 线性音量转分贝（dB）
     * @param level 线性音量 [0, 1]
     * @return 分贝值（0dB 为最大音量，负数为衰减）
     */
    public static double linear2dB(double level) {
        return level <= 0 ? -Double.MAX_VALUE : 20 * Math.log10(level);
    }

    /**
     * 分贝（dB）转线性音量
     * @param dB 分贝值（负数为衰减，0dB 为最大）
     * @return 线性音量 [0, 1]
     */
    public static double dB2Linear(double dB) {
        return Math.pow(10, dB / 20);
    }

    /**
     * 获取 N 通道的通道掩码（支持 8 通道以上扩展）
     * @param n 通道序号（从 1 开始）
     * @return 通道掩码
     */
    public static int getChannelMask(int n) {
        return n < 1 ? BASS_BFX_CHANNONE : 1 << (n - 1);
    }

    // ========================== 变速处理相关方法 ==========================
    /**
     * 创建变速处理通道
     * @param chan 源音频通道句柄
     * @param flags 标志位（如 BASS_FX_FREESOURCE）
     * @return 变速通道句柄（失败返回 0）
     */
    public static native int BASS_FX_TempoCreate(int chan, int flags);

    /**
     * 获取变速通道对应的源通道句柄
     * @param chan 变速通道句柄
     * @return 源通道句柄（失败返回 0）
     */
    public static native int BASS_FX_TempoGetSource(int chan);

    /**
     * 获取变速通道的当前速率比
     * @param chan 变速通道句柄
     * @return 速率比（1.0 为原速，>1 加速，<1 减速）
     */
    public static native float BASS_FX_TempoGetRateRatio(int chan);

    // ========================== 反向播放相关方法 ==========================
    /**
     * 创建反向播放通道
     * @param chan 源音频通道句柄
     * @param decBlock 解码块大小（建议 0.1~1.0 秒）
     * @param flags 标志位（如 BASS_FX_FREESOURCE）
     * @return 反向通道句柄（失败返回 0）
     * @note MOD 格式需配合 BASS_MUSIC_PRESCAN 标志加载
     */
    public static native int BASS_FX_ReverseCreate(int chan, float decBlock, int flags);

    /**
     * 获取反向通道对应的源通道句柄
     * @param chan 反向通道句柄
     * @return 源通道句柄（失败返回 0）
     */
    public static native int BASS_FX_ReverseGetSource(int chan);

    // ========================== BPM 检测相关方法 ==========================
    /**
     * 解码并检测音频 BPM（节拍数/分钟）
     * @param chan 音频通道句柄
     * @param startSec 检测起始时间（秒）
     * @param endSec 检测结束时间（秒）
     * @param minMaxBPM 最小/最大 BPM 范围（高16位=最大，低16位=最小）
     * @param flags 标志位（如 BASS_FX_BPM_BKGRND/BASS_FX_BPM_MULT2）
     * @param progressProc 进度回调（可 null）
     * @param user 用户自定义参数（传递给回调）
     * @return 检测到的 BPM 值（失败返回 0）
     */
    public static native float BASS_FX_BPM_DecodeGet(int chan, double startSec, double endSec,
                                                    int minMaxBPM, int flags,
                                                    @Nullable BPMPROGRESSPROC progressProc,
                                                    @Nullable Object user);

    /**
     * 设置 BPM 实时检测回调
     * @param handle BPM 检测句柄
     * @param proc BPM 回调函数
     * @param period 检测周期（秒）
     * @param minMaxBPM 最小/最大 BPM 范围
     * @param flags 标志位
     * @param user 用户自定义参数
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_CallbackSet(int handle, @NonNull BPMPROC proc,
                                                        double period, int minMaxBPM, int flags,
                                                        @Nullable Object user);

    /**
     * 重置 BPM 检测回调
     * @param handle BPM 检测句柄
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_CallbackReset(int handle);

    /**
     * 释放 BPM 检测句柄
     * @param handle BPM 检测句柄
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_Free(int handle);

    // ========================== 节拍触发相关方法 ==========================
    /**
     * 设置节拍位置触发回调
     * @param handle BPM 检测句柄
     * @param proc 节拍回调函数
     * @param user 用户自定义参数
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_BeatCallbackSet(int handle, @NonNull BPMBEATPROC proc,
                                                            @Nullable Object user);

    /**
     * 重置节拍触发回调
     * @param handle BPM 检测句柄
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_BeatCallbackReset(int handle);

    /**
     * 解码并检测节拍位置
     * @param chan 音频通道句柄
     * @param startSec 起始时间（秒）
     * @param endSec 结束时间（秒）
     * @param flags 标志位
     * @param proc 节拍回调函数
     * @param user 用户自定义参数
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_BeatDecodeGet(int chan, double startSec, double endSec,
                                                          int flags, @NonNull BPMBEATPROC proc,
                                                          @Nullable Object user);

    /**
     * 设置节拍检测参数
     * @param handle BPM 检测句柄
     * @param bandwidth 带宽（Hz）
     * @param centerfreq 中心频率（Hz）
     * @param beatRtime 节拍响应时间（秒）
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_BeatSetParameters(int handle, float bandwidth,
                                                              float centerfreq, float beatRtime);

    /**
     * 获取节拍检测参数
     * @param handle BPM 检测句柄
     * @param bandwidth 输出参数：带宽
     * @param centerfreq 输出参数：中心频率
     * @param beatRtime 输出参数：节拍响应时间
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_BeatGetParameters(int handle,
                                                              @NonNull Float[] bandwidth,
                                                              @NonNull Float[] centerfreq,
                                                              @NonNull Float[] beatRtime);

    /**
     * 释放节拍检测句柄
     * @param handle 节拍检测句柄
     * @return 是否成功
     */
    public static native boolean BASS_FX_BPM_BeatFree(int handle);

    // ========================== 回调接口定义 ==========================
    /**
     * BPM 检测结果回调接口
     */
    @FunctionalInterface
    public interface BPMPROC {
        /**
         * @param chan 音频通道句柄
         * @param bpm 检测到的 BPM 值
         * @param user 用户自定义参数
         */
        void onBpmDetected(int chan, float bpm, @Nullable Object user);
    }

    /**
     * BPM 检测进度回调接口
     */
    @FunctionalInterface
    public interface BPMPROGRESSPROC {
        /**
         * @param chan 音频通道句柄
         * @param percent 检测进度（0~100）
         * @param user 用户自定义参数
         */
        void onProgress(int chan, float percent, @Nullable Object user);
    }

    /**
     * 节拍位置触发回调接口
     */
    @FunctionalInterface
    public interface BPMBEATPROC {
        /**
         * @param chan 音频通道句柄
         * @param beatpos 节拍位置（秒）
         * @param user 用户自定义参数
         */
        void onBeatDetected(int chan, double beatpos, @Nullable Object user);
    }

    // ========================== 音效参数实体类 ==========================
    /**
     * 声道音量乒乓切换参数
     */
    public static class BASS_BFX_ROTATE {
        /** 旋转速率（Hz），负数为反向旋转 */
        public float fRate;
        /** 生效通道掩码（仅支持偶数通道数） */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 音量调节参数
     */
    public static class BASS_BFX_VOLUME {
        /** 生效通道掩码（0 为全局音量） */
        public int lChannel = BASS_BFX_CHANALL;
        /** 线性音量 [0, 1]，大于1为增益 */
        public float fVolume = 1.0f;
    }

    /**
     * 峰值均衡器参数
     */
    public static class BASS_BFX_PEAKEQ {
        /** 均衡器频段数（越多音质越好，CPU占用越高） */
        public int lBand = 8;
        /** 带宽（倍频程），优先级高于 fQ */
        public float fBandwidth = 1.0f;
        /** Q 值（0~1），带宽未设置时生效 */
        public float fQ = 0.5f;
        /** 中心频率（Hz），范围 [1, 采样率/2] */
        public float fCenter = 1000.0f;
        /** 增益（dB），范围 [-15, 15]，可超出 */
        public float fGain = 0.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 声道混合/重映射参数
     */
    public static class BASS_BFX_MIX {
        /** 通道映射数组，索引对应目标通道，值对应源通道掩码 */
        public int[] lChannel;
    }

    /**
     * 动态增益参数
     */
    public static class BASS_BFX_DAMP {
        /** 目标音量 [0, 1] */
        public float fTarget = 0.8f;
        /** 静音阈值 [0, 1] */
        public float fQuiet = 0.2f;
        /** 增益调整速率 [0, 1] */
        public float fRate = 0.5f;
        /** 增益倍数 [0, n] */
        public float fGain = 1.0f;
        /** 增益延迟（秒） */
        public float fDelay = 0.1f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 自动哇音参数
     */
    public static class BASS_BFX_AUTOWAH {
        /** 干声混合比 [-2, 2] */
        public float fDryMix = 1.0f;
        /** 湿声混合比 [-2, 2] */
        public float fWetMix = 1.0f;
        /** 反馈量 [-1, 1] */
        public float fFeedback = 0.5f;
        /** 扫描速率（Hz） [0, 10] */
        public float fRate = 1.0f;
        /** 扫描范围（倍频程） [0, 10] */
        public float fRange = 2.0f;
        /** 基础频率（Hz） [0, 1000] */
        public float fFreq = 100.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 移相器参数
     */
    public static class BASS_BFX_PHASER {
        /** 干声混合比 [-2, 2] */
        public float fDryMix = 1.0f;
        /** 湿声混合比 [-2, 2] */
        public float fWetMix = 1.0f;
        /** 反馈量 [-1, 1] */
        public float fFeedback = 0.5f;
        /** 扫描速率（Hz） [0, 10] */
        public float fRate = 1.0f;
        /** 扫描范围（倍频程） [0, 10] */
        public float fRange = 2.0f;
        /** 基础频率（Hz） [0, 1000] */
        public float fFreq = 100.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 合唱/镶边参数
     */
    public static class BASS_BFX_CHORUS {
        /** 干声混合比 [-2, 2] */
        public float fDryMix = 1.0f;
        /** 湿声混合比 [-2, 2] */
        public float fWetMix = 1.0f;
        /** 反馈量 [-1, 1] */
        public float fFeedback = 0.5f;
        /** 最小延迟（ms） [0, 6000] */
        public float fMinSweep = 1.0f;
        /** 最大延迟（ms） [0, 6000] */
        public float fMaxSweep = 10.0f;
        /** 扫描速率（ms/s） [0, 1000] */
        public float fRate = 10.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 失真参数
     */
    public static class BASS_BFX_DISTORTION {
        /** 驱动强度 [0, 5] */
        public float fDrive = 1.0f;
        /** 干声混合比 [-5, 5] */
        public float fDryMix = 1.0f;
        /** 湿声混合比 [-5, 5] */
        public float fWetMix = 1.0f;
        /** 反馈量 [-1, 1] */
        public float fFeedback = 0.5f;
        /** 输出音量 [0, 2] */
        public float fVolume = 1.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 压缩器2代参数
     */
    public static class BASS_BFX_COMPRESSOR2 {
        /** 输出增益（dB） [-60, 60] */
        public float fGain = 0.0f;
        /** 压缩阈值（dB） [-60, 0] */
        public float fThreshold = -10.0f;
        /** 压缩比 [1, n] */
        public float fRatio = 2.0f;
        /** 启动时间（ms） [0.01, 1000] */
        public float fAttack = 10.0f;
        /** 释放时间（ms） [0.01, 5000] */
        public float fRelease = 100.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 音量包络节点参数
     */
    public static class BASS_BFX_ENV_NODE {
        /** 节点位置（秒），第一个节点必须为 0 */
        public double pos;
        /** 节点音量值（线性） */
        public float val;
    }

    /**
     * 音量包络参数
     */
    public static class BASS_BFX_VOLUME_ENV {
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
        /** 节点数量 */
        public int lNodeCount;
        /** 节点数组 */
        public BASS_BFX_ENV_NODE[] pNodes;
        /** 是否跟随源音频位置 */
        public boolean bFollow = true;
    }

    /**
     * 双二阶滤波器参数
     */
    public static class BASS_BFX_BQF {
        /** 滤波器类型 {@link #BASS_BFX_BQF_LOWPASS} 等 */
        public int lFilter = BASS_BFX_BQF_LOWPASS;
        /** 中心频率（Hz） [1, 采样率/2] */
        public float fCenter = 1000.0f;
        /** 增益（dB），仅峰值/架式滤波器生效 [-15, 15] */
        public float fGain = 0.0f;
        /** 带宽（倍频程），优先级高于 fQ [0.1, 10] */
        public float fBandwidth = 1.0f;
        /** Q 值，带宽未设置时生效 [0.1, n] */
        public float fQ = 0.5f;
        /** 斜率参数，仅架式滤波器生效 [0.1, n] */
        public float fS = 1.0f;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 回声4代参数
     */
    public static class BASS_BFX_ECHO4 {
        /** 干声混合比 [-2, 2] */
        public float fDryMix = 1.0f;
        /** 湿声混合比 [-2, 2] */
        public float fWetMix = 1.0f;
        /** 反馈量 [-1, 1] */
        public float fFeedback = 0.5f;
        /** 延迟时间（秒） [0, n] */
        public float fDelay = 0.5f;
        /** 是否开启立体声交叉回声 */
        public boolean bStereo = true;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * 音调偏移参数（移动端不可用）
     */
    public static class BASS_BFX_PITCHSHIFT {
        /** 音调偏移倍数 [0.5, 2]，1 为原调，优先级高于 fSemitones */
        public float fPitchShift = 1.0f;
        /** 半音偏移，0 为原调 */
        public float fSemitones = 0.0f;
        /** FFT 帧大小（2的幂，<=8192），默认 2048 */
        public int lFFTsize = 2048;
        /** 过采样率（>=4），默认 8，32 音质最佳 */
        public int lOsamp = 8;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }

    /**
     * Freeverb 混响参数
     */
    public static class BASS_BFX_FREEVERB {
        /** 干声混合比 [0, 1] */
        public float fDryMix = 0.0f;
        /** 湿声混合比 [0, 3] */
        public float fWetMix = 1.0f;
        /** 房间大小 [0, 1] */
        public float fRoomSize = 0.5f;
        /** 阻尼 [0, 1] */
        public float fDamp = 0.5f;
        /** 立体声宽度 [0, 1] */
        public float fWidth = 1.0f;
        /** 混响模式 {@link #BASS_BFX_FREEVERB_MODE_FREEZE} */
        public int lMode = 0;
        /** 生效通道掩码 */
        public int lChannel = BASS_BFX_CHANALL;
    }
}
