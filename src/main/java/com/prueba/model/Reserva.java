package com.prueba.model;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name="tb_reserva")
@Data



public class Reserva {
@Id
private int num_reserva;
private int dni;
private String nombre;
private int telefono;
private int id_tipo;
private int id_medico;

// Relación ManyToOne con la tabla tb_tipo
@ManyToOne
@JoinColumn(name = "tipoObjec", referencedColumnName = "id_tipo")
private Tipo tipoObject;

// Relación ManyToOne con la tabla tb_medico
@ManyToOne
@JoinColumn(name = "medicoObject", referencedColumnName = "id_medico")
private Medico medicoObject;
}
