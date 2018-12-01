package controller;

import com.mysql.jdbc.PreparedStatement;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JOptionPane;
import model.bean.Venda;
import model.conexao.Conexao;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class VendaDao {

    Connection con = null; // Criando conexão com DB
    PreparedStatement ps = null; // Preparando uma ação
    ResultSet rs = null; // Retorna uma Informações do banco
    String sql = "";

    public void gerarVenda(Venda venda) {

        con = Conexao.getConexao();
        sql = "insert into venda(formaPagamentoVenda, valorVenda, idEventoVenda, idIngressoVenda, idLoteVenda) values (?, ?, ? , ?, ?)";
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, venda.getFormaPagamentoVenda());
            ps.setDouble(2, venda.getValorVenda());
            ps.setInt(3, venda.getIdEventoVenda());
            ps.setInt(4, venda.getIdIngressoVenda());
            ps.setInt(5, venda.getIdLoteVenda());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Venda do ingresso realizada com sucesso.");
        } catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar o Evento.");
        } finally {
            Conexao.closeConexao(con);
        }
    }

    public int verificaIdUltimaVenda() {
        con = Conexao.getConexao();
        sql = "select max(idVenda) as idVenda from venda";
        int idVenda = 0;
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                idVenda = (rs.getInt("idVenda"));
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel localizar o ID da ultima venda!");
        } finally {
            Conexao.closeConexao(con);
        }
        return idVenda;
    }

    public void gerarIngresso() {

        Connection con = null;
        con = (Connection) Conexao.getConexao();

        //verifica o id da ultima venda
        int idVenda =  verificaIdUltimaVenda();
        sql = "select v.idvenda, e.nomeEvento, e.dataEvento, e.horarioEvento, i.descricaoIngresso, l.numeroLote, v.valorVenda from venda v\n"
                + "inner join evento e on (v.idEventoVenda = e.idEvento)\n"
                + "inner join ingresso i on (v.idIngressoVenda = i.idIngresso)\n"
                + "inner join lote l on (v.idLoteVenda = l.idLote)\n"
                + "where v.idVenda = " + idVenda;
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            rs = (ResultSet) ps.executeQuery();

        } catch (Exception e) {
            e.getMessage();
        }
        
        HashMap<String, Object> parametro = new HashMap<String, Object>();
        parametro.put("idVendaCodBar", Integer.toString(idVenda));
        
        InputStream nomeArqRelatorio = getClass().getResourceAsStream("/view/IngressoView.jasper");
        JasperPrint jasperPrint = null;
        
        try {
            JRDataSource jrds = new JRResultSetDataSource(rs);
            parametro.put("REPORT_CONNECTION", con);
            jasperPrint = JasperFillManager.fillReport(nomeArqRelatorio, parametro, jrds);
            
        } catch (JRException e) {
            System.out.println("Erro " + e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        JasperViewer view = new JasperViewer(jasperPrint, false);
        view.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
        view.setTitle("INGRESIS - Sistema de Emissão de Ingressos");
        view.setVisible(true);
    }
}
