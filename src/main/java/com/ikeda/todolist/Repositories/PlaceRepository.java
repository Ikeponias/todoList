package com.ikeda.todolist.Repositories;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ikeda.todolist.Models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Place p WHERE p.id = :id")
    Place findForUpdateById(@Param("id") Long id);

}