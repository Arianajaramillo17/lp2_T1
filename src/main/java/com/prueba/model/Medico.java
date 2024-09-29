package com.prueba.model;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name="tb_medico")
@Data


public class Medico {
	@Id
	private int id_medico;
	private String nombre;
}
