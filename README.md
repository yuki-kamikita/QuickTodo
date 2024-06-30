# CI/CD
mainブランチにマージしたらGooglePlayにaabをビルドし、署名します


# テスト

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