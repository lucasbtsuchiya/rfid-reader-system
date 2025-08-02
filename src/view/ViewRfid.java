/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.bean.Evento;
import model.bean.Relatorio;
import model.bean.RelatorioTableModel;
import model.bean.Rfid;
import model.bean.RfidTableModel;
import model.dao.RfidDao;
import util.ArduinoSerial;

/**
 *
 * @author Lucas B Tsuchiya
 */
public class ViewRfid extends javax.swing.JFrame {

    /**
     * Creates new form ViewRfid
     */
    RfidTableModel tableModel = new RfidTableModel();
    RelatorioTableModel tableModelR = new RelatorioTableModel();


    
    public ViewRfid() {
        
        
        
        initComponents();
        //jTAlunos.setModel(tableModel);
      
        
       
      // this.ViewComboBox();
        
        ArduinoSerial arduino = new ArduinoSerial("COM5");
        Thread t = new Thread(){
            String id = arduino.read();
            String idread = "";
            String ev = "";
            String alunosalvo = "";
            String eventosalvo = "";
             
            @Override
            public void run(){
                RfidDao check = new RfidDao();
                Rfid aluno = new Rfid();
               ViewComboBox();
                arduino.initialize();
                while(true){
                      lbRfid.setText(arduino.read());
                      id = arduino.read();
                    if(arduino.read() != null && id != idread){
                        idread = lbRfid.getText();
                        
                        System.out.println(check.validar(arduino.read()));
                        aluno = check.validar(idread);
                        if(aluno != null){
                            System.out.println("Tag Encontrada");
                            aluno = check.readUid(id);
                            lblNome.setText(aluno.getNome());
                            lblMatricula.setText(Integer.toString(aluno.getMatricula()));
                            lblCurso.setText(aluno.getCurso());
                            arduino.send("1");
                            Evento evento = (Evento) cbEventos.getSelectedItem();
                            ev = evento.getNome_evento();
                            if(idread != alunosalvo){
                                alunosalvo = idread;
                                eventosalvo = ev;
                                txt_uid_tag.setText("");
                                Relatorio lista = new Relatorio ();
                                lista.setNome_aluno(lblNome.getText());
                                lista.setNome_evento(ev);
                                lista.setMatricula_aluno(Integer.parseInt(lblMatricula.getText()));
                                lista.setData_evento(evento.getData());
                                lista.setResponsavel(evento.getResponsavel());
                                lista.setCurso_aluno(lblCurso.getText());
                                RfidDao dao = new RfidDao();
                                dao.Gerar_relatorio(lista);
                                
                                
                            }
                            System.out.println("Evento Selecionado:" +ev);
                            
                            
                        }else{
                            System.out.println("Tag NÂO Encontrada");
                            txt_uid_tag.setText(id);
                            lblNome.setText("");
                            lblMatricula.setText("");
                            lblCurso.setText("");
                            arduino.send("0");
                        }
                        
                         //System.out.println(idread);
                        arduino.sleep(5000);
                    }
                        
            }
                 
                
            }   
        };
            
        t.start();
    }
    
