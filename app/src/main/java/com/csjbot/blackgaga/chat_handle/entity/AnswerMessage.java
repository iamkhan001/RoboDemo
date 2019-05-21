package com.csjbot.blackgaga.chat_handle.entity;

/**
 * Created by jingwc on 2017/6/29.
 */

public class AnswerMessage {

    /* 当前问题的sid */
    private String sid;

    /* 标签(标识当前处理问题的哪家平台) */
    private Integer tag;

    /* 回答的内容 */
    private String answerText;

    /* 问题的内容 */
    private String problemText;

    /* 是否搜索完成 */
    private Boolean isSearchComplete;

    /* 优先级 */
    private Integer priority;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getProblemText() {
        return problemText;
    }

    public void setProblemText(String problemText) {
        this.problemText = problemText;
    }

    public Boolean getSearchComplete() {
        return isSearchComplete;
    }

    public void setSearchComplete(Boolean searchComplete) {
        isSearchComplete = searchComplete;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
