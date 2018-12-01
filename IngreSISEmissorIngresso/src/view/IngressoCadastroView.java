package view;

import controller.EventoDao;
import controller.IngressoDao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.bean.Evento;
import model.bean.Ingresso;

public class IngressoCadastroView extends javax.swing.JFrame {

    int statusAcao = 0; //1 - Novo / 2 - Alterar 

    public IngressoCadastroView() {
        initComponents();
        this.setLocationRelativeTo(null);
        //Retirando a Mximização da tela
        this.setResizable(false);
        iniciarTela();
    }

    private void habilitaBotoesCrud() {
        btAterar.setEnabled(true);
        btExcluir.setEnabled(true);
        btConsultar.setEnabled(true);
        btExcluir.setEnabled(true);
        btNovo.setEnabled(true);
    }

    private void desabilitaBotoesCrud() {
        btAterar.setEnabled(false);
        btExcluir.setEnabled(false);
        btConsultar.setEnabled(false);
        btExcluir.setEnabled(false);
        btNovo.setEnabled(false);
    }

    private void habilitaBotoesManterCrud() {
        btCancelar.setEnabled(true);
        btSalvar.setEnabled(true);
    }

    private void desabilitaBotoesManterCrud() {
        btCancelar.setEnabled(false);
        btSalvar.setEnabled(false);
    }

    private void limparCampos() {
        txtPesquisa.setText("");
        txtDescricaoIngresso.setText("");
        txtId.setText("");
        cbEvento.removeAllItems();
    }

    private void habilitaCampos() {
        txtPesquisa.setEnabled(true);
        txtDescricaoIngresso.setEnabled(true);
        cbEvento.setEnabled(true);
    }

    private void desabilitaCampos() {
        txtPesquisa.setEnabled(false);
        txtDescricaoIngresso.setEnabled(false);
        txtId.setEnabled(false);
        cbEvento.setEnabled(false);
    }

    private void desabilitaGridListagem() {
        tbListagem.setEnabled(false);
        tbListagem.setRowSelectionAllowed(false);
    }

    private void habilitaGridListagem() {
        tbListagem.setEnabled(true);
        tbListagem.setRowSelectionAllowed(true);
    }

    private void tamanhoColunasGridListagem() {
        tbListagem.setAutoResizeMode(0); //Desabilita tamanho automatico coluna
        tbListagem.getColumnModel().getColumn(0).setPreferredWidth(50);  //id
        tbListagem.getColumnModel().getColumn(1).setPreferredWidth(200); //nome evento 
        tbListagem.getColumnModel().getColumn(2).setPreferredWidth(200); //descrição ingresso
    }

    private void iniciarTela() {
        limparCampos();
        habilitaBotoesCrud();
        desabilitaBotoesManterCrud();
        desabilitaCampos();
        desabilitaGridListagem();
        txtPesquisa.setEnabled(true);
        txtPesquisa.setRequestFocusEnabled(true);
        tamanhoColunasGridListagem();
    }

    private void consultar() {
        if (txtPesquisa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum registro localizado.");
            txtPesquisa.requestFocus();
        } else {
            habilitaGridListagem();
            carregaListagem(txtPesquisa.getText().toUpperCase());
        }
    }

    private void Novo() {
        desabilitaBotoesCrud();
        desabilitaGridListagem();
        habilitaBotoesManterCrud();
        limparCampos();
        limparListagem(tbListagem);
        habilitaCampos();
        txtPesquisa.setEnabled(false);
        listarEvento();
    }

