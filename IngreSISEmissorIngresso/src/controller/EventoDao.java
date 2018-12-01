package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import com.mysql.jdbc.PreparedStatement;
import model.bean.Evento;
import model.conexao.Conexao;
import java.util.ArrayList;
import java.util.List;

public class EventoDao {
    
    Connection con = null; // Criando conexão com DB
    PreparedStatement ps = null; // Preparando uma ação
    ResultSet rs = null; // Retorna uma Informações do banco
    String sql = "";

    public void cadastrar(Evento evento) {

        con = Conexao.getConexao();
	sql = "insert into evento(nomeEvento, localEvento, dataEvento, horarioEvento, statusEvento) values (?, ?, ? , ?, 'ATIVO')";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, evento.getNomeEvento());
            ps.setString(2, evento.getLocalEvento());
            ps.setString(3, evento.getDataEvento());
            ps.setString(4, evento.getHorarioEvento());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Novo Evento cadastrado com sucesso.");
	} catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar o Evento.");
	} finally {
            Conexao.closeConexao(con);
	}
    }
    
    public List<Evento> consultar(String nomeEvento){
		
	con = Conexao.getConexao();
	sql = "select *from evento where nomeEvento like '" + nomeEvento + "' and statusEvento = 'ATIVO'";
	List<Evento> lista = new ArrayList<Evento>();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    Evento evento = new Evento();
                    evento.setIdEvento(rs.getInt("idEvento"));
                    evento.setNomeEvento(rs.getString("nomeEvento"));
                    evento.setLocalEvento(rs.getString("localEvento"));
                    evento.setDataEvento(rs.getString("dataEvento"));
                    evento.setHorarioEvento(rs.getString("horarioEvento"));
                    lista.add(evento);
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Eventos!");
        } finally {
                Conexao.closeConexao(con);
        }
        return lista;
    }

    public void alterar(Evento evento) {

	con = Conexao.getConexao();
	sql = "update evento set nomeEvento = ?, localEvento = ?,  dataEvento = ?, horarioEvento = ? where idEvento = ?";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, evento.getNomeEvento());
            ps.setString(2, evento.getLocalEvento());
            ps.setString(3, evento.getDataEvento());
            ps.setString(4, evento.getHorarioEvento());
            ps.setInt(5, evento.getIdEvento());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Dados do Evento alterado com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Altear dados!");
        } finally {
            Conexao.closeConexao(con);
	}
    }
    
    public void excluir(Evento evento) {

	con = Conexao.getConexao();
	sql = "update evento set statusEvento = 'DELETADO' where idEvento = ?";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, evento.getIdEvento());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Evento excluído com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel excluir os dados!");
        } finally {
            Conexao.closeConexao(con);
	}
    }
    
    public List<Evento> listar(){
		
	con = Conexao.getConexao();
	sql = "select idEvento, nomeEvento from evento where statusEvento = 'ATIVO'";
        
        List<Evento> lista = new ArrayList<Evento>();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    Evento evento = new Evento();
                    evento.setNomeEvento(rs.getString("nomeEvento"));
                    lista.add(evento);
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Eventos!");
        } finally {
            Conexao.closeConexao(con);
        }
        return lista;
    }
    
    public Evento consultaUnicoEvento(String nomeEvento){
		
	con = Conexao.getConexao();
	sql = "select idEvento, nomeEvento from evento where nomeEvento = '" + nomeEvento +"' and statusEvento = 'ATIVO'"; // Utilizado para verificar o id do evento selecionado no combo ao Salvar na tela de Cadastro de Ingresso
        Evento evento = new Evento();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    evento.setIdEvento(rs.getInt("idEvento"));
                    evento.setNomeEvento(rs.getString("nomeEvento"));
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Eventos!");
        } finally {
               Conexao.closeConexao(con);
        }
        return evento;
    }
}
