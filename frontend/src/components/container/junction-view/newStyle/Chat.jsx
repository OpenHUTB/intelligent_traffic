import React, { useState, useRef, useEffect } from 'react'
import styles from './css/chatComponent.module.scss'
import { useSelector, useDispatch } from 'react-redux'
import { setAiMessage } from 'stores/junctionLight/aiMessageSlice'

const ChatComponent = () => {
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
  const messagesEndRef = useRef(null)
  const eventSourceRef = useRef(null)

  // 新增自动滚动效果
  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollTo({
        top: messagesEndRef.current.scrollHeight,
        behavior: 'smooth',
      })
    }
  }

  // 当messages变化时触发滚动
  useEffect(() => {
    scrollToBottom()
  }, [messages])

  // 清理EventSource
  useEffect(() => {
    return () => {
      if (eventSourceRef.current) {
        eventSourceRef.current.close()
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

  const handleSend = () => {
    if (inputText.trim()) {
      // 关闭先前的EventSource如果存在
      if (eventSourceRef.current) {
        eventSourceRef.current.close()
      }

      // 添加用户消息
      window.userMessage = inputText
      const newAiId = `ai-${Date.now()}` // 生成唯一ID

      // 先添加用户消息和空的AI消息
      setMessages((prev) => [
        ...prev,
        { text: inputText, isUser: true, id: Date.now() },
        { text: '', isUser: false, id: newAiId, streaming: true }, // 标记为streaming状态
      ])

      setInputText('')
      setIsReceivingStream(true)

      // 创建新的EventSource并保存引用
      eventSourceRef.current = new EventSource(
        'http://localhost:8080/simulation/deepseek/stream?question=' +
          window.userMessage
      )

      eventSourceRef.current.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)
          console.log('AI回复:', data)
          const textChunk = data.choices[0].text // 根据你的数据结构调整

          // 使用函数式更新，确保基于最新状态
          setMessages((prev) => {
            const newMessages = [...prev]
            const lastMessageIndex = newMessages.length - 1

            if (
              lastMessageIndex >= 0 &&
              newMessages[lastMessageIndex].id === newAiId
            ) {
              newMessages[lastMessageIndex] = {
                ...newMessages[lastMessageIndex],
                text: newMessages[lastMessageIndex].text + textChunk,
              }
            }

            return newMessages
          })

          // 强制滚动到底部，确保用户看到最新内容
          setTimeout(scrollToBottom, 0)
        } catch (error) {
          console.error('处理消息时出错:', error)
        }
      }

      eventSourceRef.current.onopen = () => {
        console.log('开始接收流式数据')
      }

      eventSourceRef.current.onerror = (error) => {
        console.error('EventSource错误:', error)
        // 关闭连接并更新状态
        eventSourceRef.current.close()
        eventSourceRef.current = null

        setIsReceivingStream(false)
        // 移除streaming标记
        setMessages((prev) =>
          prev.map((msg) =>
            msg.id === newAiId ? { ...msg, streaming: false } : msg
          )
        )
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
    <div className={styles.chatContainer}>
      <div className={styles.title}>DeepSeek交策问答</div>
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
  )
}

export default ChatComponent
