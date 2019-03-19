package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tangwei - Toby
 * @date 2019-03-19
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecordObject {
    private String id;

    private String projectNames;

    private Double cost;

    private String payMethod;

    private String useCard;

    private Date serviceTime;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"projectNames\":\"")
                .append(projectNames).append('\"');
        sb.append(",\"cost\":")
                .append(cost);
        sb.append(",\"payMethod\":\"")
                .append(payMethod).append('\"');
        sb.append(",\"useCard\":")
                .append(useCard);
        sb.append(",\"serviceTime\":\"")
                .append(serviceTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
