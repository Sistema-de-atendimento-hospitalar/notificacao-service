package br.com.bublemedical.notificationservice.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import br.com.bublemedical.notificationservice.domain.Wrapper;

import javassist.NotFoundException;

@ControllerAdvice
public class ApiExceptionHandle extends ResponseEntityExceptionHandler {

	public static final String INTERNAL_ERROR = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador do sistema";

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}

		String detail = "O corpo da requsição está inválido. Verifique erros de sintaxe.";
		Wrapper wrapper = createWrapper(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail);
		return this.handleExceptionInternal(ex, wrapper, new HttpHeaders(), status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Wrapper wrapper = createWrapper(status, problemType, detail);

		return handleExceptionInternal(ex, wrapper, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {

	    String path = joinPath(ex.getPath());
	    
	    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
	    String detail = String.format("A propriedade '%s' não existe. "
	            + "Corrija ou remova essa propriedade e tente novamente.", path);

	    Wrapper wrapper = createWrapper(status, problemType, detail);
	    return handleExceptionInternal(ex, wrapper, headers, status, request);
	}               

//	@ExceptionHandler(BusinessException.class)
//	public ResponseEntity<Object> handleBadRequest(BusinessException ex, WebRequest request) {
//		HttpStatus status = HttpStatus.BAD_REQUEST;
//		Wrapper wrapper = createWrapper(status, ProblemType.BUSINESS_EXCEPTION, ex.getMessage());
//		return this.handleExceptionInternal(ex, wrapper, new HttpHeaders(), status, request);
//	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		Wrapper wrapper = createWrapper(status, ProblemType.ENTIDADE_NAO_ENONTRADA, ex.getMessage());
		return this.handleExceptionInternal(ex, wrapper, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String description = INTERNAL_ERROR;
		Wrapper wrapper = createWrapper(status, ProblemType.ERRO_DE_SISTEMA, description);
		return this.handleExceptionInternal(ex, wrapper, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String description = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());
		ex.printStackTrace();
		Wrapper wrapper = createWrapper(status, ProblemType.RECURSO_NAO_ENCONTRADO, description);
		return this.handleExceptionInternal(ex, wrapper, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String description = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";
		Wrapper wrapper = createWrapper(status, ProblemType.DADOS_INVALIDOS, description);
		wrapper.setUserMessage(description);
		return this.handleExceptionInternal(ex, wrapper, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Wrapper problem = createWrapper(status, problemType, detail);
		problem.setUserMessage(INTERNAL_ERROR);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = createWrapper(status.getReasonPhrase(), status.value(), INTERNAL_ERROR);
			
		} else if (body instanceof String) {
			body = createWrapper((String) body, status.value(), INTERNAL_ERROR);
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Wrapper createWrapper(HttpStatus status, ProblemType problemType, String detail) {
		Wrapper wrapper = new Wrapper();
		wrapper.setDate(LocalDateTime.now());
		wrapper.setStatus(status.value());
		wrapper.setTitle(problemType.getTitle());
		wrapper.setType(problemType.getUri());
		wrapper.setDetail(detail);
		
		return wrapper;
	}
	
	private Wrapper createWrapper(String title, Integer status, String message) {
		Wrapper wrapper = new Wrapper();
		wrapper.setDate(LocalDateTime.now());
		wrapper.setTitle(title);
		wrapper.setStatus(status);
		wrapper.setUserMessage(message);
		
		return wrapper;
	}
	
	private String joinPath(List<Reference> references) {
	    return references.stream()
	        .map(Reference::getFieldName)
	        .collect(Collectors.joining("."));
	}

}