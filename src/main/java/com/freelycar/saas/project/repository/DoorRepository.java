package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Door;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
public interface DoorRepository extends JpaRepository<Door, String> {
    Door findTopByOrderId(String sn);

    Door findTopByArkSnAndDoorSn(String arkSn, String doorSn);

    List<Door> findByArkSnAndStateAndDelStatus(String arkSn, int state, boolean delStatus);
}
