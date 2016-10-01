/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubes1;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author rezaramadhan
 */
public class UIHandler  extends JFrame {
    //datas
    private List<MatrixJadwal> matrixJadwalList;
    private List<Ruangan> ruangList;
    private List<Jadwal> jadwalList;
    private final int nHari = 5;
    private final int nJam = 11;
    
    //UI Element
    private JLabel[][] tabel;
    Button ubahJadwalButton;
    Button runButton;
    JLabel presentasiTotalLabel;
    JLabel totalConflictLabel;
    JComboBox ruangComboBox;
    JRadioButton HCRButton;
    JRadioButton SARButton;
    JRadioButton GARButton;
    
    public UIHandler() throws InterruptedException {
        //UI handling
        super("Appa Scheduler");
        initComponent();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        
        
        
        //data handling
        ruangList = new ArrayList<>();
        jadwalList = new ArrayList<>();
        matrixJadwalList = new ArrayList<>();
    }
   
    private void initComponent() {
        
        
        JPanel headContainer = new JPanel();
        JPanel tableContainer = new JPanel();
        JLabel title = new JLabel("Appa Yipyip Scheduler");
        
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 32));
        this.setLayout(new BorderLayout());
        getContentPane().add(headContainer, BorderLayout.PAGE_START);
        getContentPane().add(tableContainer, BorderLayout.PAGE_END);
        
        JPanel head = new JPanel();
        head.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        //add title
        headContainer.setLayout(new BoxLayout(headContainer, BoxLayout.PAGE_AXIS));
        headContainer.add(title);
        headContainer.add(head);
        
        //add opsi2 gitu
        head.setLayout(new FlowLayout());
        JPanel leftHead = new JPanel();
        JPanel rightHead = new JPanel();
        head.add(leftHead);
        head.add(rightHead);
        
        
        //add pilihan algoritma
        JLabel algo = new JLabel("Pilihan Algoritma");
        HCRButton = new JRadioButton("Hill Climbing");
        SARButton = new JRadioButton("Simmulated Annealing");
        GARButton = new JRadioButton("Genetic Algorithm");
        runButton = new Button("Run!");
        runButton.setEnabled(false);
        runButton.addActionListener(this::execute);
        ButtonGroup group = new ButtonGroup();
        group.add(HCRButton);
        group.add(SARButton);
        group.add(GARButton);
        
        rightHead.setLayout(new BoxLayout(rightHead, BoxLayout.PAGE_AXIS));
        rightHead.add(algo);
        rightHead.add(HCRButton);
        rightHead.add(SARButton);
        rightHead.add(GARButton);
        rightHead.add(runButton);
        
        //add container ruangan & file chooser
        JPanel chooseRuangContainer = new JPanel();
        JPanel chooseFileContainer = new JPanel();
        leftHead.setLayout(new BorderLayout(1,1));
        leftHead.add(chooseRuangContainer, BorderLayout.EAST);
        leftHead.add(chooseFileContainer, BorderLayout.WEST);
        
        chooseRuangContainer.setLayout(new BoxLayout(chooseRuangContainer, BoxLayout.PAGE_AXIS));
        chooseFileContainer.setLayout(new BoxLayout(chooseFileContainer, BoxLayout.PAGE_AXIS));
        chooseRuangContainer.setBorder(BorderFactory.createLineBorder(Color.RED));
        chooseFileContainer.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        
        //add pilihan ruangan
        JLabel ruangLabel = new JLabel("Ruangan:");
        ruangComboBox = new JComboBox();
        ruangComboBox.addActionListener(this::comboBoxChanged);
        chooseRuangContainer.add(ruangLabel);
        chooseRuangContainer.add(ruangComboBox);
        
        //add pilihan file & ubah kelas manual
        Button fileButton = new Button("Choose Input File!");
        ubahJadwalButton = new Button("Change Schedule");
        ubahJadwalButton.setEnabled(false);
        ubahJadwalButton.addActionListener(this::changeKelas);
        chooseFileContainer.add(fileButton);
        chooseFileContainer.add(ubahJadwalButton);
        fileButton.addActionListener(this::chooseFileClicked);
        
        //add staticstics
        presentasiTotalLabel = new JLabel("Presentasi penggunaan ruangan: ");
        totalConflictLabel = new JLabel("Jumlah conflict: ");
        
        JPanel statsContainer = new JPanel();
        leftHead.add(statsContainer, BorderLayout.PAGE_END);
        
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.PAGE_AXIS));
        statsContainer.add(presentasiTotalLabel);
        statsContainer.add(totalConflictLabel);
        
        
        //add table
        tabel = new JLabel[nJam+1][nHari+1];
        tableContainer.setLayout(new GridLayout(nJam+1, nHari+1));
        for (int i = 0; i<nJam+1; i++) {
            for (int k = 0; k < nHari+1; k++) {
                tabel[i][k] = new JLabel();
                tabel[i][k].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                tabel[i][k].setBackground(Color.PINK);
                tabel[i][k].setOpaque(true);
                tableContainer.add(tabel[i][k]);
            }
        }
        for (int i = 1; i < nJam+1; i++) {
            tabel[i][0].setText(Integer.toString(i+6) + ".00");
        }
        tabel[0][0].setText("Jam\\Hari");
        tabel[0][1].setText("Senin");
        tabel[0][2].setText("Selasa");
        tabel[0][3].setText("Rabu");
        tabel[0][4].setText("Kamis");
        tabel[0][5].setText("Jumat");
        
    }
    
    private void changeKelas(java.awt.event.ActionEvent ect) {
        String kelas = JOptionPane.showInputDialog("Masukkan kelas: ");
        String ruangPindah = JOptionPane.showInputDialog("Masukkan ruangan pindah: ");
        int hariPindah = Integer.parseInt(JOptionPane.showInputDialog("Masukkan hari pindah: "));
        int jamPindah = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jam pindah: "));
        MatrixJadwal.PindahJadwal(kelas, ruangPindah, jamPindah, hariPindah, matrixJadwalList, ruangList, jadwalList);
        refreshUI();
    }
    
    //ketika combo box terubah
    private void comboBoxChanged(java.awt.event.ActionEvent evt){
        refreshTable();
    }
    
    //ngerefresh tabel, dipanggil ketika refresh tabel dan combo box berubah
    private void refreshTable() {
        if (matrixJadwalList.isEmpty()) {
            System.out.println("Matrix kosong");
        } else {
            String namaRuang = ruangComboBox.getSelectedItem().toString();
            
            //cari nama ruang di list ruangan, pasti ketemu
            int idx = searchNamaRuang(namaRuang);
            
            for (int i = 1; i <= nJam; i++) {
                for (int j = 1; j <= nHari; j++) {
                    tabel[i][j].setText(matrixJadwalList.get(idx).elmt[i][j]);
                    
                    //SET WARNA TABEL DISINI~
                    //tabel[i][j].setBackground(Color.red);
                }
            }
        }
    }
    
      
    //refresh table sama statistics pake data yg baru diubah
    private void refreshUI() {
        refreshTable();
        
        //refresh statistics
        if (!matrixJadwalList.isEmpty()) {
            int nConflict = MatrixJadwal.HitungConflict(matrixJadwalList, ruangList);
            float persentasi = MatrixJadwal.HitungPersen(matrixJadwalList, ruangList);

            presentasiTotalLabel.setText("Persentasi penggunaan ruangan: " + Float.toString(persentasi));
            totalConflictLabel.setText("Jumlah conflict: " + Integer.toString(nConflict));
        }
    }
    
     //cari nama ruang di list ruangan, pasti ketemu
    private int searchNamaRuang(String namaRuang) {
        int idx = -1;
        boolean found = false;
        while (idx < ruangList.size() && !found) {
            idx++;
            found = ruangList.get(idx).Nama.equals(namaRuang);
        }
        if (found) 
            return idx;
        else
            return -1;
    } 
    
    //pilih input file
    private void chooseFileClicked(java.awt.event.ActionEvent evt){                                         
        System.out.println("Clicked");
        try {
            ruangList.clear();
            jadwalList.clear();
            JFileChooser fad = new JFileChooser();
            File input;
            int rent = fad.showOpenDialog(fad);
            input = fad.getSelectedFile();
            Scanner in = new Scanner(input);
            String nextLine = in.nextLine();
            String[] tes;
            Ruangan B = new Ruangan();
            nextLine = in.nextLine();
            while(!nextLine.equals("")) {
                tes = nextLine.split(";");
                B.Nama=tes[0];
                ruangComboBox.addItem(B.Nama);
                B.JamMulai=tes[1];
                B.JamSelesai=tes[2];
                B.Hari=tes[3];
                ruangList.add(B);
                nextLine = in.nextLine();
                B = new Ruangan();
            }

            nextLine = in.nextLine();
            Jadwal A = new Jadwal();
            while(in.hasNext()) {
                nextLine = in.nextLine();
                tes = nextLine.split(";");
                A.NamaKegiatan=tes[0];
                A.Ruangan=tes[1];
                A.JamMulai=tes[2];
                A.JamSelesai=tes[3];
                A.Durasi=tes[4];
                A.Hari=tes[5];
                jadwalList.add(A);
                A = new Jadwal();
            }
            in.close();
            
            runButton.setEnabled(true);
//            System.out.println(jadwalList.size());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UIHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //handler ketika
    private void execute (java.awt.event.ActionEvent evt) {
        ubahJadwalButton.setEnabled(true);
        if (GARButton.isSelected()) {
            GeneticAlgorithm g = new GeneticAlgorithm(jadwalList, ruangList);
            String s = g.execute();
            matrixJadwalList = g.isiMatrixJadwal(s);
            
        } else if (HCRButton.isSelected()) {
            matrixJadwalList = HillClimbing.HillClimbing(ruangList, jadwalList);
        } else if (SARButton.isSelected()) {
            SimulatedAnnealing sa = new SimulatedAnnealing(jadwalList, ruangList);
            sa.execute();
            matrixJadwalList = sa.scheduleWorld;
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Please select any of the provided Algorithm");
            ubahJadwalButton.setEnabled(false);
        }
        refreshUI();
    }
    
}
    
