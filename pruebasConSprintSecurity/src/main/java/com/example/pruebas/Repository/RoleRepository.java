package com.example.pruebas.Repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.pruebas.Models.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

}
