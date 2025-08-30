// ---- Importaciones necesarias ----
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

public class program extends JFrame {

    // Campos de texto
    private JTextField txtDNI, txtNombre, txtMarca;
    private JComboBox<String> cmbModelo;
    private JCheckBox chkTC;
    private JSpinner dateNacimiento;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public RentCarApp() {
        setTitle("zxRentCar - Registro de Clientes");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // --- Componentes ---
        add(new JLabel("DNI"));
        txtDNI = new JTextField(15);
        add(txtDNI);

        add(new JLabel("Nombre"));
        txtNombre = new JTextField(15);
        add(txtNombre);

        add(new JLabel("Modelo"));
        cmbModelo = new JComboBox<>(new String[]{"Sedan", "SUV", "Hatchback", "Pickup"});
        add(cmbModelo);

        add(new JLabel("Marca"));
        txtMarca = new JTextField(15);
        add(txtMarca);

        add(new JLabel("Fecha Nacimiento"));
        dateNacimiento = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateNacimiento, "dd/MM/yyyy");
        dateNacimiento.setEditor(editor);
        add(dateNacimiento);

        chkTC = new JCheckBox("Tiene T/C");
        add(chkTC);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        add(btnGuardar);
        add(btnLimpiar);

        // --- Tabla ---
        String[] columnas = {"DNI", "Nombres", "Modelo", "Marca", "Fecha Nacimiento", "Tiene T/C"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(650, 200));
        add(scroll);

        // --- Eventos ---
        btnGuardar.addActionListener(e -> guardarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void guardarCliente() {
        String dni = txtDNI.getText().trim();
        String nombre = txtNombre.getText().trim();
        String modelo = cmbModelo.getSelectedItem().toString();
        String marca = txtMarca.getText().trim();
        Date fecha = (Date) dateNacimiento.getValue();
        boolean tieneTC = chkTC.isSelected();

        // Validaciones
        if (dni.isEmpty() || nombre.isEmpty() || marca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.");
            return;
        }
        if (!tieneTC) {
            JOptionPane.showMessageDialog(this, "El cliente debe tener una tarjeta de crédito.");
            return;
        }

        // Validar edad mayor de 18
        int edad = calcularEdad(fecha);
        if (edad < 18) {
            JOptionPane.showMessageDialog(this, "El cliente debe ser mayor de 18 años.");
            return;
        }

        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaNacimiento = sdf.format(fecha);

        // Insertar en tabla
        modeloTabla.addRow(new Object[]{dni, nombre, modelo, marca, fechaNacimiento, (tieneTC ? "Si" : "No")});

        JOptionPane.showMessageDialog(this, "Cliente registrado con éxito.");
        limpiarCampos(); // limpia campos después de guardar
    }

    private void limpiarCampos() {
        txtDNI.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        cmbModelo.setSelectedIndex(0);
        chkTC.setSelected(false);
        dateNacimiento.setValue(new Date()); // reinicia la fecha de nacimiento
    }

    private int calcularEdad(Date fechaNacimiento) {
        Date hoy = new Date();
        long diff = hoy.getTime() - fechaNacimiento.getTime();
        long años = diff / (1000L * 60 * 60 * 24 * 365);
        return (int) años;
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new program().setVisible(true));
    }
}
