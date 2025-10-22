package com.example.pruebas.Models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;


    /**
     * * Constructor NO parametrizado
     */
    public RoleEntity() {}

    /**
     * * Constructor parametrizado
     */
    public RoleEntity(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    // Todos los Getters y Setters a continuación...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    // Creación de BUILDER manual sin usar Lombok
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private ERole name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(ERole name) {
            this.name = name;
            return this;
        }

        public RoleEntity build() {
            return new RoleEntity(id, name);
        }
    }
}
