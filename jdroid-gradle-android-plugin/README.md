# Jdroid Gradle Android Plugin
Gradle Plugins for Android Apps & Libraries

## Features

 * Default Google Android Plugin configuration
 * [Google Android Ribbonizer plugin](https://github.com/gfx/gradle-android-ribbonizer-plugin) integration
 * [Stetho](http://facebook.github.io/stetho/) integration
 * [Firebase Performance](https://firebase.google.com/docs/perf-mon/) integration
 * Android Tasks
    * Verify Missing Translations Between Locales
    * Verify Missing Translations
    * Prefix Verification
    * Increment app/library version according to [Semantic Versioning](http://semver.org/)
    * Copy generated APKs to a target directory

## Android Application Plugin

### Setup

Add the following lines to your `build.gradle`:

    buildscript {
      
      repositories {
        jcenter()
      }
      
      dependencies {
        classpath 'com.jdroidtools:jdroid-gradle-android-plugin:X.Y.Z'
      }
    }
    
    apply plugin: 'com.jdroid.android.application'

Replace the X.Y.Z by the [latest jdroid gradle plugin version](https://github.com/maxirosson/jdroid-gradle-plugin/releases/latest)

**NOTE:** You don't need to include the Google Android Gradle plugin, because it is already included by the Jdroid Gradle Plugin

The plugin adds the following configuration to your build script

    android {
        compileSdkVersion 27
        buildToolsVersion "27.0.2"
        defaultConfig {
            minSdkVersion 19
            targetSdkVersion 27
        }
        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
        lintOptions {
            checkReleaseBuilds false
            abortOnError false
        }
        packagingOptions {
            exclude 'META-INF/LICENSE'
            exclude 'META-INF/NOTICE'
        }
    }
    
## Android Library Plugin

### Setup

Add the following lines to your `build.gradle`:

    buildscript {
      
      repositories {
        jcenter()
      }
      
      dependencies {
        classpath 'com.jdroidtools:jdroid-gradle-android-plugin:X.Y.Z'
      }
    }
    
    apply plugin: 'com.jdroid.android.library'

Replace the X.Y.Z by the [latest jdroid gradle plugin version](https://github.com/maxirosson/jdroid-gradle-plugin/releases/latest)

**NOTE:** You don't need to include the Google Android Gradle plugin, because it is already included by the Jdroid Gradle Plugin


## Donations
Help us to continue with this project:

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2UEBTRTSCYA9L)

<a href='https://pledgie.com/campaigns/30030'><img alt='Click here to lend your support to: Jdroid and make a donation at pledgie.com !' src='https://pledgie.com/campaigns/30030.png?skin_name=chrome' border='0' ></a>
