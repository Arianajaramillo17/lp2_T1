package com.prueba.app;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.prueba.model.Reserva;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ListadoApellido extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea txtAreaListado;
    private EntityManagerFactory emf;
    private EntityManager em;

    public ListadoApellido() {
        setTitle("Listado de Reservas por Apellido");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        txtAreaListado = new JTextArea();
        txtAreaListado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaListado);
        add(scrollPane, BorderLayout.CENTER);

        emf = Persistence.createEntityManagerFactory("jpa_sesion01");
        em = emf.createEntityManager();

        cargarReservasPorApellido();

        setVisible(true);
    }

    private void cargarReservasPorApellido() {
        List<Reserva> reservas = em.createQuery("SELECT r FROM Reserva r ORDER BY r.nombre", Reserva.class).getResultList();
        StringBuilder sb = new StringBuilder();

        for (Reserva reserva : reservas) {
            sb.append("Id Reserva: ").append(reserva.getNum_reserva()).append("\n");
            sb.append("DNI: ").append(reserva.getDni()).append("\n");
            sb.append("Nombre: ").append(reserva.getNombre()).append("\n");
            sb.append("Teléfono: ").append(reserva.getTelefono()).append("\n");
            sb.append("Tipo: ").append(reserva.getTipoObject().getDescripcion()).append("\n");
            sb.append("Médico: ").append(reserva.getMedicoObject().getNombre()).append("\n");
            sb.append("Precio: ").append(reserva.getTipoObject().getPrecio()).append("\n");

            sb.append("---------------------------------\n");
        }

        txtAreaListado.setText(sb.toString());
    }

    public static void main(String[] args) {
        new ListadoApellido();
    }
}
