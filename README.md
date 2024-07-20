# PlayStore
[![ToPlayStore](play_store_image/GetItOnGooglePlay_Badge_Web_color_Japanese.png)](https://play.google.com/store/apps/details?id=com.akaiyukiusagi.quicktodo)  

![ToPlayStoreQR](play_store_image/todo_app_download.png)

# Git
## Commitメッセージ
[Gitmoji](https://gitmoji.dev/)を参考に、したりしなかったり

- 🐛 バグ修正
- 🚀 新機能
- ♻️ リファクタリング
- 🎨 デザイン修正
- 🚑 ホットフィックス
- 🔖 リリースタグ
- ✅ テスト
- ➕ 依存関係追加
- ⬆️ 依存関係更新
- 📝 ドキュメント
- 🛠️ (何かに使えそうな気はする)

## ブランチ
- main: リリース用
  - マージしたら自動的にGooglePlayにアップロードされる
- develop: 開発用
- animatable: アニメーション確認用 デバッグ不可
- qa: テスト用 難読化あり、デバッグ不可


# CI/CD
mainブランチにマージしたらGooglePlayにaabをビルドし、署名、リリースします


# テスト
## スクリーンショットテスト
https://developer.android.com/studio/preview/compose-screenshot-testing  

#### スクリーンショット撮影
```
./gradlew updateDebugScreenshotTest
open ./app/src/debug/screenshotTest/reference
```

#### スクリーンショット比較
```
./gradlew validateDebugScreenshotTest
open ./app/build/reports/screenshotTest/preview/debug/index.html
open ./app/build/reports/screenshotTest/preview/debug
```

## ユニットテスト
#### カバレッジ更新
```
./gradlew createDebugCoverageReport
open ./app/build/reports/coverage/androidTest/debug/connected/index.html
open ./app/build/reports/androidTests/connected/debug/index.html
```

# ローカルビルド
#### .apkビルド
```
./gradlew assembleDebug
open ./app/build/outputs/apk/debug
```