package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author tangwei - Toby
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class CardProjectInfo implements Serializable {
    private static final long serialVersionUID = 10L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    @Length(max = 50)
    private String id;

    @Column
    private Integer times;

    @Column
    private String cardServiceId;

    @Column
    private String projectId;

    @Override
    public String toString() {
        return "CardProjectInfo{" +
                "id='" + id + '\'' +
                ", times=" + times +
                ", cardServiceId='" + cardServiceId + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getCardServiceId() {
        return cardServiceId;
    }

    public void setCardServiceId(String cardServiceId) {
        this.cardServiceId = cardServiceId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public CardProjectInfo() {
    }
}
