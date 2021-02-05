package curso.api.spring.rest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
/** qualquer tipo de erro no projeto é interceptado por essa classe com as anotações acima **/
public class ControleExcecoes extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class}) /** essa anotação que ativa esse método para capturar a exceção **/
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest requisicao) {

        String msg = "";

        /** verificando se a excecao é do tipo MethodArgumentNotValidException **/
        if (ex instanceof MethodArgumentNotValidException){
            List<ObjectError> listaErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

            for (ObjectError objectError : listaErrors){
                /** percorrendo a lista de erros e atribuindo a msg **/
                msg += objectError.getDefaultMessage() + "\n";
            }
        }
        /** se for qualquer outro tipo de exceção entra aqui **/
        else {
            msg = ex.getMessage();
        }


        ObjetoErro objetoErro = new ObjetoErro();
        /** aqui é pego a mensagem do erro **/
        objetoErro.setError(msg);
        /** atribuido o código do erro e a descrição do erro **/
        objetoErro.setCode(status.value() + " ==> " + status.getReasonPhrase());

        /** retornando o objeto de erro, o cabeçalho e o status **/
        return new ResponseEntity<>(objetoErro,headers, status);
    }

    /** tratamentos dos erros mais comuns de banco de dados **/
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExcpetionDataIntegry(Exception ex){

        String msg = "";

        /** pegando a mensagem especifica da exceção DataIntegrityViolationException **/
        if ( ex instanceof DataIntegrityViolationException){
            msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
        }
        /** pegando a mensagem especifica da exceção ConstraintViolationException **/
        else if (ex instanceof  ConstraintViolationException){
            msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
        }
        /** pegando a mensagem especifica da exceção SQLException **/
        else if (ex instanceof SQLException){
            msg = ((SQLException) ex).getCause().getCause().getMessage();
        }
        else {
            msg = ex.getMessage(); /** outras mensagens de erros diversas relacionadas ao BD **/
        }

        ObjetoErro objetoErro = new ObjetoErro();
        objetoErro.setError(msg);

        /** os erros que acontecem com essas classes a maioria deles são erros INTERNO do servidor portanto uso a classe HTTP PARA MOSTRAR O CÓDIGO **/
        objetoErro.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()); /** MANDA CÓDIGO E DESCRIÇÃO DO ERRO DE UMA FORMA MAIS LEGÍVEL **/

        return new ResponseEntity<>(objetoErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
