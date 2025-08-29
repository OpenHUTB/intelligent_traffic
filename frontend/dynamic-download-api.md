# 动态文件下载 API 接口说明

## 1. 获取文件列表 API

### 接口地址

```
GET /api/files/list
```

### 响应格式

```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "files": [
      "各路口信控优化对比.xls",
      "交通流量报告_20250829.pdf",
      "信号灯配时方案.xlsx"
    ],
    "downloadUrls": [
      "/api/files/download/各路口信控优化对比.xls",
      "/api/files/download/交通流量报告_20250829.pdf",
      "/api/files/download/信号灯配时方案.xlsx"
    ],
    "downloadBaseUrl": "/api/files/download/"
  }
}
```

## 2. 下载文件 API

### 接口地址

```
GET /api/files/download/:filename
```

### 请求示例

```
GET /api/files/download/各路口信控优化对比.xls
```

### 响应

- 成功：返回文件流（application/octet-stream）
- 失败：返回错误 JSON

## 3. 前端使用示例

### 方式 1：动态获取文件列表

```javascript
// 在组件中调用
const exportPlan = async () => {
  try {
    // 获取文件列表
    const response = await fetch('/api/files/list')
    const data = await response.json()

    if (data.success && data.data.files) {
      // 下载所有文件
      await downloadFiles(data.data.files, data.data.downloadUrls)
    }
  } catch (error) {
    console.error('获取文件列表失败:', error)
  }
}
```

### 方式 2：直接下载指定文件

```javascript
const downloadSpecificFile = async (filename) => {
  try {
    const response = await fetch(
      `/api/files/download/${encodeURIComponent(filename)}`
    )
    if (response.ok) {
      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = filename
      a.click()
      window.URL.revokeObjectURL(url)
    }
  } catch (error) {
    console.error('下载失败:', error)
  }
}
```

## 4. WebSocket 实时推送文件

### 前端监听

```javascript
const ws = new WebSocket('ws://localhost:8080/ws/files')

ws.onmessage = (event) => {
  const data = JSON.parse(event.data)
  if (data.type === 'fileGenerated') {
    // 有新文件生成，更新文件列表
    window.__OUTPUT_FILE_LIST = data.files
    // 或者重新调用API获取最新列表
  }
}
```
