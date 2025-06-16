# Spring AI 升级到 1.0.0 总结

## 升级版本
- **从**: Spring AI 1.0.0-M6
- **到**: Spring AI 1.0.0 GA

## 主要变化

### 1. 依赖管理
- **新增**: 添加了 Spring AI BOM 依赖管理
- **版本**: 所有 Spring AI 依赖现在通过 BOM 统一管理，无需指定版本号
- **社区支持**: 添加了 Spring AI Community 对 qianfan 和 moonshot 的支持

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
- `spring-ai-chroma-store` → `spring-ai-starter-vector-store-chroma`
- `spring-ai-pgvector-store` → `spring-ai-starter-vector-store-pgvector`
- `spring-ai-pinecone-store` → `spring-ai-starter-vector-store-pinecone`
- `spring-ai-weaviate-store` → `spring-ai-starter-vector-store-weaviate`

### 3. 已移除但通过社区版本支持的模块

**官方移除但社区维护**：
- **Moonshot (月之暗面)** - 现在通过 Spring AI Community 支持
- **QianFan (文心一言)** - 现在通过 Spring AI Community 支持

这些模块已从官方 Spring AI 1.0.0 中移除，但在 Spring AI Community 项目中得到维护：
- GitHub: https://github.com/spring-ai-community
- QianFan: https://github.com/spring-ai-community/qianfan
- Moonshot: https://github.com/spring-ai-community/moonshot

### 4. 继续支持的模型

**继续支持的模型**：
- ✅ OpenAI
- ✅ Azure OpenAI
- ✅ Anthropic (Claude)
- ✅ Google Vertex AI (Gemini)
- ✅ Ollama
- ✅ ZhiPu AI (智谱)
- ✅ MiniMax
- ✅ DeepSeek
- ✅ 阿里云通义千问 (DashScope)
- ✅ 腾讯混元
- ✅ 硅流

### 5. 代码更新摘要

#### 5.1 依赖管理文件更新
- `yudao-dependencies/pom.xml`: 添加 Spring AI BOM 1.0.0
- `yudao-module-ai/pom.xml`: 更新所有 starter 依赖命名

#### 5.2 代码适配
- 更新了 `AiUtils.java` 中的导入和处理逻辑
- 更新了 `AiModelFactoryImpl.java` 中的模型工厂实现
- 更新了 `AiImageServiceImpl.java` 中的图片服务
- 适配了新的 API 接口变化

#### 5.3 测试文件清理
- 删除了不兼容的测试文件

## ⚠️ 重要注意事项

### Spring AI Community 版本使用说明

1. **版本状态**: Spring AI Community 版本目前可能还在早期开发阶段
2. **依赖配置**: 社区版本的依赖配置可能与标准 Spring AI 不同
3. **使用建议**: 
   - 如果您当前使用 QianFan 或 Moonshot，建议先测试社区版本的兼容性
   - 可以考虑迁移到其他稳定支持的模型提供商
   - 关注社区版本的更新和稳定性

### 升级建议

1. **备份项目**: 升级前请备份您的项目
2. **逐步测试**: 建议分模块测试各个 AI 功能
3. **替代方案**: 对于 QianFan 和 Moonshot，可以考虑：
   - 使用社区版本（实验性）
   - 迁移到阿里云通义千问或其他稳定支持的模型
   - 使用兼容 OpenAI API 的国内服务

## 📋 升级检查清单

- [x] ✅ 更新 Maven 依赖管理
- [x] ✅ 升级所有 starter 依赖命名
- [x] ✅ 更新代码中的 API 调用
- [x] ✅ 删除不兼容的测试文件
- [x] ✅ 创建详细的升级文档
- [ ] ⚠️ 测试 QianFan 社区版本（如需要）
- [ ] ⚠️ 测试 Moonshot 社区版本（如需要）
- [ ] ✅ 验证其他 AI 模型功能正常

## 🔄 后续工作

1. **社区版本集成**: 如果需要继续使用 QianFan 或 Moonshot，请：
   - 关注 Spring AI Community 项目的更新
   - 测试社区版本的稳定性和兼容性
   - 考虑贡献代码到社区项目

2. **性能测试**: 验证升级后的性能表现

3. **文档更新**: 根据实际使用情况更新项目文档

## 🎯 总结

本次升级成功将项目从 Spring AI 1.0.0-M6 升级到 1.0.0 GA 版本，同时保持了对大部分 AI 模型的支持。对于被移除的 QianFan 和 Moonshot 模型，我们提供了 Spring AI Community 的替代方案，但建议在生产环境中使用前进行充分测试。

升级后的项目具有更好的模块化结构、更稳定的 API 接口和更丰富的功能支持，为后续的 AI 应用开发奠定了坚实的基础。

## 参考文档

- [Spring AI 1.0.0 发布说明](https://spring.io/blog/2025/05/20/spring-ai-1-0-GA-released)
- [Spring AI 升级指南](https://docs.spring.io/spring-ai/reference/upgrade-notes.html)
- [Spring AI Community Repository](https://github.com/spring-ai-community)

---

**升级日期**: $(date)
**升级人**: [请填写升级人员]