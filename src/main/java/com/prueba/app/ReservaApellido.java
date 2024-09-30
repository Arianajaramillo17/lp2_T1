package com.prueba.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.prueba.model.Reserva;
import com.prueba.model.Tipo;
import com.prueba.model.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class ReservaApellido extends JFrame {

    private JTextField txtDni, txtNombre, txtTelefono, txtFecha;
    private JComboBox<String> combo1;
    private JComboBox<String> combo2;

    private JComboBox<Tipo> comboTipo;
    private JComboBox<Medico> comboMedico;
    private JButton btnRegistrar, btnBuscar, btnListado;
    private JTextArea txtAreaListado;

    private EntityManagerFactory emf;
    private EntityManager em;

    public ReservaApellido() {
        setTitle("Mantenimiento de Reservas - T-KURA");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 
        
        emf = Persistence.createEntityManagerFactory("jpa_sesion01");
        em = emf.createEntityManager();

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setBounds(10, 10, 100, 20);
        add(lblDni);
        txtDni = new JTextField();
        txtDni.setBounds(120, 10, 150, 20);
        add(txtDni);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 40, 100, 20);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(120, 40, 150, 20);
        add(txtNombre);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(10, 70, 100, 20);
        add(lblTelefono);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(120, 70, 150, 20);
        add(txtTelefono);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(10, 100, 100, 20);
        add(lblTipo);
        comboTipo = new JComboBox<>();
        comboTipo.setBounds(120, 100, 150, 20);
        comboTipos();
        add(comboTipo);

        JLabel lblMedico = new JLabel("Médico:");
        lblMedico.setBounds(10, 130, 100, 20);
        add(lblMedico);
        comboMedico = new JComboBox<>();
        comboMedico.setBounds(120, 130, 150, 20);
        comboMedico(); 
        add(comboMedico);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(10, 160, 150, 20);
        add(lblFecha);
        txtFecha = new JTextField("2024-09-30"); 
        txtFecha.setBounds(160, 160, 110, 20);
        txtFecha.setEditable(false); 
        add(txtFecha);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(10, 200, 100, 30);
        add(btnRegistrar);

        btnListado = new JButton("Listado");
        btnListado.setBounds(230, 200, 100, 30);
        add(btnListado);

        txtAreaListado = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtAreaListado);
        scrollPane.setBounds(10, 250, 360, 100);
        add(scrollPane);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarReserva();
            }
        });

        btnListado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarReservas();
            }
        });

        setVisible(true);
    }

    private void comboTipos() {
    	  List<Tipo> tipos = em.createQuery("SELECT t FROM Tipo t", Tipo.class).getResultList();
          for (Tipo tipo : tipos) {
              comboTipo.addItem(tipo); 
          }
    }

    private void comboMedico() {
        List<Medico> medicos = em.createQuery("SELECT m FROM Medico m", Medico.class).getResultList();
        for (Medico medico : medicos) {
            comboMedico.addItem(medico); 
        }
    }

   private void registrarReserva() {
    try {
        int dni = Integer.parseInt(txtDni.getText());
        String nombre = txtNombre.getText();
        int telefono = Integer.parseInt(txtTelefono.getText());

        // Obtener los elementos seleccionados como ComboItem
        Tipo tipoSeleccionado =(Tipo) comboTipo.getSelectedItem();
        Medico medicoSeleccionado = (Medico) comboMedico.getSelectedItem();

        if (nombre.isEmpty() || tipoSeleccionado == null || medicoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setDni(dni);
        nuevaReserva.setNombre(nombre);
        nuevaReserva.setTelefono(telefono);

        // Buscar el Tipo por nombre
        Tipo tipo = em.createQuery("SELECT t FROM Tipo t WHERE t.descripcion = :descripcion", Tipo.class)
                .setParameter("descripcion", tipoSeleccionado.getDescripcion())
                .getSingleResult();

        // Buscar el Medico por nombre
        Medico medico = em.createQuery("SELECT m FROM Medico m WHERE m.nombre = :nombre", Medico.class)
                .setParameter("nombre", medicoSeleccionado.getNombre())
                .getSingleResult();

        // Asignar los objetos a la reserva
        nuevaReserva.setTipoObject(tipoSeleccionado);
        nuevaReserva.setMedicoObject(medicoSeleccionado);

        em.getTransaction().begin();
        em.persist(nuevaReserva);
        em.getTransaction().commit();
em.close();
        JOptionPane.showMessageDialog(this, "Reserva registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "DNI o Teléfono inválido. Deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NoResultException e) {
        JOptionPane.showMessageDialog(this, "No se encontró el tipo o médico especificado.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al registrar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
   
}

    private void listarReservas() {
        List<Reserva> reservas = em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
        StringBuilder sb = new StringBuilder();
        for (Reserva reserva : reservas) {
            sb.append("Id Reserva: ").append(reserva.getNum_reserva()).append("\n");
            sb.append("DNI: ").append(reserva.getDni()).append("\n");
            sb.append("Nombre: ").append(reserva.getNombre()).append("\n");
            sb.append("Teléfono: ").append(reserva.getTelefono()).append("\n");
            sb.append("Tipo: ").append(reserva.getTipoObject().getDescripcion());
            sb.append("Médico: ").append(reserva.getMedicoObject().getNombre());
            sb.append("Fecha: ").append(txtFecha.getText()).append("\n");
            sb.append("---------------------------------\n");
            System.out.print(reserva);
        }
        txtAreaListado.setText(sb.toString());
    }

    public static void main(String[] args) {
        new ReservaApellido();
    }
}
