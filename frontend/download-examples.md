# 动态文件下载使用示例

## 当前实现的功能

您的组件现在支持以下下载方式：

### 1. 自动模式（推荐）

```javascript
// 自动选择最佳下载方式
const exportPlan = async () => {
  await downloadFiles({
    fileSource: 'auto', // 优先API，失败则用静态方式
    onProgress: (progress) => {
      console.log(`${progress.current}/${progress.total}: ${progress.filename}`)
    },
    onSuccess: (results) => {
      alert(`下载完成！成功: ${results.success}个`)
    },
  })
}
```

### 2. API 模式

```javascript
// 从后端API获取文件列表并下载
await downloadFiles({
  fileSource: 'api',
  // 会调用 GET /api/files/list 获取文件列表
  // 然后通过 GET /api/files/download/:filename 下载文件
})
```

### 3. 静态模式

```javascript
// 从静态资源下载
await downloadFiles({
  fileSource: 'static',
  // 会从 /output/manifest.json 或 window.__OUTPUT_FILE_LIST 获取文件列表
})
```

### 4. 自定义文件列表

```javascript
// 下载指定的文件列表
await downloadFiles({
  fileList: ['文件1.xlsx', '文件2.pdf'],
  downloadUrls: ['/api/files/download/文件1.xlsx', '/output/文件2.pdf'],
})
```

## 后端 API 实现示例

### Node.js/Express 示例

```javascript
// 获取文件列表
app.get('/api/files/list', (req, res) => {
  const fs = require('fs')
  const path = require('path')

  const outputDir = path.join(__dirname, 'public/output')

  try {
    const files = fs
      .readdirSync(outputDir)
      .filter((file) => !file.startsWith('.'))

    const downloadUrls = files.map(
      (file) => `/api/files/download/${encodeURIComponent(file)}`
    )

    res.json({
      success: true,
      data: {
        files,
        downloadUrls,
      },
    })
  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message,
    })
  }
})

// 下载文件
app.get('/api/files/download/:filename', (req, res) => {
  const filename = decodeURIComponent(req.params.filename)
  const filePath = path.join(__dirname, 'public/output', filename)

  // 安全检查
  if (!fs.existsSync(filePath)) {
    return res.status(404).json({ error: '文件不存在' })
  }

  // 设置响应头
  res.setHeader(
    'Content-Disposition',
    `attachment; filename="${encodeURIComponent(filename)}"`
  )
  res.setHeader('Content-Type', 'application/octet-stream')

  // 发送文件
  res.sendFile(filePath)
})
```

### Spring Boot 示例

```java
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Value("${app.output.directory}")
    private String outputDirectory;

    @GetMapping("/list")
    public ResponseEntity<?> getFileList() {
        try {
            File dir = new File(outputDirectory);
            String[] files = dir.list((dir1, name) -> !name.startsWith("."));

            List<String> downloadUrls = Arrays.stream(files)
                .map(file -> "/api/files/download/" + URLEncoder.encode(file, "UTF-8"))
                .collect(Collectors.toList());

            Map<String, Object> response = Map.of(
                "success", true,
                "data", Map.of(
                    "files", Arrays.asList(files),
                    "downloadUrls", downloadUrls
                )
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(outputDirectory, filename);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
```

## WebSocket 实时推送示例

```javascript
// 前端监听（已集成到组件中）
useEffect(() => {
  const ws = new WebSocket('ws://localhost:8080/ws/files')

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data)
    if (data.type === 'fileGenerated') {
      // 有新文件生成，更新文件列表
      window.__OUTPUT_FILE_LIST = data.files
      // 可以显示提示："检测到新文件，点击下载"
    }
  }

  return () => ws.close()
}, [])
```

```javascript
// 后端推送（Node.js）
const WebSocket = require('ws')
const wss = new WebSocket.Server({ port: 8080, path: '/ws/files' })

// 当有新文件生成时
function notifyNewFiles(files) {
  const message = JSON.stringify({
    type: 'fileGenerated',
    files: files,
    timestamp: Date.now(),
  })

  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(message)
    }
  })
}
```

## 文件生成时机示例

假设在"一键优化"功能中生成文件：

```javascript
const OPTclick = async () => {
  console.log('一键优化')

  try {
    // 调用后端优化算法
    const response = await fetch('/api/optimize', {
      method: 'POST',
      body: JSON.stringify(trafficState),
    })

    if (response.ok) {
      const result = await response.json()

      // 更新状态
      dispatch(mergeTrafficState(result.optimizedState))

      // 如果后端生成了新文件，更新文件列表
      if (result.generatedFiles) {
        window.__OUTPUT_FILE_LIST = result.generatedFiles
        // 显示提示
        alert('优化完成！生成了新的报告文件，可点击"方案导出"下载。')
      }
    }
  } catch (error) {
    console.error('优化失败:', error)
  }
}
```
