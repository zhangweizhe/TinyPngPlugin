# TinyPngPlugin

![Build](https://github.com/zhangweizhe/TinyPngPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
A handy image compression plugin, based on TinyPng.
With this plugin, you can **compress multiple images with one click**, instead of the old way of:

<s>1. open the TinyPng website</s>

<s>2. Upload the images to be compressed</s>

<s>3. Compress the image</s>

<s>4. Download the compressed images and overwrite them in your project</s>

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "TinyPng"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/zhangweizhe/TinyPngPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage
- Get TinyPng api key

Get your TinyPng api key from [TinyPng](https://tinypng.com/developers)
![get api key](https://raw.githubusercontent.com/zhangweizhe/TinyPngPlugin/main/capture/get_api_key.png)
- Set api key

Set your api key in Prefernces
![set api key](https://github.com/zhangweizhe/TinyPngPlugin/raw/main/capture/set_api_key.png)
- Compress

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