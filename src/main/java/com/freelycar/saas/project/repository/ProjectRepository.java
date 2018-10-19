package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2018/10/18
 * @email toby911115@gmail.com
 */
public interface ProjectRepository extends JpaRepository<Project, String> {
}
