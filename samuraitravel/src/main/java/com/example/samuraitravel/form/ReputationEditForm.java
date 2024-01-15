package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ReputationEditForm {
	private Integer score;
	
	@NotBlank(message="文字を入力して下さい")
	private String description;
}
