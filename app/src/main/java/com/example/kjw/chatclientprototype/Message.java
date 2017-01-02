package com.example.kjw.chatclientprototype;

import java.util.Date;
/**
 * 사용자 입력 및 서버로 부터 응답 메시지 클래스
 * <pre>
 * TYPE_MY_MESSAGE : 메시지
 * TYPE_LOG : 유저 접속 메시지
 * TYPE_ATTRIBUTE_MESSAGE : URI or URL 포함 메시지
 * TYPE_FAIL : 메시지 전송 실패시(socket disconnected or etc)
 * TYPE_ALLOWMESSAGE : 메시지 전송성공 (서버로부터 에코 받았을시 업데이트할 상태)
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 *    김지운, 1.0, 2017.1.2 초기작성
 * </pre>
 *
 * @author 김지운
 * @version 1.0, 2017.1.2 클래스 생성
 * @todo 메시지 생성후 빌더로 빌드
 */
public class Message {


    public static final int TYPE_MY_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ATTRIBUTE_MESSAGE = 2;
    public static final int TYPE_FAIL = 3;
    public static final int TYPE_ALLOWMESSAGE = 4;
    public static final int TYPE_OTHERS_MESSAGE = 5;

    private int mType;
    private String mMessage;
    private String mUsername;
    private Date mDate;
    private int position;


    /**
     * @brief Constructor
     * @return void
     */
    private Message() {}

    /**
     * @brief Method
     * @return int
     */
    public int getType() {
        return mType;
    };

    /**
     * @brief Method
     * @return String
     */
    public String getMessage() {
        return mMessage;
    };

    /**
     * @brief Method
     * @return String
     */
    public String getUsername() {
        return mUsername;
    };

    /**
     * @brief Method
     * @return Date
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * @brief Method
     * @return int
     */
    public int getPosition() {
        return position;
    }


    /**
     * @brief Method
     * @return void
     */
    public void setmType(int mType) {
        this.mType = mType;
    }

    /**
     * Message 클래스 Builder
     * <pre>
     * mType : 메시지 타입
     * mUsername : 유저 닉네임
     * mMessage : 메시지
     * mDate : 메시지 전송시도한 시간
     * </pre>
     *
     * <pre>
     * <b>History:</b>
     *    김지운, 1.0, 2017.1.2 초기작성
     * </pre>
     *
     * @author 김지운
     * @version 1.0, 2017.1.2 클래스 생성
     */
    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private Date mDate;
        private int position;
        /**
         * @brief Constructor
         * @param type
         * @return void
         */
        public Builder(int type) {
            mType = type;
        }

        /**
         * @brief set username
         * @param username
         * @return Builder
         */
        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        /**
         * @brief set message
         * @param message
         * @return Builder
         */
        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        /**
         * @brief set date
         * @param date
         * @return Builder
         */
        public Builder date(Date date){
            mDate = date;
            return this;
        }

        /**
         * @brief set date
         * @param position
         * @return Builder
         */
        public Builder position(int position){
            position = position;
            return this;
        }
        /**
         * @brief Message Class Builder
         * @return Message
         */
        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mDate = mDate;
            message.position = position;
            return message;
        }
    }
}
