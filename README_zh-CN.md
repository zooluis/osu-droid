![osudroid](https://github.com/osudroid/osu-droid/assets/52914632/8747ab50-c77d-4808-91aa-5058891e4bcd)

# osu-droid
[![Official International Discord](https://discordapp.com/api/guilds/316545691545501706/widget.png?style=shield)](https://discord.gg/nyD92cE)
[![Android](https://github.com/osudroid/osu-droid/workflows/Android/badge.svg?branch=master)](https://github.com/osudroid/osu-droid/actions?query=workflow%3A"Android")
[![CodeFactor](https://www.codefactor.io/repository/github/osudroid/osu-droid/badge)](https://www.codefactor.io/repository/github/osudroid/osu-droid)

osu!droid 是一个面向 Android 设备的免费点击节奏游戏。它是由 [osu!](https://osu.ppy.sh/home) 社区多年前孵化的一款游戏。这个项目现在由一小群爱好者以及外国朋友共同开发。

## 状态

osu!droid 持续开发中。功能不断添加、bug 不断修复，但它可玩又有趣！

### 下载源码

克隆仓库：

```sh
git clone https://github.com/osudroid/osu-droid.git
```
在 Android Studio 中打开文件夹。

要将源码更新到最新提交，在 `osu-droid` 目录内运行：

```she
git pull
```

### 构建

osu!droid 需要 Java 17 才能构建。

在 Android Studio 中，你可以 `Build` 一个 debug 版本测试你的改动。你的 `.apk` 输出目录在 `osu-droid` 目录的 `build/output` 内。

如果你喜欢命令行，且在 Linux 上，请在该目录内运行 `chmod +x gradlew` 和 `./gradlew assembleDebug` 来构建 debug `.apk` 文件。

## 贡献

我们欢迎任何有益的贡献。无法贡献代码的人可以通过[功能请求](https://github.com/osudroid/osu-droid/issues/11)或 [GitHub issues](https://github.com/osudroid/osu-droid/issues)建议小改动，如语法修正或报告客户端问题。

## 许可证

osu!droid 采用 [Apache-2.0](https://opensource.org/licenses/Apache-2.0) 许可证。更多信息请见 LICENSE 文件。
