import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.Arrays;

public class javapro extends JFrame {
    public javapro() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame Frame=new JFrame("JEncrypt");
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image image = new ImageIcon("C:\\Users\\srich\\Desktop\\img1.png").getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\srich\\Desktop\\logobg.png");

        JButton en = new JButton("ENCRYPT A FILE");
        en.setBounds(75, 100, 250, 40);
        en.addActionListener(e -> {
            try {
                String inputFile = getFile("Select a file to encrypt");
                String outputFile = getSaveFile("Select a location to save the encrypted file");
                String key = getKey();
                encryptFile(inputFile, outputFile, key);
                JOptionPane.showMessageDialog(null, "File encrypted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error encrypting file: " + ex.getMessage());
            }
        });

        JButton de = new JButton("DECRYPT A FILE");
        de.setBounds(75, 150, 250, 40);
        de.addActionListener(e -> {
            try {
                String inputFile = getFile("Select a file to decrypt");
                String outputFile = getSaveFile("Select a location to save the decrypted file");
                String key = JOptionPane.showInputDialog(null, "Enter the secret key for the encrypted file:");
                decryptFile(inputFile, outputFile, key);
                JOptionPane.showMessageDialog(null, "File decrypted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error decrypting file: " + ex.getMessage());
            }
        });

        JButton exit = new JButton("EXIT");
        exit.setBounds(75, 200, 250, 40);
        exit.addActionListener(e -> System.exit(0));

        panel.add(en);
        panel.add(de);
        panel.add(exit);
        Frame.setIconImage(icon);
        Frame.add(panel);
        Frame.setVisible(true);
        Frame.setContentPane(panel);
        Frame.setSize(400, 500);
        Frame.setLocationRelativeTo(null);
    }

    private static String getFile(String message) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(message);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            throw new RuntimeException("File not selected.");
        }
    }

    private static String getSaveFile(String message) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(message);
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            throw new RuntimeException("Location not selected.");
        }
    }

    private static String getKey() {
        String key = JOptionPane.showInputDialog(null, "Enter a secret key for the file (must be at least 16 characters):");
        if (key == null || key.length() < 16) {
            throw new RuntimeException("Invalid secret key.");
        }
        return key;
    }

    private static void encryptFile(String inputFile, String outputFile, String key) throws Exception {
        File inFile = new File(inputFile);
        FileInputStream fis = new FileInputStream(inFile);
        byte[] inputBytes = new byte[(int) inFile.length()];
        fis.read(inputBytes);
        fis.close();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = Arrays.copyOf(sha.digest(key.getBytes("UTF-8")), 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        File outFile = new File(outputFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        fos.write(outputBytes);
        fos.close();
    }

    private static void decryptFile(String inputFile, String outputFile, String key) throws Exception {
        File inFile = new File(inputFile);
        FileInputStream fis = new FileInputStream(inFile);
        byte[] inputBytes = new byte[(int) inFile.length()];
        fis.read(inputBytes);
        fis.close();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = Arrays.copyOf(sha.digest(key.getBytes("UTF-8")), 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        File outFile = new File(outputFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        fos.write(outputBytes);
        fos.close();
    }

    public static void main(String[] args) {
        new javapro();
    }
}