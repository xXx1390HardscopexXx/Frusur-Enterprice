package Vista;

import Controlador.ControladorFrusur;
import Modelo.CuentaAgro;

import javax.swing.*;
import java.awt.event.*;
import java.util.Optional;

public class GUISesionAgro extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usuarioText;
    private JTextField contrasenaText;

    public GUISesionAgro() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (validarCampos()){
            Optional<CuentaAgro> cuenta = ControladorFrusur.getInstance().findAgronomo(usuarioText.getText());
            if(cuenta.isPresent()){
                if (cuenta.get().getContrasena().equals(contrasenaText.getText())){
                    dispose();
                    GUIGestionCompraProv.display();
                }else{
                    JOptionPane.showMessageDialog
                            (this, "La contraseña es incorrecta");
                }
            }else if(cuenta.isEmpty()){
                JOptionPane.showMessageDialog
                        (this, "No existe una cuenta con ese nombre de usuario",
                                "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private boolean validarCampos() {
        if (usuarioText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (contrasenaText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUISesionAgro dialog = new GUISesionAgro();
        dialog.pack();
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
    }
}
