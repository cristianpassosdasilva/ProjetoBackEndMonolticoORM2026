package br.edu.iftm.tspi.pbackorm.e_commerce.exception;

public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }

}
