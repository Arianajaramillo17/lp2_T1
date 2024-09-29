package com.prueba.model;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

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

}
