import React, { useState, useRef, useEffect } from 'react'

import styles from './index.module.scss'
import { useSelector, useDispatch } from 'react-redux'
import { setAiMessage } from 'stores/junctionLight/aiMessageSlice'
import OpenAI from 'openai'

const openai = new OpenAI({
  apiKey: process.env.REACT_APP_OPENAI_API_KEY,
  baseURL: 'https://dashscope.aliyuncs.com/compatible-mode/v1',
  dangerouslyAllowBrowser: true,
})

export default function GPT() {
  const dispatch = useDispatch()
  const [messages, setMessages] = useState([
    {
      text: '您好！我是交通策略问答模块，搭载湘江实验室和湖南工商大学在本地国产异构算力平台部署的DeepSeek-R1大模型。我能通过自然对话实时解析拥堵成因，生成信号灯配时优化等策略方案，并联动Carla模拟推演实施效果，为城市交通优化提供智能决策支持。现在开始提问吧！',
      isUser: false,
      id: 1687930800000,
    },
  ])
  const [inputText, setInputText] = useState('')
  const [isReceivingStream, setIsReceivingStream] = useState(false)
  const [detailedResponse, setDetailedResponse] = useState('') // 新增：存储详细回复
  const [isDetailedResponseStreaming, setIsDetailedResponseStreaming] =
    useState(false) // 新增：详细回复流状态
  const messagesEndRef = useRef(null)
  const detailedResponseRef = useRef(null) // 新增：详细回复容器引用
  const abortControllerRef = useRef(null)

  // 新增自动滚动效果
  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollTo({
        top: messagesEndRef.current.scrollHeight,
        behavior: 'smooth',
      })
    }
  }

  // 新增：详细回复自动滚动
  const scrollDetailedToBottom = () => {
    if (detailedResponseRef.current) {
      detailedResponseRef.current.scrollTo({
        top: detailedResponseRef.current.scrollHeight,
        behavior: 'smooth',
      })
    }
  }

  // 当messages变化时触发滚动
  useEffect(() => {
    scrollToBottom()
  }, [messages])

  // 当详细回复变化时触发滚动
  useEffect(() => {
    scrollDetailedToBottom()
  }, [detailedResponse])

  // 清理AbortController
  useEffect(() => {
    return () => {
      if (abortControllerRef.current) {
        abortControllerRef.current.abort()
      }
    }
  }, [])

  useEffect(() => {
    const updateMessage = (event) => {
      dispatch(setAiMessage(event.detail))
      console.log('AI回复:', event.detail)
      setMessages((prev) => [
        ...prev,
        { text: event.detail, isUser: false, id: Date.now() + 1 },
      ])
    }

    window.addEventListener('aiResponseUpdate', updateMessage)

    return () => {
      window.removeEventListener('aiResponseUpdate', updateMessage)
    }
  }, [dispatch])

  const handleSend = async () => {
    if (inputText.trim()) {
      // 检查API密钥是否配置
      if (!process.env.REACT_APP_OPENAI_API_KEY) {
        console.error('API密钥未配置')
        setMessages((prev) => [
          ...prev,
          { text: inputText, isUser: true, id: Date.now() },
          {
            text: '错误：API密钥未配置，请检查环境变量设置。',
            isUser: false,
            id: Date.now() + 1,
          },
        ])
        setInputText('')
        return
      }

      // 取消先前的请求如果存在
      if (abortControllerRef.current) {
        abortControllerRef.current.abort()
      }

      // 创建新的AbortController
      abortControllerRef.current = new AbortController()

      // 添加用户消息
      window.userMessage = inputText
      const newAiId = `ai-${Date.now()}` // 生成唯一ID

      // 清空详细回复并设置流状态
      setDetailedResponse('')
      setIsDetailedResponseStreaming(true)

      // 先添加用户消息和简单的AI回复
      setMessages((prev) => [
        ...prev,
        { text: inputText, isUser: true, id: Date.now() },
        { text: '正在思考中...', isUser: false, id: newAiId, streaming: true },
      ])

      const currentInput = inputText
      setInputText('')
      setIsReceivingStream(true)

      try {
        // 使用OpenAI流式API
        const stream = await openai.chat.completions.create(
          {
            model: 'qwen-plus',
            messages: [
              {
                role: 'system',
                content: `你是一个湘江实验室开发的智慧交通策略问答助手，专门帮助用户解决交通优化相关问题。如果提问你是谁，一定要说你是湘江实验室开发的智慧交通策略问答助手。请提供详细、专业的回答。同时还有一组当天的流量数据，如果让你预测流量，就根据这个流量数据预测下一日的24小时流量数据,在回复时，不要列出当前数据，只需列出预测结果和调控建议。数据如下：[
  { "0:00": 506 },
  { "1:00": 281 },
  { "2:00": 176 },
  { "3:00": 131 },
  { "4:00": 76 },
  { "5:00": 142 },
  { "6:00": 471 },
  { "7:00": 875 },
  { "8:00": 1608 },
  { "9:00": 1508 },
  { "10:00": 1330 },
  { "11:00": 1458 },
  { "12:00": 1535 },
  { "13:00": 1458 },
  { "14:00": 1248 },
  { "15:00": 1313 },
  { "16:00": 1229 },
  { "17:00": 1993 },
  { "18:00": 2331 },
  { "19:00": 1666 },
  { "20:00": 1374 },
  { "21:00": 1150 },
  { "22:00": 922 },
  { "23:00": 582 }
]`,
              },
              {
                role: 'user',
                content: currentInput,
              },
            ],
            stream: true,
          },
          {
            signal: abortControllerRef.current.signal,
          }
        )

        console.log('开始接收流式数据')

        let fullResponse = ''
        let firstChunkReceived = false

        // 处理流式响应
        for await (const chunk of stream) {
          const content = chunk.choices[0]?.delta?.content || ''

          if (content) {
            console.log('AI回复片段:', content)
            fullResponse += content

            // 更新详细回复（右侧显示）
            setDetailedResponse(fullResponse)

            // 第一次接收到内容时，更新左侧为简单回复
            if (!firstChunkReceived) {
              firstChunkReceived = true
              setMessages((prev) => {
                const newMessages = [...prev]
                const lastMessageIndex = newMessages.length - 1

                if (
                  lastMessageIndex >= 0 &&
                  newMessages[lastMessageIndex].id === newAiId
                ) {
                  newMessages[lastMessageIndex] = {
                    ...newMessages[lastMessageIndex],
                    text: '已为您生成详细回复，请查看右侧完整内容。',
                    streaming: false,
                  }
                }

                return newMessages
              })
            }

            // 强制滚动到底部
            setTimeout(scrollDetailedToBottom, 0)
          }
        }

        // 流式响应完成
        setIsReceivingStream(false)
        setIsDetailedResponseStreaming(false)
      } catch (error) {
        console.error('API请求错误:', error)

        // 处理错误
        setIsReceivingStream(false)
        setIsDetailedResponseStreaming(false)

        const errorMessage =
          error.name === 'AbortError'
            ? '请求已取消'
            : '抱歉，发生了错误，请稍后重试。'

        setMessages((prev) => {
          const newMessages = [...prev]
          const lastMessageIndex = newMessages.length - 1

          if (
            lastMessageIndex >= 0 &&
            newMessages[lastMessageIndex].id === newAiId
          ) {
            newMessages[lastMessageIndex] = {
              ...newMessages[lastMessageIndex],
              text: errorMessage,
              streaming: false,
            }
          }

          return newMessages
        })

        setDetailedResponse(errorMessage)
      }
    }
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div className={styles.junctionLight}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        {/* 左侧：简单问答 */}
        <div className={styles.userConversationContainer}>
          <div className={styles.title}>
            <span>智慧交策问答</span>
          </div>
          <div className={styles.userinputContainer}>
            <div className={styles.chatContainer}>
              <div className={styles.messagesContainer} ref={messagesEndRef}>
                {messages.map((message) => (
                  <div
                    key={message.id}
                    className={`${styles.message} ${
                      message.isUser ? styles.userMessage : styles.aiMessage
                    }`}
                  >
                    <div className={styles.content}>
                      {message.text}
                      {message.streaming && (
                        <span className={styles.typingIndicator}>▋</span>
                      )}
                    </div>
                  </div>
                ))}
              </div>

              <div className={styles.inputContainer}>
                <input
                  type='text'
                  value={inputText}
                  onChange={(e) => setInputText(e.target.value)}
                  onKeyPress={handleKeyPress}
                  placeholder='输入消息...'
                  className={styles.inputField}
                  disabled={isReceivingStream}
                />
                <button
                  onClick={handleSend}
                  className={styles.sendButton}
                  disabled={isReceivingStream || !inputText.trim()}
                >
                  发送
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* 右侧：详细AI回复 */}
        <div className={styles.gptResponseContainer}>
          <div className={styles.title}>
            <span>详细回复</span>
          </div>
          <div
            className={styles.searchResultsContainer}
            ref={detailedResponseRef}
          >
            {detailedResponse ? (
              <div className={styles.detailedResponseContent}>
                {detailedResponse}
                {isDetailedResponseStreaming && (
                  <span className={styles.typingIndicator}>▋</span>
                )}
              </div>
            ) : (
              <div className={styles.noSearchPlaceholder}>
                <p>请在左侧输入问题，这里将显示详细的AI回复内容</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
