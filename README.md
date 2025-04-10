# W2P-Converter

## 简介
W2P-Converter 是一个独立的服务器，用于将 Word 文件转换为 PDF 文件。该项目基于 Spring Boot 构建，支持文件上传和 Base64 编码文件的转换，并将转换后的 PDF 文件上传到阿里云 OSS 服务器。

## 功能特性
- **文件类型和大小校验**：支持对上传的文件进行类型和大小校验，确保上传的文件符合要求。
- **Word 转 PDF**：使用 Jacob 库将 Word 文件转换为 PDF 文件。
- **文件上传到 OSS**：将转换后的 PDF 文件上传到阿里云 OSS 服务器。
- **临时文件清理**：在应用启动和关闭时，自动清理临时文件。
- **服务检测**：提供服务检测接口，用于检查服务是否正常运行。

## 项目结构
```
W2P-Converter/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── mpanda/
│   │   │           └── word2pdf/
│   │   │               ├── common/         # 通用工具类和配置类
│   │   │               ├── config/         # 项目配置类
│   │   │               ├── controller/     # 控制器类
│   │   │               ├── dto/            # 数据传输对象类
│   │   │               ├── enums/          # 枚举类
│   │   │               ├── exception/      # 异常处理类
│   │   │               ├── service/        # 服务类
│   │   │               └── utils/          # 工具类
│   │   └── resources/
│   │       ├── application.yml             # 项目配置文件
│   │       └── lib/                        # 依赖库文件
│   └── test/
│       └── java/
│           └── com/
│               └── mpanda/
│                   └── word2pdf/
│                       └── ...             # 测试类
├── .gitignore              # Git 忽略文件配置
├── mvnw                    # Maven 包装器脚本
├── mvnw.cmd                # Maven 包装器脚本（Windows）
├── pom.xml                 # Maven 项目配置文件
└── README.md               # 项目说明文档
```

## 环境要求
- Java 8 或更高版本
- Maven
- 阿里云 OSS 账号

## 配置说明
在 `src/main/resources/application.yml` 文件中进行配置：
```yaml
server:
  port: 8181
  servlet:
    context-path: /

spring:
  devtools:
    restart:
      enabled: false
  servlet:
    multipart:
      max-file-size: 500MB
      max-request-size: 500MB
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
file:
  check:
    maxSize: 10
    types:
      doc: D0CF11E0
      docx: 504B030414
logging:
  level:
    web: debug
```
- `server.port`：服务器端口号
- `spring.servlet.multipart.max-file-size` 和 `spring.servlet.multipart.max-request-size`：上传文件的最大大小
- `file.check.maxSize`：允许上传文件的最大大小（MB）
- `file.check.types`：允许上传的文件类型及其文件头信息

## 快速开始
1. **克隆项目**
```bash
git clone https://github.com/your-repo/W2P-Converter.git
cd W2P-Converter
```
2. **配置阿里云 OSS 信息**
在 `SSOBaseInfoDto` 类中配置阿里云 OSS 的 `endPoint`、`accessKeyId`、`accessKeySecret`、`bucketName` 和 `filedir`。

3. **构建项目**
```bash
./mvnw clean package
```

4. **运行项目**
```bash
java -jar target/Word2Pdf-0.0.1.jar
```

## API 文档
### 1. Word 转 PDF（文件上传方式）
- **URL**：`/word2pdf/upload`
- **方法**：`POST`
- **请求参数**：
  - `file`：上传的 Word 文件
  - `target`：`UploadFileInfoDto` 对象，包含阿里云 OSS 信息
- **响应**：`Response<UploadFileInfoRes>` 对象，包含转换后的 PDF 文件信息

### 2. Word 转 PDF（Base64 编码方式）
- **URL**：`/word2pdf/base64`
- **方法**：`POST`
- **请求参数**：`UploadFileInfoBase64Dto` 对象，包含 Base64 编码的 Word 文件和文件名，以及阿里云 OSS 信息
- **响应**：`Response<UploadFileInfoRes>` 对象，包含转换后的 PDF 文件信息

### 3. 服务检测
- **URL**：`/word2pdf/check`
- **方法**：`GET`
- **响应**：`Response<String>` 对象，返回 `"OK"` 表示服务正常运行

## 注意事项
- 该项目依赖 Jacob 库，需要在 `pom.xml` 中配置 `jacob.jar` 的路径。
- 确保服务器上安装了 Microsoft Word 软件，因为 Jacob 库需要调用 Word 进行文件转换。
- 在使用前，请确保已经正确配置了阿里云 OSS 的相关信息。

## 贡献
如果你有任何建议或发现了问题，请提交 Issue 或 Pull Request。

## 许可证
本项目采用 [MIT 许可证](https://opensource.org/licenses/MIT)。
