
package model.bean;


public class Ingresso {
    
    private int idIngresso;
    private String descricaoIngresso;
    private int idIngressoEvento;
    private String statusIngresso;
    Evento evento;
    
    public Ingresso(){
        evento = new Evento();
    }

    public int getIdIngresso() {
        return idIngresso;
    }

    public void setIdIngresso(int idIngresso) {
        this.idIngresso = idIngresso;
    }

    public String getDescricaoIngresso() {
        return descricaoIngresso;
    }

    public void setDescricaoIngresso(String descricaoIngresso) {
        this.descricaoIngresso = descricaoIngresso;
    }

    public int getIdIngressoEvento() {
        return idIngressoEvento;
    }

    public void setIdIngressoEvento(int idIngressoEvento) {
        this.idIngressoEvento = idIngressoEvento;
    }

    public String getStatusIngresso() {
        return statusIngresso;
    }

    public void setStatusIngresso(String statusIngresso) {
        this.statusIngresso = statusIngresso;
    }
    
 
    public int getIdEvento(){
        return this.evento.getIdEvento();
    } 
    
    public void setIdEvento(int idEvento){
        evento.setIdEvento(idEvento);
    }
    
    public String getNomeEvento(){
        return this.evento.getNomeEvento();
    } 
    
    public void setNomeEvento(String nomeEvento){
        evento.setNomeEvento(nomeEvento);
    }
    
}
