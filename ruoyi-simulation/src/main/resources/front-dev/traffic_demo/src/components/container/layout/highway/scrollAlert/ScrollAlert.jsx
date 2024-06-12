import React, { useEffect, useRef, useState } from 'react'
import styles from '../css/scrollAlert.module.scss';
export default function ScrollAlert() {
    const [currentIndex, setCurrentIndex] = useState(0);
    const noticeBarListRef = useRef(null);

    // List of notices
    const notices = [
        { userName: "1. Jerry***", saveAmount: "$10", text: "by using mobile topup voucher." },
        { userName: "2. Tom***", saveAmount: "$8", text: "by using mobile topup voucher." },
        { userName: "3. Jack***", saveAmount: "$10", text: "by using mobile topup voucher." }
    ];

    // Autoscroll logic
    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentIndex((prevIndex) => (prevIndex + 1) % notices.length);
        }, 3000); // Change every 3 seconds

        return () => clearInterval(interval);
    }, [notices.length]);

    return (
        <div className={styles.pageContainer}>
            <div className={styles.noticebar}>
                <div className={styles.noticebarListContainer} ref={noticeBarListRef}>
                    <div className={styles.noticebarList} style={{ transform: `translateY(-${currentIndex * 100}%)`, transition: 'transform 0.5s ease' }}>
                        {notices.map((notice, index) => (
                            <div className={styles.noticebarItem} key={index}>
                                <div className={styles.customNoticeItem}>
                                    <span className={styles.userName}>{notice.userName}</span> save <span className={styles.saveAmount}>{notice.saveAmount}</span> {notice.text}
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                <div className={styles.noticebarCloseIcon}>
                    <img
                        src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6/NlyAAAAAXNSR0IArs4c6QAAAfZJREFUaEPt2u1NwzAQBuBLukDZABaoEN2hYgvKCEwAI7ABsAUfI6BIFAaADegCxMgSkUKaj7N97zlK3L91XD9+nSY+OaOZfbKZeSmBp554SjglPLEZSEt6YoEecFLCXQmv1psLIrolon1O+c1b8fgQczX4joed8Gq9+SSi4wqZU76Nhf7D3tcmfP9ePB9xAnABfxPRst5pDHQLljLKvnbF04ko+PTsfFtm5V2zU010G9aOx+Tm8uP1pZ54p52dsO0hJloCaw1O4FhoKawXWBstifUGa6GlsUFgNBqBDQaj0CisCFgajcSKgaXQaKwoOBStgRUH+6K1sBCwK1oTCwNz0dpYKHgIXVJpmxy88LtsBDi7o2Yb53dp1x/p2nC09YPGwhOuUBy0BlYN3Le87XdaWFVw1x+UHYRmEQF+D1tQH7Za9lpoOJiD1URDwX3P2cXPgmLUyGBgzktFjBoZBMzBDj2yUPe0ONgFGwMtCvbBaqPFwCFYTbQIWAKrhQ4GS2I10EFgBBaN9gYjsUi0F1gDi0I7gzWxCLQTOAZWGs0Gx8RKol3A/854aFcqBtD4Mx6aZZlmwa+5y4Kd8TCZuSYyyzI3V9wzFa5VTm57i/YZD3tJcwcy9nYJPPaEQseXEg6dwbFfnxIee0Kh40sJh87g2K+fXcK/GTb9TA793a8AAAAASUVORK5CYII="
                        alt=""
                    />
                </div>
            </div>
        </div>
    );
};