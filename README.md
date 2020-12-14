[ ![Download](https://api.bintray.com/packages/zhangchuan622/maven/gifvideo/images/download.svg?version=0.3.0) ](https://bintray.com/zhangchuan622/maven/gifvideo/0.3.0/link)

Rendering and playing the video in the form of gif in Android will greatly improve the response efficiency of your application. For example, playing multiple video files in the recyclerview at the same time will not cause sliding and freezing. It will be easy to use gifvideo.You can [download](https://github.com/ben622/GifVideo/releases) and install gifvideo demo here

<img src="./screenshots/SVID_20201211_150841_1.gif" width="360px" height="640px"/>

### How do I use gifvideo?
Add dependencies in build.gradle

```
implementation 'com.ben.android:gifvideo:$VERSION'
```

```
VideoView videoView = new VideoView(context);
//set auto play
videoView.setAutoPlay(true)     //default true
//set filepath and start play
videoView.setPath(path);
//you can also control the pause or play by yourself
//videoView.start();
//videoView.stop();

```