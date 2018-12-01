package model.bean;

public class Lote {
    
    private int idLote;
    private String numeroLote;
    private int quantidadeLote;
    private Double valorLote;
    private int idIngressoLote;
    private String statusLote;
    Ingresso ingresso;
    Evento evento;
    
    public Lote(){
        ingresso = new Ingresso();
        evento = new Evento();
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public int getQuantidadeLote() {
        return quantidadeLote;
    }

    public void setQuantidadeLote(int quantidadeLote) {
        this.quantidadeLote = quantidadeLote;
    }

    public Double getValorLote() {
        return valorLote;
    }

    public void setValorLote(Double valorLote) {
        this.valorLote = valorLote;
    }

    public int getIdIngressoLote() {
        return idIngressoLote;
    }

    public void setIdIngressoLote(int idIngressoLote) {
        this.idIngressoLote = idIngressoLote;
    }

    public String getStatusLote() {
        return statusLote;
    }

    public void setStatusLote(String statusLote) {
        this.statusLote = statusLote;
    }
    
    public int geIdIngresso(){
        return this.ingresso.getIdIngresso();
    } 
    
    public void setIdIngresso(int idIngresso){
        ingresso.setIdIngresso(idIngresso);
    }
    
    public String getDescricaoIngresso(){
        return this.ingresso.getDescricaoIngresso();
    } 
    
    public void setDescricaoIngresso(String descricaoIngresso){
        ingresso.setDescricaoIngresso(descricaoIngresso);
    }
    
    public int geIdEvento(){
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