    private void alterar() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "É necessário selecionar um registro.");
            txtPesquisa.requestFocus();
        } else {
            desabilitaBotoesCrud();
            habilitaBotoesManterCrud();
            habilitaCampos();
            txtPesquisa.setEnabled(false);
            cbEvento.setEnabled(false);
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "É necessário selecionar um registro.");
            txtPesquisa.requestFocus();
        } else {
            IngressoDao ingressoDao = new IngressoDao();
            Ingresso ingresso = new Ingresso();
            ingresso.setIdIngresso(Integer.parseInt(txtId.getText()));
            ingressoDao.excluir(ingresso);
            DefaultTableModel linha = (DefaultTableModel) tbListagem.getModel();
            linha.removeRow(tbListagem.getSelectedRow());
            limparCampos();
            iniciarTela();
            if (tbListagem.getRowCount() > 0) {
                habilitaGridListagem();
            } else {
                desabilitaGridListagem();
            }
        }
    }

    private void salvar() {
        if (verificaCamposObrigatorios()) {

            Ingresso ingresso = new Ingresso();
            IngressoDao ingressoDao = new IngressoDao();
            EventoDao eventoDao = new EventoDao();
            ingresso.setIdEvento(eventoDao.consultaUnicoEvento(cbEvento.getSelectedItem().toString()).getIdEvento());
            ingresso.setDescricaoIngresso(txtDescricaoIngresso.getText().toUpperCase());
            ingresso.setIdIngressoEvento(ingresso.getIdEvento());

            if (statusAcao == 1) { //1 - Cadastrar
                ingressoDao.cadastrar(ingresso);
                limparCampos();
                iniciarTela();
                statusAcao = 0;
            }
            if (statusAcao == 2) { //2 - Alterar
                ingresso.setIdIngresso(Integer.parseInt(txtId.getText()));
                ingressoDao.alterar(ingresso);
                limparCampos();
                iniciarTela();
                limparListagem(tbListagem);
                habilitaGridListagem();
                statusAcao = 0;
            }
        }
    }

    private void carregaRegistroSelecionado() {
        if (tbListagem.getRowSelectionAllowed()) {
            try {
                cbEvento.removeAllItems();
                txtId.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 0).toString());
                cbEvento.addItem(tbListagem.getValueAt(tbListagem.getSelectedRow(), 1).toString());
                txtDescricaoIngresso.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 2).toString());
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void limparListagem(JTable tbGridListagem) {
        DefaultTableModel linha = (DefaultTableModel) tbGridListagem.getModel();
        while (linha.getRowCount() > 0) {
            linha.removeRow(0);
        }
    }

    private void carregaListagem(String consulta) {

        IngressoDao ingressoDao = new IngressoDao();
        List<Ingresso> listaIngresso = new ArrayList<Ingresso>();
        listaIngresso = ingressoDao.consultar(consulta);

        DefaultTableModel linha = (DefaultTableModel) tbListagem.getModel();
        limparListagem(tbListagem);
        try {
            for (Ingresso ingresso : listaIngresso) {
                linha.addRow(new Object[]{
                    ingresso.getIdIngresso(),
                    ingresso.getNomeEvento(),
                    ingresso.getDescricaoIngresso(),});
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem n o grid");
        }

        if ((listaIngresso.size() == 0) && (statusAcao != 3)) {
            JOptionPane.showMessageDialog(this, "Nenhum registro localizado.");
        }

    }

    private boolean verificaCamposObrigatorios() {
        if (cbEvento.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            return false;
        }
        if (txtDescricaoIngresso.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            txtDescricaoIngresso.requestFocus();
            return false;
        } else {

            return true;
        }
    }

    private void listarEvento() {
        cbEvento.removeAllItems();
        EventoDao eventoDao = new EventoDao();
        Evento evento = new Evento();
        List<Evento> listaEvento = new ArrayList<Evento>();
        listaEvento = eventoDao.listar();
        try {
            for (Evento event : listaEvento) {
                cbEvento.addItem(event.getNomeEvento());
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem n o grid");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbListagem = new javax.swing.JTable();
        btConsultar = new javax.swing.JButton();
        btNovo = new javax.swing.JButton();
        btAterar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbEvento = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtDescricaoIngresso = new javax.swing.JTextField();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setTitle("Cadastro de Ingresso");

        jLabel1.setText("Pesquisa... Informe o Nome do Evento");

        tbListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "EVENTO", "DESCRIÇÃO DO INGRESSO"
            }
        ));
        tbListagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListagemMouseClicked(evt);
            }
        });
        tbListagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListagemKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbListagem);

        btConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/consultar.png"))); // NOI18N
        btConsultar.setText("Consultar");
        btConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultarActionPerformed(evt);
            }
        });

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Add2.png"))); // NOI18N
        btNovo.setText("Novo Ingresso");
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btAterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Alterar.png"))); // NOI18N
        btAterar.setText("Alterar");
        btAterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAterarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Excluir.png"))); // NOI18N
        btExcluir.setText("Excluir");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Dados do Ingresso"));

        jLabel2.setText("Evento");

        cbEvento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbEventoItemStateChanged(evt);
            }
        });
        cbEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEventoActionPerformed(evt);
            }
        });
        cbEvento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbEventoKeyPressed(evt);
            }
        });

        jLabel3.setText("Descrição do Ingresso");

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cancelar.png"))); // NOI18N
        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Salvar.png"))); // NOI18N
        btSalvar.setText("Salvar");
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        jLabel4.setText("ID");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("*");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbEvento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDescricaoIngresso)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 243, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDescricaoIngresso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jLabel5.setText("Listagem de Ingressos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btAterar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAterar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(btConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultarActionPerformed
        consultar();

    }//GEN-LAST:event_btConsultarActionPerformed

    private void tbListagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListagemKeyPressed

    }//GEN-LAST:event_tbListagemKeyPressed

    private void tbListagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListagemMouseClicked
        carregaRegistroSelecionado();
    }//GEN-LAST:event_tbListagemMouseClicked

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
        Novo();
        statusAcao = 1;
    }//GEN-LAST:event_btNovoActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btAterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAterarActionPerformed
        alterar();
        statusAcao = 2;
    }//GEN-LAST:event_btAterarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        iniciarTela();
    }//GEN-LAST:event_btCancelarActionPerformed

    private void cbEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEventoActionPerformed

    }//GEN-LAST:event_cbEventoActionPerformed

    private void cbEventoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbEventoKeyPressed

    }//GEN-LAST:event_cbEventoKeyPressed

    private void cbEventoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbEventoItemStateChanged

    }//GEN-LAST:event_cbEventoItemStateChanged

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
            java.util.logging.Logger.getLogger(IngressoCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngressoCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngressoCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngressoCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IngressoCadastroView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAterar;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btConsultar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btSalvar;
    private javax.swing.JComboBox<String> cbEvento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbListagem;
    private javax.swing.JTextField txtDescricaoIngresso;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
