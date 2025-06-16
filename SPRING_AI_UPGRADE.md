# Spring AI 升级到 1.0.0 总结

## 升级版本
- **从**: Spring AI 1.0.0-M6
- **到**: Spring AI 1.0.0 GA

## 主要变化

### 1. 依赖管理
- **新增**: 添加了 Spring AI BOM 依赖管理
- **版本**: 所有 Spring AI 依赖现在通过 BOM 统一管理，无需指定版本号

### 2. 依赖名称变更
按照新的命名模式更新了所有依赖：

**模型依赖**:
- `spring-ai-openai-spring-boot-starter` → `spring-ai-starter-model-openai`
- `spring-ai-azure-openai-spring-boot-starter` → `spring-ai-starter-model-azure-openai`
- `spring-ai-ollama-spring-boot-starter` → `spring-ai-starter-model-ollama`
- `spring-ai-stability-ai-spring-boot-starter` → `spring-ai-starter-model-stability-ai`
- `spring-ai-zhipuai-spring-boot-starter` → `spring-ai-starter-model-zhipu-ai`
- `spring-ai-minimax-spring-boot-starter` → `spring-ai-starter-model-minimax`

**向量存储依赖**:
- `spring-ai-qdrant-store` → `spring-ai-starter-vector-store-qdrant`
- `spring-ai-redis-store` → `spring-ai-starter-vector-store-redis`
- `spring-ai-milvus-store` → `spring-ai-starter-vector-store-milvus`

### 3. 移除的模块
以下模块在 Spring AI 1.0.0 中已被移除：

**⚠️ 已移除的模块**:
- **Moonshot (月之暗面)**: `spring-ai-moonshot-spring-boot-starter`
- **QianFan (文心一言)**: `spring-ai-qianfan-spring-boot-starter`

> **注意**: 这些模块已移至 Spring AI Community repository。如需继续使用，请参考社区版本。

### 4. 代码变更

#### 4.1 AiUtils.java
- 移除了 `MoonshotChatOptions` 和 `QianFanChatOptions` 导入
- 更新了 `buildChatOptions` 方法，对 `YI_YAN` 和 `MOONSHOT` 平台抛出 `UnsupportedOperationException`

#### 4.2 AiModelFactoryImpl.java
- 移除了相关导入和方法：
  - `buildYiYanChatModel()`
  - `buildQianFanImageModel()`
  - `buildMoonshotChatModel()`
  - `buildYiYanEmbeddingModel()`
- 更新了所有相关的 switch 语句，对已移除的平台抛出异常

#### 4.3 AiImageServiceImpl.java
- 移除了 `QianFanImageOptions` 导入
- 更新了 `buildImageOptions` 方法，对 `YI_YAN` 平台抛出异常

#### 4.4 测试文件
删除了不兼容的测试文件：
- `QianFanImageTests.java`
- `YiYanChatModelTests.java`
- `MoonshotChatModelTests.java`

### 5. 通义千问配置
- 通义千问依赖 `spring-ai-alibaba-starter` 版本从 `1.0.0-M6.1` 更新到 `1.0.0.1`

## 兼容性说明

### ✅ 继续支持的模型
- OpenAI
- Azure OpenAI
- Ollama
- Stability AI
- 智谱 GLM (ZhiPu AI)
- MiniMax
- 通义千问 (TongYi)

### ❌ 不再支持的模型
- Moonshot (月之暗面)
- QianFan (文心一言)

### 🔄 迁移建议
如果您的应用程序使用了被移除的模型，建议：

1. **迁移到其他支持的模型**：
   - 从 Moonshot → OpenAI、智谱GLM 或其他聊天模型
   - 从 QianFan → 通义千问、智谱GLM 或其他聊天模型

2. **使用社区版本**：
   - 查看 Spring AI Community repository 获取这些模型的社区支持版本

## 验证升级

升级完成后，请验证：

1. ✅ 应用程序正常启动
2. ✅ 现有的 AI 功能正常工作
3. ✅ 向量存储连接正常
4. ✅ 测试覆盖的模型功能正常

## 参考文档

- [Spring AI 1.0.0 发布说明](https://spring.io/blog/2025/05/20/spring-ai-1-0-GA-released)
- [Spring AI 升级指南](https://docs.spring.io/spring-ai/reference/upgrade-notes.html)
- [Spring AI Community Repository](https://github.com/spring-ai-community)

---

**升级日期**: $(date)
**升级人**: [请填写升级人员]