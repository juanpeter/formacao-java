package br.com.indracompany.gestaoalunos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Aluno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;

	@NotBlank(message = "Nome é obrigatório!")
	private String nome;

	@Positive(message = "Nota é obrigatória")
	private Integer nota;

}