    /**
     *
     * 
     */
    
    
        
        
    public void ViewComboBox(){
            RfidDao dao = new RfidDao();
            for(Evento e: dao.readEvento()){
                cbEventos.addItem(e);
            }
        }        
        
        
         public void ViewJTable(){
            initComponents();
            DefaultTableModel modelo = (DefaultTableModel) jTAlunos.getModel();
            jTAlunos.setRowSorter(new TableRowSorter(modelo));
            readJTable();
        }       
         
    
        public void readJTable(){
            DefaultTableModel modelo = (DefaultTableModel) jTAlunos.getModel();
            modelo.setNumRows(0);
            RfidDao rdao = new RfidDao();
            for(Rfid r: rdao.read()){
                modelo.addRow(new Object[]{
                r.getId(),
                r.getUid_tag(),
                r.getNome(),
                r.getMatricula(),
                r.getCurso()
            });
        }
    }
        
     
        /*
        public void ViewJTableRelatorio(){
            initComponents();
            DefaultTableModel modelo = (DefaultTableModel) jTRelatorio.getModel();
            jTRelatorio.setRowSorter(new TableRowSorter(modelo));
            readJRelatorio();
        }
        
    
        public void readJRelatorio(){
            
            DefaultTableModel modelo = (DefaultTableModel) jTRelatorio.getModel();
            modelo.setNumRows(0);
            RfidDao rdao = new RfidDao();
            for(Relatorio r: rdao.readRelatorio())
                modelo.addRow(new Object[]{
                r.getNome_aluno(),
                r.getMatricula_aluno(),
                r.getNome_evento(),
                r.getData_evento(),
                r.getResponsavel(),
                r.getCurso_aluno()
            });
        }
      
        */
        public void readJRelatorioBusca(String busca, String Parametro){
            
            DefaultTableModel modelo = (DefaultTableModel) jTRelatorio.getModel();
            modelo.setNumRows(0);
            RfidDao rdao = new RfidDao();
            for(Relatorio r: rdao.readRelatorioBusca(busca, Parametro))
                modelo.addRow(new Object[]{
                r.getNome_aluno(),
                r.getMatricula_aluno(),
                r.getNome_evento(),
                r.getData_evento(),
                r.getResponsavel(),
                r.getCurso_aluno()
            });
        }
    /*
    public class ViewJTbable extends javax.swing.JFrame{
        
        public void ViewJTable(){
            initComponents();
            DefaultTableModel modelo = (DefaultTableModel) jTAlunos.getModel();
            jTAlunos.setRowSorter(new TableRowSorter(modelo));
            readJTable();
        }
    
        public void readJTable(){
            DefaultTableModel modelo = (DefaultTableModel) jTAlunos.getModel();
            RfidDao rdao = new RfidDao();
            for(Rfid r: rdao.read()){
                modelo.addRow(new Object[]{
                r.getId(),
                r.getUid_tag(),
                r.getNome(),
                r.getMatricula(),
                r.getCurso()
            });
        }
    }   
    }
    */
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbRfid = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblMatricula = new javax.swing.JLabel();
        lblCurso = new javax.swing.JLabel();
        cbEventos = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_uid_tag = new javax.swing.JTextField();
        txt_nome = new javax.swing.JTextField();
        txt_matricula = new javax.swing.JTextField();
        txt_curso = new javax.swing.JTextField();
        btn_cadastrar = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtEvento = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtResponsavel = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        btnGerar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTAlunos = new javax.swing.JTable();
        btnAlunos = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        txtBusca = new javax.swing.JTextField();
        cbBusca = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTRelatorio = new javax.swing.JTable();
        btngerar = new javax.swing.JButton();

        jLabel11.setText("jLabel11");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel1.setText("Controle de Presença");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("UID Tag:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nome:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Matrícula:");

        lbRfid.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Curso:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Evento:");

