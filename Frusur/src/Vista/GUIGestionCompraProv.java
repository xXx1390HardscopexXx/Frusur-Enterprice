package Vista;

import Controlador.ControladorFrusur;
import Excepciones.CFException;
import Modelo.EstadoProductor;
import Modelo.Productor;
import Modelo.TipoBerrie;
import Utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class GUIGestionCompraProv extends JDialog {
    private JPanel contentPane;
    private JButton agregarButton;
    private JButton editarButton;
    private JTable tablaProductores;
    private JButton guardarButton;
    private JButton volverButton; // Botón para cerrar/volver

    private DefaultTableModel tableModel;

    public GUIGestionCompraProv() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Gestión de Compras a Productores");

        // 1. Configurar la Tabla
        configurarTabla();
        actualizarTabla();

        // 2. Acción: Agregar Nuevo Productor
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarVentanaAgregar();
            }
        });

        // 3. Acción: Editar Estado
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarCambioEstado();
            }
        });

        // 4. Acción: Guardar
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ControladorFrusur.getInstance().guardar();
                    JOptionPane.showMessageDialog(contentPane, "Datos guardados exitosamente.");
                } catch (CFException ex) {
                    JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 5. Acción: Volver
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Cerrar con X
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void configurarTabla() {
        // Encabezados pedidos: Estado, Berrie, Kilos + (Datos Productor para identificar)
        String[] columnas = {"RUT", "Nombre", "Contacto", "Tipo Berrie", "Kilos", "Estado Actual"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override // Hacer que la tabla no sea editable directamente, solo via botones
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductores.setModel(tableModel);
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0); // Limpiar tabla
        ArrayList<Productor> lista = ControladorFrusur.getInstance().getProductores(); // Asegúrate de tener este getter en Controlador

        for (Productor p : lista) {
            // Solo mostramos productores con actividad relevante o historial?
            // Mostramos todos para ver el historial
            Object[] fila = {
                    p.getRut().toString(),
                    p.getNombre(),
                    p.getContacto(),
                    (p.getTipoBerrieActual() != null ? p.getTipoBerrieActual() : "-"),
                    p.getKilosActuales(),
                    p.getEstado()
            };
            tableModel.addRow(fila);
        }
    }

    // LÓGICA: Agregar Nuevo Productor
    private void mostrarVentanaAgregar() {
        // Panel personalizado para el JOptionPane
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField txtRut = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtContacto = new JTextField();
        JComboBox<String> cmbBerrie = new JComboBox<>(TipoBerrie.getNombres());
        JTextField txtKilos = new JTextField();

        panel.add(new JLabel("RUT (ej: 12.345.678-9):"));
        panel.add(txtRut);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Contacto:"));
        panel.add(txtContacto);
        panel.add(new JLabel("Tipo de Berrie:"));
        panel.add(cmbBerrie);
        panel.add(new JLabel("Cantidad Kilos:"));
        panel.add(txtKilos);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Ingresar Nuevo Productor", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validar RUT
                Rut rutObj = Rut.of(txtRut.getText());
                if (rutObj == null) throw new CFException("RUT inválido.");

                // Validar Kilos
                double kilos = Double.parseDouble(txtKilos.getText());

                // Obtener Enum Berrie
                TipoBerrie berrieSeleccionada = TipoBerrie.valueOf((String) cmbBerrie.getSelectedItem());

                // Llamar al Controlador
                ControladorFrusur.getInstance().gestionarCompraProductor(
                        rutObj,
                        txtNombre.getText(),
                        txtContacto.getText(),
                        berrieSeleccionada,
                        kilos
                );

                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Productor agregado/actualizado correctamente.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error en número de kilos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (CFException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Lógica", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // LÓGICA: Editar Estado
    private void procesarCambioEstado() {
        int filaSeleccionada = tablaProductores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un productor de la tabla.");
            return;
        }

        // Obtener el RUT de la tabla (Columna 0)
        String rutString = (String) tableModel.getValueAt(filaSeleccionada, 0);
        Rut rutObj = Rut.of(rutString);

        // Buscar productor real desde el controlador (usando un helper o findProductor si lo haces público)
        // Como findProductor es private en tu codigo, idealmente hazlo public o usa un metodo getter
        // Asumiré que modificas findProductor a public o usamos un stream rapido aqui:
        Productor productor = null;
        for(Productor p : ControladorFrusur.getInstance().getProductores()){
            if(p.getRut().equals(rutObj)) { productor = p; break; }
        }

        if (productor == null) return;

        EstadoProductor estado = productor.getEstado();
        String mensaje = "";

        if (estado == EstadoProductor.PENDIENTE) {
            mensaje = "¿Desea firmar el contrato?";
        } else if (estado == EstadoProductor.EN_PROCESO) {
            mensaje = "¿Desea realizar la charla?";
        } else if (estado == EstadoProductor.HABILITADO) {
            mensaje = "Está habilitado. ¿Desea recibir la fruta (" + productor.getKilosActuales() + "kg) y guardarla?";
        } else {
            JOptionPane.showMessageDialog(this, "Este productor ya finalizó su proceso (Estado: " + estado + ")");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Cambio de Estado", JOptionPane.YES_NO_OPTION);
        boolean aceptar = (respuesta == JOptionPane.YES_OPTION);

        try {
            String resultado = ControladorFrusur.getInstance().avanzarEstadoProductor(productor, aceptar);
            JOptionPane.showMessageDialog(this, resultado);
            actualizarTabla();
        } catch (CFException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public static void display() {
        GUIGestionCompraProv dialog = new GUIGestionCompraProv();
        dialog.pack();
        dialog.setSize(800, 500); // Un poco más grande para que quepa la tabla
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}