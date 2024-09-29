package com.prueba.model;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name="tb_tipo")
@Data


public class Tipo {
	@Id
	private int id_tipo;
	private int  precio;
	private String descripcion;
}
