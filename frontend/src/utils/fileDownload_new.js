// 通用文件下载工具类
export class FileDownloadManager {
  constructor(config = {}) {
    this.config = {
      apiBaseUrl: '/api/files',
      staticBaseUrl: '/output',
      maxRetries: 3,
      retryDelay: 1000,
      downloadDelay: 500,
      ...config,
    }
  }

  // 主要下载方法
  async downloadFiles(options = {}) {
    const {
      fileSource = 'auto', // 'api' | 'static' | 'auto'
      fileList = null,
      downloadUrls = null,
      onProgress = null,
      onError = null,
      onSuccess = null,
    } = options

    let filesToDownload = []
    let downloadUrls_final = []

    try {
      // 根据fileSource决定获取文件的方式
      switch (fileSource) {
        case 'api':
          ;({ filesToDownload, downloadUrls: downloadUrls_final } =
            await this.getFilesFromAPI())
          break
        case 'static':
          ;({ filesToDownload, downloadUrls: downloadUrls_final } =
            await this.getFilesFromStatic())
          break
        case 'auto':
        default:
          // 优先尝试API，失败则使用静态方式
          try {
            ;({ filesToDownload, downloadUrls: downloadUrls_final } =
              await this.getFilesFromAPI())
          } catch (e) {
            console.info('API方式失败，尝试静态方式:', e)
            ;({ filesToDownload, downloadUrls: downloadUrls_final } =
              await this.getFilesFromStatic())
          }
          break
      }

      // 如果传入了自定义文件列表，使用它们
      if (fileList && Array.isArray(fileList)) {
        filesToDownload = fileList
      }
      if (downloadUrls && Array.isArray(downloadUrls)) {
        downloadUrls_final = downloadUrls
      }

      if (filesToDownload.length === 0) {
        throw new Error('未发现可下载文件')
      }

      // 执行下载
      const results = await this.performDownloads(
        filesToDownload,
        downloadUrls_final,
        {
          onProgress,
          onError,
        }
      )

      if (onSuccess) {
        onSuccess(results)
      }

      return results
    } catch (error) {
      if (onError) {
        onError(error)
      }
      throw error
    }
  }

  // 从API获取文件列表
  async getFilesFromAPI() {
    const response = await fetch(`${this.config.apiBaseUrl}/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })

    if (!response.ok) {
      throw new Error(`API请求失败: ${response.statusText}`)
    }

    const data = await response.json()

    if (!data.success || !data.data || !Array.isArray(data.data.files)) {
      throw new Error('API返回数据格式错误')
    }

    const filesToDownload = data.data.files
    const downloadUrls =
      data.data.downloadUrls ||
      filesToDownload.map(
        (filename) =>
          `${this.config.apiBaseUrl}/download/${encodeURIComponent(filename)}`
      )

    return { filesToDownload, downloadUrls }
  }

  // 从静态资源获取文件列表
  async getFilesFromStatic() {
    let filesToDownload = []

    // 尝试从manifest.json获取
    try {
      const manifestUrl = `${
        this.config.staticBaseUrl
      }/manifest.json?ts=${Date.now()}`
      const response = await fetch(manifestUrl)
      if (response.ok) {
        const list = await response.json()
        if (Array.isArray(list)) {
          filesToDownload = list.filter(
            (f) => typeof f === 'string' && f.trim()
          )
        }
      }
    } catch (e) {
      console.info('manifest.json读取失败:', e)
    }

    // 尝试从全局变量获取
    if (
      filesToDownload.length === 0 &&
      Array.isArray(window.__OUTPUT_FILE_LIST)
    ) {
      filesToDownload = window.__OUTPUT_FILE_LIST.filter(
        (f) => typeof f === 'string' && f.trim()
      )
    }

    if (filesToDownload.length === 0) {
      throw new Error('未找到静态文件列表')
    }

    const downloadUrls = filesToDownload.map(
      (filename) =>
        `${this.config.staticBaseUrl}/${encodeURIComponent(filename)}`
    )

    return { filesToDownload, downloadUrls }
  }

  // 执行实际下载
  async performDownloads(fileNames, urls, { onProgress, onError }) {
    const results = {
      success: 0,
      failed: 0,
      errors: [],
    }

    for (let i = 0; i < fileNames.length; i++) {
      const filename = fileNames[i]
      const url = urls[i]

      try {
        await this.downloadSingleFile(filename, url)
        results.success++

        if (onProgress) {
          onProgress({
            current: i + 1,
            total: fileNames.length,
            filename,
            status: 'success',
          })
        }
      } catch (error) {
        results.failed++
        results.errors.push({ filename, error: error.message })

        if (onError) {
          onError({ filename, error })
        }

        if (onProgress) {
          onProgress({
            current: i + 1,
            total: fileNames.length,
            filename,
            status: 'failed',
            error: error.message,
          })
        }
      }

      // 添加延迟避免浏览器限制
      if (i < fileNames.length - 1) {
        await new Promise((resolve) =>
          setTimeout(resolve, this.config.downloadDelay)
        )
      }
    }

    return results
  }

  // 下载单个文件
  async downloadSingleFile(filename, url) {
    let lastError = null

    for (let attempt = 1; attempt <= this.config.maxRetries; attempt++) {
      try {
        if (url.includes('/api/')) {
          // API下载方式
          await this.downloadFromAPI(filename, url)
        } else {
          // 直接下载方式
          await this.downloadDirectly(filename, url)
        }
        return // 成功则返回
      } catch (error) {
        lastError = error
        console.warn(
          `下载尝试 ${attempt}/${this.config.maxRetries} 失败:`,
          error
        )

        if (attempt < this.config.maxRetries) {
          await new Promise((resolve) =>
            setTimeout(resolve, this.config.retryDelay)
          )
        }
      }
    }

    throw lastError
  }

  // API方式下载
  async downloadFromAPI(filename, apiUrl) {
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })

    if (!response.ok) {
      throw new Error(`API下载失败: ${response.statusText}`)
    }

    const blob = await response.blob()
    this.createDownloadLink(filename, window.URL.createObjectURL(blob), true)
  }

  // 直接下载方式
  async downloadDirectly(filename, url) {
    // 先检查文件是否存在
    const checkRes = await fetch(url, { method: 'HEAD' })
    if (!checkRes.ok) {
      throw new Error(`文件不存在: ${filename}`)
    }

    this.createDownloadLink(filename, url, false)
  }

  // 创建下载链接
  createDownloadLink(filename, url, needRevoke = false) {
    const a = document.createElement('a')
    a.href = url
    a.download = filename
    a.style.display = 'none'
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)

    if (needRevoke) {
      // 延迟清理临时URL，确保下载开始
      setTimeout(() => window.URL.revokeObjectURL(url), 1000)
    }
  }
}

// 导出单例实例
export const fileDownloadManager = new FileDownloadManager()

// 简化的使用方法
export const downloadFiles = async (options = {}) => {
  return await fileDownloadManager.downloadFiles(options)
}
