package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangwei - Toby
 * @date 2019-02-12
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArkDevice {
    private String deviceId;

    private String boardId;

    private Integer boxId;

    private Boolean online;
}
