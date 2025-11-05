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

public class PlanEntreno extends JPanel {

    private JComboBox<String> comboDias;
    private ImagenEscalada imagenPanel;

    public PlanEntreno() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //ComboBox opciones
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("");
        modelo.addElement("2 días por semana");
        modelo.addElement("3 días por semana");
        modelo.addElement("4 días por semana");
        

        comboDias = new JComboBox<>(modelo);
        comboDias.setFont(new Font("Arial", Font.PLAIN, 16));

        imagenPanel = new ImagenEscalada("https://www.sportlife.es/uploads/s1/11/06/11/77/istock-587204700.jpeg");

        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("¿Cuántos días quieres entrenar por semana?"));
        panelSuperior.add(comboDias);

        add(panelSuperior, BorderLayout.NORTH);
        add(imagenPanel, BorderLayout.CENTER);

        comboDias.addActionListener(e -> actualizarImagen());
    }

    private void actualizarImagen() {
        String seleccion = (String) comboDias.getSelectedItem();
        String url = "";
        
        //Enlaces
        switch (seleccion) {
            case "":
                break;
            case "2 días por semana":
                url = "https://cdn.shopify.com/s/files/1/0918/2062/2161/files/La_mejor_rutina_FULL_BODY_2_dias.webp?v=1746345403";
                break;
            case "3 días por semana":
                url = "https://underrated.es/cdn/shop/articles/La_mejor_rutina_FULL_BODY_3_dias.webp?v=1746188529";
                break;
            case "4 días por semana":
                url = "https://static.docsity.com/documents_first_pages/2016/03/26/56b7173e5d6e53f008ff4cf80772144e.png";
                break;
        }

        remove(imagenPanel);
        imagenPanel = new ImagenEscalada(url);
        add(imagenPanel, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Plan de Entrenamiento");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new PlanEntreno());
        frame.setVisible(true);
    }
}
