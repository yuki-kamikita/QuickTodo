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
- 🛠️ Gradleなどの設定類

## ブランチ
- main: リリース用
  - マージしたら自動的にGooglePlayにアップロードされる
- develop: 開発用
- animatable: アニメーション確認用 デバッグ不可
- qa: テスト用 難読化あり、デバッグ不可


# ディレクトリ構成（抜粋）
- `core/`: アプリ全体で使う共通処理や定数、拡張関数など
- `dataLayer/`: データ取得・保存（リポジトリ、データソース、エンティティ等）
- `uiLayer/`: UI関連
  - `component/`: 複数画面で使いまわすもの
    - `system/`: バイブレーションや通知などシステム機能
    - `ui/`
      - `parts/`: 見た目を主とするUI部品（ボタン、テキストフィールドなどのMaterial3関連）
      - `behavior/`: 機能や振る舞いを主とするUI部品（ライフサイクル、アニメーションなど）
      - `layout/`: 配置を主とするUI部品（スロットパターンなど）
  - `screen/`: 画面単位のUI
  - `notification/`: 通知の比重が大きいアプリなので分割


# CI/CD
mainブランチにマージしたらGooglePlayにaabをビルドし、署名、リリースします


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

# ローカルビルド
.apkビルド
```
./gradlew assembleDebug
```