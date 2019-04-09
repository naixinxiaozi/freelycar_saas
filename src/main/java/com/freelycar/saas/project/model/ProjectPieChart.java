package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangwei - Toby
 * @date 2019-04-08
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPieChart {
    private String projectId;

    private String projectName;

    private Double projectPrice;

    private Double percent;

    private Object count;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"projectId\":\"")
                .append(projectId).append('\"');
        sb.append(",\"projectName\":\"")
                .append(projectName).append('\"');
        sb.append(",\"count\":")
                .append(count);
        sb.append(",\"projectPrice\":")
                .append(projectPrice);
        sb.append(",\"percent\":")
                .append(percent);
        sb.append('}');
        return sb.toString();
    }
}
