package view;

import controller.UsuarioDao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.bean.Usuario;

public class UsuarioCadastroView extends javax.swing.JFrame {

    public UsuarioCadastroView() {
        initComponents();
        this.setLocationRelativeTo(null);
        //Retirando a Mximização da tela
        this.setResizable(false);
        iniciarTela();
    }

    int statusAcao = 0; //1 - Novo / 2 - Alterar 

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
        txtNome.setText("");
        txtSenha.setText("");
        txtId.setText("");
    }

    private void habilitaCampos() {
        txtPesquisa.setEnabled(true);
        txtNome.setEnabled(true);
        txtSenha.setEnabled(true);
        cbTipoUsuario.setEnabled(true);
    }

    private void desabilitaCampos() {
        txtPesquisa.setEnabled(false);
        txtNome.setEnabled(false);
        txtSenha.setEnabled(false);
        txtId.setEnabled(false);
        cbTipoUsuario.setEnabled(false);
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
        tbListagem.getColumnModel().getColumn(1).setPreferredWidth(200); //tipo usuario
        tbListagem.getColumnModel().getColumn(2).setPreferredWidth(200); //nome login 
        tbListagem.getColumnModel().getColumn(3).setPreferredWidth(100); //senha

    }

    private void iniciarTela() {
        limparCampos();
        habilitaBotoesCrud();
        desabilitaBotoesManterCrud();
        desabilitaCampos();
        desabilitaGridListagem();
        txtPesquisa.setEnabled(true);
        txtPesquisa.setRequestFocusEnabled(true);

    }

    private void consultar() {
        if (txtPesquisa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum registro localizado.");
            txtPesquisa.requestFocus();
        } else {
            habilitaGridListagem();
            carregalistagem(txtPesquisa.getText().toUpperCase());
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
        popularEvento();
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
            popularEvento();
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "É necessário selecionar um registro.");
            txtPesquisa.requestFocus();
        } else {

            UsuarioDao usuarioDao = new UsuarioDao();
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(Integer.parseInt(txtId.getText()));
            usuarioDao.excluir(usuario);

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
            Usuario usuario = new Usuario();
            UsuarioDao usuarioDao = new UsuarioDao();

            usuario.setLoginUsuario(txtNome.getText().toUpperCase());
            usuario.setSenhaUsuario(txtSenha.getText().toUpperCase());
            usuario.setTipoUsuario(cbTipoUsuario.getSelectedItem().toString());

            if (statusAcao == 1) {
                usuarioDao.cadastrar(usuario);
                limparCampos();
                iniciarTela();
                statusAcao = 0;
            }
            if (statusAcao == 2) {
                usuario.setIdUsuario(Integer.parseInt(txtId.getText()));
                usuarioDao.alterar(usuario);
                limparCampos();
                iniciarTela();
                habilitaGridListagem();
                limparListagem(tbListagem);
                txtPesquisa.requestFocus();
                statusAcao = 0;
            }
        }
    }

    private void carregaRegistroSelecionado() {
        if (tbListagem.getRowSelectionAllowed()) {
            try {
                cbTipoUsuario.removeAllItems();
                txtId.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 0).toString());
                cbTipoUsuario.addItem(tbListagem.getValueAt(tbListagem.getSelectedRow(), 1).toString());
                txtNome.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 2).toString());
                txtSenha.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 3).toString());
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

    private void carregalistagem(String consulta) {

        UsuarioDao usuarioDao = new UsuarioDao();
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        listaUsuario = usuarioDao.consultar(consulta);

        DefaultTableModel linha = (DefaultTableModel) tbListagem.getModel();
        limparListagem(tbListagem);
        try {
            for (Usuario usuario : listaUsuario) {
                linha.addRow(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getTipoUsuario(),
                    usuario.getLoginUsuario(),
                    usuario.getSenhaUsuario(),});
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem no grid");
        }

        if ((listaUsuario.size() == 0) && (statusAcao != 3)) {
            JOptionPane.showMessageDialog(this, "Nenhum registro localizado.");
        }

    }

    private boolean verificaCamposObrigatorios() {
        if (txtNome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            txtNome.requestFocus();
            return false;
        } else if (txtSenha.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            txtSenha.requestFocus();
            return false;
        } else if (cbTipoUsuario.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            return false;
        } else {

            return true;
        }
    }

    private void popularEvento() {
        cbTipoUsuario.removeAllItems();
        cbTipoUsuario.addItem("PRODUTOR");
        cbTipoUsuario.addItem("VENDEDOR");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btNovo = new javax.swing.JButton();
        btAterar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSenha = new javax.swing.JTextField();
        cbTipoUsuario = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbListagem = new javax.swing.JTable();
        btConsultar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setTitle("Cadastro de Usuário");

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Add2.png"))); // NOI18N
        btNovo.setText("Novo Usuário");
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

        jLabel1.setText("Pesquisa de Usuário ... Informe o Nome / Login");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Dados do Usuário"));

        jLabel3.setText("Nome / Login");

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

        jLabel5.setText("Senha");

        cbTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoUsuarioActionPerformed(evt);
            }
        });

        jLabel2.setText("Tipo de Usuário");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("*");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("*");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNome)
                    .addComponent(txtSenha)
                    .addComponent(cbTipoUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jLabel3)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        tbListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "TIPO DE USUÁRIO", "NOME / LOGIN DO USUÁRIO", "SENHA"
            }
        ));
        tbListagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListagemMouseClicked(evt);
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

        jLabel6.setText("Listagem de Usuários");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btExcluirActionPerformed

    private void cbTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoUsuarioActionPerformed

    private void btConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultarActionPerformed
        consultar();
    }//GEN-LAST:event_btConsultarActionPerformed

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
            java.util.logging.Logger.getLogger(UsuarioCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UsuarioCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UsuarioCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UsuarioCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new UsuarioCadastroView().setVisible(true);
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
    private javax.swing.JComboBox<String> cbTipoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbListagem;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JTextField txtSenha;
    // End of variables declaration//GEN-END:variables
}