        lblNome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblMatricula.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblCurso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbEventos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEventosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRfid))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNome))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMatricula))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbEventos, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCurso)))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lbRfid))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblNome))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblMatricula))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblCurso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cbEventos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(146, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Read", jPanel1);

        jLabel6.setText("UID Tag:");

        jLabel7.setText("Nome:");

        jLabel8.setText("Matrícula:");

        jLabel9.setText("Curso:");

        txt_uid_tag.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txt_nome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txt_matricula.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txt_curso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btn_cadastrar.setText("Cadastrar");
        btn_cadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(18, 18, 18)
                            .addComponent(txt_curso))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_matricula, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_uid_tag, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btn_cadastrar))
                .addContainerGap(232, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_uid_tag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_matricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt_curso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_cadastrar)
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cadastro", jPanel2);

        jLabel12.setText("Nome do Evento:");

        jLabel13.setText("Responsável:");

        jLabel14.setText("Data:");

        btnGerar.setText("Gerar Evento");
        btnGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtEvento))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGerar))
                .addContainerGap(259, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnGerar)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Evento", jPanel3);

        jTAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "UID Tag", "Nome", "Matricula", "Curso"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTAlunos);

        btnAlunos.setText("Buscar Alunos");
        btnAlunos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlunosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlunos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnAlunos)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Alunos", jPanel5);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        cbBusca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Buscar Por", "Aluno", "Data", "Evento", "Responsável", "Matricula Aluno", "Curso" }));
        cbBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBuscaActionPerformed(evt);
            }
        });

        jTRelatorio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nome", "Matricula", "Evento", "Data", "Responsável", "Curso"
            }
        ));
        jScrollPane3.setViewportView(jTRelatorio);

        btngerar.setText("Gerar Relatório");
        btngerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 380, Short.MAX_VALUE)
                                .addComponent(btngerar)))
                        .addGap(19, 19, 19))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)
                        .addContainerGap(160, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(cbBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btngerar)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Relatório", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastrarActionPerformed
        // TODO add your handling code here:
        Rfid cadastro = new Rfid();
        
        cadastro.setUid_tag(txt_uid_tag.getText());
        cadastro.setNome(txt_nome.getText());
        cadastro.setMatricula(Integer.parseInt(txt_matricula.getText()));
        cadastro.setCurso(txt_curso.getText());
        // fazendo a validação dos dados
    if ((txt_uid_tag.getText().isEmpty()) || (txt_nome.getText().isEmpty()) || (txt_matricula.getText().isEmpty()) || (txt_curso.getText().isEmpty())) {
        JOptionPane.showMessageDialog(null, "Os campos não podem retornar vazios");
        
    }
    else {
    // instanciando a classe UsuarioDAO do pacote dao e criando seu objeto dao
    RfidDao dao = new RfidDao();
    dao.save(cadastro);
    JOptionPane.showMessageDialog(null, "Aluno "+txt_nome.getText()+" inserido com sucesso! ");
    this.readJTable();
    
    
    
}

// apaga os dados preenchidos nos campos de texto
    txt_uid_tag.setText("");
    txt_nome.setText("");
    txt_matricula.setText("");
    txt_curso.setText("");
    }//GEN-LAST:event_btn_cadastrarActionPerformed

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed
        // TODO add your handling code here:
        Evento novoevento = new Evento();
        novoevento.setNome_evento(txtEvento.getText());
        novoevento.setResponsavel(txtResponsavel.getText());
        novoevento.setData(txtData.getText());
        if ((txtEvento.getText().isEmpty()) || (txtResponsavel.getText().isEmpty()) || (txtData.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Os campos não podem retornar vazios");
        }else {
            // instanciando a classe UsuarioDAO do pacote dao e criando seu objeto dao
            RfidDao dao = new RfidDao();
            dao.criarEvento(novoevento);
            JOptionPane.showMessageDialog(null, "Aluno "+txtEvento.getText()+" inserido com sucesso! ");
        }
        txtEvento.setText("");
        txtResponsavel.setText("");
        txtData.setText("");
        this.ViewComboBox();
        
        /*
        
        
        
        
        */
    }//GEN-LAST:event_btnGerarActionPerformed

    private void cbEventosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEventosActionPerformed
        // TODO add your handling code here:
        //ViewComboBox();
    
    }//GEN-LAST:event_cbEventosActionPerformed

    private void btnAlunosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlunosActionPerformed
        // TODO add your handling code here:
        this.readJTable();
    }//GEN-LAST:event_btnAlunosActionPerformed

    private void cbBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBuscaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBuscaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        String parametro,  busca;
        parametro = (String) cbBusca.getSelectedItem();
        busca = (String) txtBusca.getText();
        this.readJRelatorioBusca(busca, parametro);
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btngerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngerarActionPerformed
        // TODO add your handling code here:
        
        
        MessageFormat header = new MessageFormat("Lista de Alunos");
        MessageFormat footer = new MessageFormat("Page[0,number,integer]");
        
        try {
          
            jTRelatorio.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Erro de impressão]", e.getMessage());
        }
        
        /*
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Relatorio.pdf"));
            document.open();
            document.add(new Paragraph("Lista de Alunos"));
            PdfPTable table = new PdfPTable(6);
            table.addCell("Nome");
            table.addCell("Matricula");
            table.addCell("Evento");
            table.addCell("Data");
            table.addCell("Responsavel");
            table.addCell("Curso");
            document.add(table);
            
            
	} catch (DocumentException | FileNotFoundException ex) {
		System.out.println("Error:"+ex);
	} finally {
		document.close();
	}

	try {
		Desktop.getDesktop().open(new File("Relatorio.pdf"));
	} catch (IOException ex){
		System.out.println("Error:" +ex);
	}
        */
    }//GEN-LAST:event_btngerarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewRfid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewRfid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewRfid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewRfid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
     
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewRfid().setVisible(true);
            }
        });
        
     
        
        
    }
    
    

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlunos;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGerar;
    private javax.swing.JToggleButton btn_cadastrar;
    private javax.swing.JButton btngerar;
    private javax.swing.JComboBox<String> cbBusca;
    private javax.swing.JComboBox<Object> cbEventos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTAlunos;
    private javax.swing.JTable jTRelatorio;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbRfid;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblMatricula;
    private javax.swing.JLabel lblNome;
    private javax.swing.JTextField txtBusca;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JTextField txtEvento;
    private javax.swing.JTextField txtResponsavel;
    private javax.swing.JTextField txt_curso;
    private javax.swing.JTextField txt_matricula;
    private javax.swing.JTextField txt_nome;
    private javax.swing.JTextField txt_uid_tag;
    // End of variables declaration//GEN-END:variables
}
