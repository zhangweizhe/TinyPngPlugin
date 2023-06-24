# TinyPngPlugin

![Build](https://github.com/zhangweizhe/TinyPngPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
A handy image compression plugin, based on TinyPng or PngQuant

With this plugin, you can **compress multiple images with one click**
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "TinyPng"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/zhangweizhe/TinyPngPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage
### Based on TinyPng
In this way, network and tinyPng api key are required

![tiny_png](https://raw.githubusercontent.com/zhangweizhe/TinyPngPlugin/main/capture/compress_mode_tiny_png.png)
- Get TinyPng api key

Get your TinyPng api key from [TinyPng](https://tinypng.com/developers)
![get api key](https://raw.githubusercontent.com/zhangweizhe/TinyPngPlugin/main/capture/get_api_key.png)
- Set api key

Set your api key in Preferences
![set api key](https://github.com/zhangweizhe/TinyPngPlugin/raw/main/capture/set_api_key.png)

### Based on PngQuant
In this way, network or api key is not required

![tiny_png](https://raw.githubusercontent.com/zhangweizhe/TinyPngPlugin/main/capture/compress_mode_tiny_png.png)


### Compress

Select (**Multi-selection support**) the images that needs to be compressed, and click <kbd>TinyPng</kbd>

![tiny.png](https://github.com/zhangweizhe/TinyPngPlugin/raw/main/capture/tiny.png)

And you will see the compress progress

![progress](https://github.com/zhangweizhe/TinyPngPlugin/raw/main/capture/compress_progress.png)

After all the images have been compressed, you will see how much space has been saved

![result](https://github.com/zhangweizhe/TinyPngPlugin/raw/main/capture/result.png)
---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation