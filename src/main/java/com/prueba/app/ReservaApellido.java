package com.prueba.app;

import javax.swing.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.prueba.model.Reserva;
import com.prueba.model.Tipo;
import com.prueba.model.Medico;

public class ReservaApellido extends JFrame {

    private JTextField txtDni, txtNombre, txtTelefono;
    private JComboBox<Tipo> comboTipo;
    private JComboBox<Medico> comboMedico;
    private JButton btnRegistrar;
    
    // JPA EntityManager
    private EntityManagerFactory emf;
    private EntityManager em;

    public ReservaApellido() {
        setTitle("Registro de Reserva - T-KURA");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        // Inicializar JPA
        emf = Persistence.createEntityManagerFactory("jpa_sesion01");
        em = emf.createEntityManager();

        // Labels y campos
        add(new JLabel("DNI:"));
        txtDni = new JTextField();
        add(txtDni);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        add(txtTelefono);

        // Combo para Tipo de Atención
        add(new JLabel("Tipo de Atención:"));
        comboTipo = new JComboBox<>();
        cargarTipos(); // Método para cargar tipos desde la base de datos
        add(comboTipo);

        // Combo para Médico
        add(new JLabel("Médico:"));
        comboMedico = new JComboBox<>();
        cargarMedicos(); // Método para cargar médicos desde la base de datos
        add(comboMedico);

        // Botón Registrar
        btnRegistrar = new JButton("Registrar Reserva");
        add(btnRegistrar);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarReserva(); // Lógica para registrar la reserva
            }
        });

        setVisible(true);
    }

    // Método para cargar tipos desde la base de datos
    private void cargarTipos() {
        List<Tipo> tipos = em.createQuery("SELECT t FROM Tipo t", Tipo.class).getResultList();
        for (Tipo tipo : tipos) {
            comboTipo.addItem(tipo); // Agregar cada tipo al comboBox
        }
    }

    // Método para cargar médicos desde la base de datos
    private void cargarMedicos() {
        List<Medico> medicos = em.createQuery("SELECT m FROM Medico m", Medico.class).getResultList();
        for (Medico medico : medicos) {
            comboMedico.addItem(medico); // Agregar cada médico al comboBox
        }
    }

    // Método para registrar la reserva en la base de datos
    private void registrarReserva() {
        try {
        	 int dni = Integer.parseInt(txtDni.getText());
             String nombre = txtNombre.getText();
             int telefono = Integer.parseInt(txtTelefono.getText());
             Tipo tipoSeleccionado = (Tipo) comboTipo.getSelectedItem();
             Medico medicoSeleccionado = (Medico) comboMedico.getSelectedItem();


            if (nombre.isEmpty() || tipoSeleccionado == null || medicoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear la nueva reserva
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setDni(dni);
            nuevaReserva.setNombre(nombre);
            nuevaReserva.setTelefono(telefono);
            nuevaReserva.setTipoObject(tipoSeleccionado);
            nuevaReserva.setMedicoObject(medicoSeleccionado);

            // Persistir la reserva en la base de datos
            em.getTransaction().begin();
            em.persist(nuevaReserva);
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(this, "Reserva registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ReservaApellido();
    }
}
