# 🔧 实用工具

这里提供了一些实用的开发和部署工具脚本，帮助您更高效地使用芋道项目。

## 📋 工具列表

### 🛠️ 开发工具

| 工具 | 说明 | 用法 |
|------|------|------|
| `dev-tools.sh` | 开发环境工具脚本 | `./dev-tools.sh [命令]` |

## 🚀 开发工具脚本

### 功能特性

`dev-tools.sh` 是一个集成了常用开发操作的脚本工具，包含以下功能：

- ✅ **环境检查** - 检查Java、Maven、MySQL、Redis等环境
- 🏗️ **项目构建** - 一键构建项目
- ▶️ **运行项目** - 启动应用服务
- 🧹 **清理项目** - 清理构建文件
- 🧪 **运行测试** - 执行单元测试
- 📦 **项目打包** - 生成JAR文件
- 🐳 **Docker构建** - 构建Docker镜像
- 🗄️ **数据库初始化** - 初始化项目数据库
- 📊 **查看日志** - 实时查看应用日志
- 🩺 **健康检查** - 检查应用运行状态
- 🔄 **重启应用** - 重启运行中的应用

### 使用方法

```bash
# 添加执行权限
chmod +x tools/dev-tools.sh

# 查看帮助
./tools/dev-tools.sh help

# 检查开发环境
./tools/dev-tools.sh check-env

# 一键构建并运行
./tools/dev-tools.sh build
./tools/dev-tools.sh run

# 健康检查
./tools/dev-tools.sh health
```

### 常用场景

#### 🎯 新手入门

```bash
# 1. 检查环境
./tools/dev-tools.sh check-env

# 2. 初始化数据库
./tools/dev-tools.sh init-db

# 3. 构建并运行
./tools/dev-tools.sh build
./tools/dev-tools.sh run
```

#### 🔧 日常开发

```bash
# 快速重启应用
./tools/dev-tools.sh restart

# 查看实时日志
./tools/dev-tools.sh logs

# 运行测试
./tools/dev-tools.sh test
```

#### 📦 部署准备

```bash
# 打包应用
./tools/dev-tools.sh package

# 构建Docker镜像
./tools/dev-tools.sh docker-build
```

## 📚 更多工具

### 计划中的工具

- 🔄 **数据库迁移工具** - 数据库版本升级脚本
- 📊 **性能监控工具** - 应用性能分析脚本
- 🔧 **代码质量检查** - 代码规范检查工具
- 📱 **前端构建工具** - 前端项目构建脚本

### 贡献工具

如果您有实用的工具脚本，欢迎提交PR分享给社区！

**提交要求：**
1. 脚本要有详细的注释
2. 提供使用说明文档
3. 经过测试验证可用

## 🐛 问题反馈

如果在使用工具过程中遇到问题，请：

1. 检查脚本权限：`chmod +x tools/dev-tools.sh`
2. 确认环境依赖：`./tools/dev-tools.sh check-env`
3. 查看详细日志：运行脚本时会显示详细信息
4. 提交Issue：[Gitee Issues](https://gitee.com/zhijiantianya/ruoyi-vue-pro/issues)

## 💡 提示

- 建议将 `tools` 目录添加到 PATH 环境变量，方便全局使用
- 可以根据项目需要修改脚本中的默认配置
- 定期更新工具脚本以获得最新功能