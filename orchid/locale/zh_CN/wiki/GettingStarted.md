使用 Oni 有两种方法:

1. 让服主把Oni当作普通的前置插件安装
2. 使用`OniBootstrap`去引导你的插件，以此自动下载依赖，减小插件体积。 （只有 Gradle用户 能用）

请优先使用第二种方式。如果你是Maven或其他构建工具(如SBT)用户，可能你需要去手动编写 OniBootstrap 的引导文件。(`oni.setting.json`)  
本文将会教你如何使用第二种方式部署你的 Hello World.

## Linker

//TODO

## Download and load a plugin in runtime

//TODO