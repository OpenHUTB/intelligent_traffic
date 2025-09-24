import React, { useEffect, useRef, useState, useCallback } from 'react'
import * as echarts from 'echarts'
import styles from './index.module.scss'
import { useSelector, useDispatch } from 'react-redux'
import {
  setTrafficState,
  mergeTrafficState,
} from 'stores/storesNewUI/junctionTimeprogressSlice'
import optIcon from 'assets/image/opt-icon.png'
import manualIcon from 'assets/image/Frame.png'
import download from 'assets/image/download.png'
import { Modal, Checkbox, Spin, message, Button } from 'antd'
import axios from 'axios'

export default function JunctionTimeprogress() {
  const chartRef = useRef(null)
  const chartInstance = useRef(null)
  const { directionMap, movementMap, trafficState } = useSelector(
    (state) => state.junctionTimeprogress
  )
  const dispatch = useDispatch()
  const OPTclick = () => {
    console.log('一键优化')
    // 示例：这里可以调用后端算法；当前只是再次写入（触发重绘）
    dispatch(mergeTrafficState(trafficState))
  }

  // ================= 新下载逻辑（基于后端接口） =================
  const [fileModalOpen, setFileModalOpen] = useState(false)
  const [fileLoading, setFileLoading] = useState(false)
  const [downloading, setDownloading] = useState(false)
  const [fileList, setFileList] = useState([]) // {documentId, documentName, url, type}
  const [selectedIds, setSelectedIds] = useState(new Set())

  const API_BASE = 'http://localhost:8080/simulation/file'

  // 获取文件列表
  const fetchFileList = useCallback(async () => {
    setFileLoading(true)
    try {
      const res = await axios.get(`${API_BASE}/documentList`, {
        params: { type: 'signal' },
      })
      if (res.data?.status === 'SUCCESS' && Array.isArray(res.data.data)) {
        const list = res.data.data
        setFileList(list)
        setSelectedIds(new Set(list.map((i) => i.documentId))) // 默认全选
      } else {
        message.error(res.data?.message || '获取文件列表失败')
      }
    } catch (e) {
      console.error(e)
      message.error('文件列表请求错误')
    } finally {
      setFileLoading(false)
    }
  }, [])

  // 点击“方案导出”按钮：拉取列表并打开弹窗
  const exportPlan = async () => {
    setFileModalOpen(true)
    if (fileList.length === 0) {
      fetchFileList()
    }
  }

  const toggleSelect = (id) => {
    setSelectedIds((prev) => {
      const next = new Set(prev)
      if (next.has(id)) next.delete(id)
      else next.add(id)
      return next
    })
  }

  const selectAll = () => {
    setSelectedIds(new Set(fileList.map((i) => i.documentId)))
  }

  const clearAll = () => {
    setSelectedIds(new Set())
  }

  // 实际下载单个文件
  const downloadOne = async (doc) => {
    try {
      const response = await axios.get(`${API_BASE}/download`, {
        params: { documentId: doc.documentId },
        responseType: 'blob',
      })

      // 如果后端返回JSON错误，而不是文件
      if (response.data.type === 'application/json') {
        const text = await response.data.text()
        try {
          const json = JSON.parse(text)
          throw new Error(json.message || '下载失败')
        } catch (e) {
          throw new Error('下载失败')
        }
      }

      // 从header获取文件名
      let filename = doc.documentName || '文件'
      const disposition = response.headers['content-disposition']
      if (disposition) {
        const match = /filename\*=UTF-8''([^;]+)|filename="?([^";]+)"?/i.exec(
          disposition
        )
        if (match) {
          filename = decodeURIComponent(match[1] || match[2] || filename)
        }
      } else if (doc.url) {
        // fallback: 使用原始url里的扩展名
        const ext = doc.url.split('.').pop()
        if (ext && !filename.endsWith(`.${ext}`)) filename += `.${ext}`
      } else if (!/\.\w+$/.test(filename)) {
        filename += '.xls'
      }

      const blob = response.data
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = filename
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
      setTimeout(() => window.URL.revokeObjectURL(url), 1500)
      return { ok: true, id: doc.documentId }
    } catch (e) {
      return { ok: false, id: doc.documentId, error: e.message }
    }
  }

  const handleDownload = async () => {
    if (selectedIds.size === 0) {
      message.warning('请选择至少一个文件')
      return
    }
    setDownloading(true)
    const targets = fileList.filter((f) => selectedIds.has(f.documentId))
    const results = []
    for (let i = 0; i < targets.length; i++) {
      const r = await downloadOne(targets[i])
      results.push(r)
    }
    setDownloading(false)
    const success = results.filter((r) => r.ok).length
    const failed = results.length - success
    if (failed === 0) {
      message.success(`全部下载完成（${success}个）`)
    } else {
      message.warning(`成功 ${success} 个，失败 ${failed} 个`)
      console.warn(
        '下载失败明细:',
        results.filter((r) => !r.ok)
      )
    }
  }
  // ========================================================

  console.log('trafficState from slice:', trafficState)

  // directionMap & movementMap 现在来自 Redux，可在运行时调整

  // 转换数据为echarts格式
  const prepareChartData = () => {
    const categories = []
    const redData = []
    const yellowData = []
    const greenData = []

    Object.entries(trafficState).forEach(([direction, movements]) => {
      Object.entries(movements).forEach(([movement, light]) => {
        const { redDurationTime, greenDurationTime } = light
        const yellowDuration = 3

        const directionName = directionMap[direction] || direction
        const movementName = movementMap[movement] || movement
        const categoryName = `${directionName}${movementName}`

        categories.push(categoryName)
        redData.push(redDurationTime)
        yellowData.push(yellowDuration)
        greenData.push(greenDurationTime)
      })
    })

    return { categories, redData, yellowData, greenData }
  }

  // WebSocket监听文件更新的useEffect
  useEffect(() => {
    let ws = null

    const connectWebSocket = () => {
      try {
        ws = new WebSocket('ws://localhost:8080/ws/files')

        ws.onmessage = (event) => {
          try {
            const data = JSON.parse(event.data)
            if (data.type === 'fileGenerated' && data.files) {
              console.log('收到新文件推送:', data.files)
              // 更新全局文件列表
              window.__OUTPUT_FILE_LIST = data.files
              // 可以在这里触发UI更新或显示提示
            }
          } catch (e) {
            console.warn('WebSocket消息解析失败:', e)
          }
        }

        ws.onerror = (error) => {
          console.warn('WebSocket连接失败:', error)
        }

        ws.onclose = () => {
          console.info('WebSocket连接已关闭')
        }
      } catch (e) {
        console.info('WebSocket不可用，使用轮询方式')
      }
    }

    // 尝试连接WebSocket
    connectWebSocket()

    return () => {
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close()
      }
    }
  }, [])

  // 事件监听和全局函数注册的useEffect
  useEffect(() => {
    const handleJunctionTimeprogressChanged = (event) => {
      const payload = event.detail
      console.log('junctionTimeprogressChanged event:', payload)
      dispatch(setTrafficState(payload))
    }
    window.addEventListener(
      'junctionTimeprogressChanged',
      handleJunctionTimeprogressChanged
    )

    // 暴露全局函数，允许直接传对象（无需包装 CustomEvent）
    window.junctionTimeprogressChanged = (payload, { merge = false } = {}) => {
      if (merge) {
        dispatch(mergeTrafficState(payload))
      } else {
        dispatch(setTrafficState(payload))
      }
    }

    return () => {
      window.removeEventListener(
        'junctionTimeprogressChanged',
        handleJunctionTimeprogressChanged
      )
    }
  }, [dispatch])

  useEffect(() => {
    if (!chartRef.current) return

    // 初始化echarts实例
    if (!chartInstance.current) {
      chartInstance.current = echarts.init(chartRef.current)
    }

    const { categories, redData, yellowData, greenData } = prepareChartData()

    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
        formatter: function (params) {
          const categoryName = params[0].name
          let result = `${categoryName}<br/>`
          params.forEach((param) => {
            const color = param.color
            const seriesName = param.seriesName
            const value = param.value
            result += `<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color};"></span>${seriesName}: ${value}秒<br/>`
          })
          return result
        },
      },
      title: {
        text: '时间进度',
        left: '2%',
        textStyle: {
          color: '#ffffff',
          fontSize: 16,
          fontWeight: 'bold',
        },
      },
      grid: {
        left: '15%',
        right: '5%',
        top: '10%',
        bottom: '5%',
        containLabel: false,
      },
      xAxis: {
        type: 'value',
        show: true,
        splitLine: {
          show: true,
          lineStyle: {
            color: '#ffffff50',
            type: 'dashed',
          },
        },
        axisLine: {
          show: false,
          lineStyle: {
            color: '#ffffff',
          },
        },
        axisLabel: {
          show: false,
          color: '#ffffff',
          fontSize: 12,
        },
      },
      yAxis: {
        type: 'category',
        data: categories,
        axisLabel: {
          color: '#ffffff',
          fontSize: 12,
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#ffffff',
          },
        },
        axisTick: {
          show: false,
        },
      },
      series: [
        {
          name: '红灯',
          type: 'bar',
          stack: 'total',
          data: redData,
          itemStyle: {
            color: '#FF483E',
          },
          label: {
            show: false,
            position: 'inside',
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'bold',
          },
          barHeight: 10,
          barWidth: '10px',
        },
        {
          name: '黄灯',
          type: 'bar',
          stack: 'total',
          data: yellowData,
          itemStyle: {
            color: '#FFCA45',
          },
          label: {
            show: false,
            position: 'inside',
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'bold',
          },
          barHeight: 10,
        },
        {
          name: '绿灯',
          type: 'bar',
          stack: 'total',
          data: greenData,
          itemStyle: {
            color: '#0EFFB0',
          },
          label: {
            show: false,
            position: 'inside',
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'bold',
          },
          barHeight: 10,
        },
      ],
    }

    chartInstance.current.setOption(option)

    // 处理窗口大小变化
    const handleResize = () => {
      if (chartInstance.current) {
        chartInstance.current.resize()
      }
    }

    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [trafficState, directionMap, movementMap])

  // 组件卸载时销毁echarts实例
  useEffect(() => {
    return () => {
      if (chartInstance.current) {
        chartInstance.current.dispose()
        chartInstance.current = null
      }
    }
  }, [])

  return (
    <div className={styles.junctionTimeprogress}>
      <div className={styles.title}>
        <span>信控模式管理</span>
        <div className={styles.download} onClick={exportPlan}>
          <img src={download} alt='' />
          <span className={styles.text}>方案导出</span>
        </div>
      </div>
      <main className={styles.progressMain}>
        <div className={styles.modeContainer}>
          <div className={styles.opt} onClick={OPTclick}>
            <img src={optIcon} alt='' />
            <span>一键优化</span>
          </div>
          <div className={styles.manual}>
            <img src={manualIcon} alt='' />
            <span>手动控制</span>
          </div>
        </div>
        <div
          ref={chartRef}
          className={styles.chartContainer}
          style={{ width: '100%', height: '300px' }}
        />
      </main>

      <Modal
        title='选择要导出的文件'
        open={fileModalOpen}
        onCancel={() => setFileModalOpen(false)}
        footer={[
          <Button key='refresh' onClick={fetchFileList} disabled={fileLoading}>
            刷新
          </Button>,
          <Button key='all' onClick={selectAll} disabled={fileLoading}>
            全选
          </Button>,
          <Button key='clear' onClick={clearAll} disabled={fileLoading}>
            清空
          </Button>,
          <Button
            key='download'
            type='primary'
            loading={downloading}
            onClick={handleDownload}
          >
            {downloading ? '下载中...' : '下载'}
          </Button>,
        ]}
        width={520}
      >
        {fileLoading ? (
          <div style={{ textAlign: 'center', padding: 40 }}>
            <Spin />
          </div>
        ) : fileList.length === 0 ? (
          <div style={{ padding: '12px 8px' }}>暂无可下载文件</div>
        ) : (
          <div
            style={{
              maxHeight: 300,
              overflowY: 'auto',
              border: '1px solid #333',
              borderRadius: 4,
              padding: 8,
            }}
          >
            {fileList.map((f) => (
              <div
                key={f.documentId}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  padding: '4px 4px',
                  borderBottom: '1px solid #222',
                }}
              >
                <Checkbox
                  checked={selectedIds.has(f.documentId)}
                  onChange={() => toggleSelect(f.documentId)}
                  style={{ flex: 1 }}
                >
                  {f.documentName}
                </Checkbox>
              </div>
            ))}
          </div>
        )}
        <div style={{ marginTop: 8, fontSize: 12, opacity: 0.7 }}>
          说明：选择需要下载的信控方案,请至少选择一个方案.
        </div>
      </Modal>
    </div>
  )
}
