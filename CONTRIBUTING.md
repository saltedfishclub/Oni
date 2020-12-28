# Contribute to Oni
(*ENGLISH COMING SOON*)  
Oni 是一个Bukkit敏捷开发框架，我们欢迎所有人对Oni做贡献，但请遵循以下规则：  
- 遵守 Google Java 代码规范
- 对于 Onion 原型请不要擅自改动。 Onion原型就是Onion的基类，通常是以`Oni+Bukkit接口名`格式命名的抽象类，原型提供了对目标类(即：原型实现的接口)提供扩展的可能性。  
  为了防止在未来与Bukkit的同名 API 语义不一致无法平滑迁移，通常对Onion原型的改动都是委托方法，且委托方法行为要与原方法完全一致。详见`新增方法`    
  但最好对于 Onion 的操作尽可能放到 `XXOnion.Accessor`
- 请尽量遵循我们的 Commit 信息格式规范  
  https://gist.github.com/iceBear67/018aa3eb37c9cc1312864b7b7ffa3820
- 避免添加更多依赖
- 请在`dev`分支上提交!
- 我们推荐创建功能的时候同时也开一个`issue`用于该功能的讨论，也方便合并到里程碑中。 对于Collaborator:
- Code Review 时请注意上述规则
- 注意关闭不合格的 issue
- 防止社区出现人身攻击行为

## 版本规范

`Core` 参考 [Mirai版本规范](https://github.com/mamoe/mirai/blob/dev/docs/Evolution.md#%E7%89%88%E6%9C%AC%E8%A7%84%E8%8C%83)  
`BootStrap`,`MavenDownloader` 按照`<兼容性更新>.<功能性更新>.<修复>`命名.

## 新增方法

请给方法加上合适的注解，例如`LowLevelAPI`表示低级API，`DelegateMethod`表示该方法最终委托的Class，`ApiStatus.Internal`表示内部方法 也要加上描述清晰的 Javadoc
并且标注 `@since 当前ONI版本号`  
这样做的目的是为了让使用者通过 IDE提示 或者 sourceJar 更快的了解这个方法的作用。