package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.model.FileDB;

public interface FileDBRepository extends JpaRepository<FileDB, Long> {
//    public FileDB findByName(String name);
}
