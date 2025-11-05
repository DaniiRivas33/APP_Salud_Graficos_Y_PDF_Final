/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoAppsalud;


/**
 *
 * @author danie
 */




import javax.swing.*;
import java.awt.*;

public class AppSalud extends JFrame {

    private JButton jButtonEjercicio;
    private JButton jButtonSalud;
    private JLabel jLabelTitulo;
    private ImagenEscalada panelImagen;

    public AppSalud() {
        setTitle("App sobre Ejercicio y Salud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        jLabelTitulo = new JLabel("App sobre Ejercicio y Salud", SwingConstants.CENTER);
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        jButtonSalud = new JButton("Salud");
        jButtonEjercicio = new JButton("Plan Entreno");

        panelImagen = new ImagenEscalada("src/main/java/imagenes/images.jpg");
        panelImagen.setPreferredSize(new Dimension(300, 150));

        //Panel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
       
        //Label
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 20, 10);
        c.anchor = GridBagConstraints.CENTER;
        panelCentral.add(jLabelTitulo, c);
        
        //Imagen
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(0, 10, 20, 10);
        panelCentral.add(panelImagen, c);

        //Botones
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(10, 20, 10, 20);
        panelCentral.add(jButtonSalud, c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(10, 20, 10, 20);
        panelCentral.add(jButtonEjercicio, c);

        //Panel central
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelCentral, gbc);

        //Funcion Botones
        jButtonSalud.addActionListener(e -> {
        JFrame frame = new JFrame("Salud");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 300);
            frame.add(new Salud());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            this.dispose(); //por algun motivo esta linea de codigo no la hace
});
        jButtonEjercicio.addActionListener(e -> {
            JFrame frame = new JFrame("Plan de Entrenamiento");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new PlanEntreno());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            this.dispose();
});

        
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        AppSalud ventana = new AppSalud();   
        ventana.setTitle("App sobre Ejercicio y Salud");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 400);
        ventana.setMinimumSize(new Dimension(400, 300));
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    });
}

}
