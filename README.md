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

# CI/CD
mainブランチにマージしたらGooglePlayにaabをビルドし、署名します


# テスト
## スクリーンショットテスト
https://developer.android.com/studio/preview/compose-screenshot-testing  
[保存場所](app/src/debug/screenshotTest/reference)

#### スクリーンショット撮影
```
./gradlew updateDebugScreenshotTest
```

#### スクリーンショット比較
```
./gradlew validateDebugScreenshotTest
```

## ユニットテスト

カバレッジ更新
```
./gradlew createDebugCoverageReport
```

[カバレッジ保存場所](./app/build/reports/coverage/androidTest/debug/connected/index.html)  
カバレッジをブラウザで開く
```
open ./app/build/reports/coverage/androidTest/debug/connected/index.html
```

.apkビルド
```
./gradlew assembleDebug
```