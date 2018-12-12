package com.freelycar.saas.basic.wrapper;

/**
 * @author tangwei - Toby
 * @date 2018-12-12
 * @email toby911115@gmail.com
 */
public enum DelStatus {
    /**
     * 有效的（数据库中对应0，bit类型）
     */
    EFFECTIVE(false),
    /**
     * 无效的（数据库中对应1，bit类型）
     */
    NONEFFECTIVE(true);

    private boolean value;

    DelStatus(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }}
