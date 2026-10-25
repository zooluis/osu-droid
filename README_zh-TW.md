![osudroid](https://github.com/osudroid/osu-droid/assets/52914632/8747ab50-c77d-4808-91aa-5058891e4bcd)

# osu-droid
[![Official International Discord](https://discordapp.com/api/guilds/316545691545501706/widget.png?style=shield)](https://discord.gg/nyD92cE)
[![Android](https://github.com/osudroid/osu-droid/workflows/Android/badge.svg?branch=master)](https://github.com/osudroid/osu-droid/actions?query=workflow%3A"Android")
[![CodeFactor](https://www.codefactor.io/repository/github/osudroid/osu-droid/badge)](https://www.codefactor.io/repository/github/osudroid/osu-droid)

osu!droid 是一個面向 Android 裝置的免費點擊節奏遊戲。它是由 [osu!](https://osu.ppy.sh/home) 社群多年前孵化的一款遊戲。這個專案現在由一小群愛好者以及外國朋友共同開發。

## 狀態

osu!droid 持續開發中。功能不斷新增、bug 不斷修復，但它可玩又有趣！

### 下載原始碼

克隆倉庫：

```sh
git clone https://github.com/osudroid/osu-droid.git
```
在 Android Studio 中開啟資料夾。

要將原始碼更新到最新提交，在 `osu-droid` 目錄內執行：

```she
git pull
```

### 建置

osu!droid 需要 Java 17 才能建置。

在 Android Studio 中，你可以 `Build` 一個 debug 版本測試你的變更。你的 `.apk` 輸出目錄在 `osu-droid` 目錄的 `build/output` 內。

如果你喜歡命令列，且在 Linux 上，請在該目錄內執行 `chmod +x gradlew` 和 `./gradlew assembleDebug` 來建置 debug `.apk` 檔案。

## 貢獻

我們歡迎任何有益的貢獻。無法貢獻程式碼的人可以透過[功能請求](https://github.com/osudroid/osu-droid/issues/11)或 [GitHub issues](https://github.com/osudroid/osu-droid/issues)建議小變更，如語法修正或報告用戶端問題。

## 授權條款

osu!droid 採用 [Apache-2.0](https://opensource.org/licenses/Apache-2.0) 授權條款。更多資訊請見 LICENSE 檔案。
