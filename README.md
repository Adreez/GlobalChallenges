<h1 align="center">GlobalChallenges Minecraft Plugin</h1>

GlobalChallenges is a Minecraft plugin designed to add global challenges and tasks to enhance the gameplay experience. This README.md file provides essential information on installing the plugin, system requirements, developer API, and additional resources.

## Installation

### Requirements

- **Java:** GlobalChallenges requires Java 17 to run. Make sure you have Java 17 or later installed on your server.

### Steps

1. **Download:**
   - Download the latest version of GlobalChallenges from the [Releases page](https://github.com/your-username/GlobalChallenges/releases).

2. **Installation:**
   - Place the downloaded JAR file into your server's `plugins` directory.

3. **Configuration:**
   - Customize the plugin settings in the `config.yml` file located in the `plugins/GlobalChallenges` folder.

4. **Restart:**
   - Restart your Minecraft server to apply the changes.

## Wiki

For more detailed information, please refer to our [Wiki](https://github.com/Adreez/GlobalChallenges/wiki).

## Developer API

### Gradle Integration

To add GlobalChallenges to your Gradle project, include the following dependency in your `build.gradle` file:

```groovy
repositories {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.Adreez:GlobalChallenges:1.0.1'
}
```
